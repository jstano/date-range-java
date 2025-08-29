package com.stano.daterange;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Java port of the Rust DateRange core with composition-based prior/next navigation.
 * <p>
 * This class is immutable. All navigation methods (prior/next/etc.) return new DateRange instances
 * preserving the same prior/next functions and startDay metadata.
 */
public final class DateRange implements Iterable<LocalDate>, Comparable<DateRange> {
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final int len; // inclusive length in days
  private final UnaryOperator<DateRange> priorFn; // may be null
  private final UnaryOperator<DateRange> nextFn;  // may be null
  private final Integer startDay; // optional: used by Monthly segments

  public DateRange(LocalDate startDate, LocalDate endDate) {
    this(startDate, endDate, null, null, null);
  }

  public static DateRange of(LocalDate startDate, LocalDate endDate) {
    return new DateRange(startDate, endDate);
  }

  static DateRange ofWithPriorNext(LocalDate startDate,
                                   LocalDate endDate,
                                   UnaryOperator<DateRange> priorFn,
                                   UnaryOperator<DateRange> nextFn) {
    return new DateRange(startDate, endDate, priorFn, nextFn, null);
  }

  static DateRange ofWithPriorNextStartDay(LocalDate startDate,
                                           LocalDate endDate,
                                           UnaryOperator<DateRange> priorFn,
                                           UnaryOperator<DateRange> nextFn,
                                           Integer startDay) {
    return new DateRange(startDate, endDate, priorFn, nextFn, startDay);
  }

  private DateRange(LocalDate startDate,
                    LocalDate endDate,
                    UnaryOperator<DateRange> priorFn,
                    UnaryOperator<DateRange> nextFn,
                    Integer startDay) {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("dates required");
    }
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("end before start");
    }
    this.startDate = startDate;
    this.endDate = endDate;
    long days = ChronoUnit.DAYS.between(startDate, endDate) + 1; // inclusive
    this.len = Math.toIntExact(days);
    this.priorFn = priorFn;
    this.nextFn = nextFn;
    this.startDay = startDay;
  }

  public LocalDate startDate() {
    return startDate;
  }

  public LocalDate endDate() {
    return endDate;
  }

  public int len() {
    return len;
  }

  public Optional<Integer> startDay() {
    return Optional.ofNullable(startDay);
  }

  public List<LocalDate> dates() {
    List<LocalDate> res = new ArrayList<>(len);
    LocalDate cur = startDate;
    while (!cur.isAfter(endDate)) {
      res.add(cur);
      cur = cur.plusDays(1);
    }
    return res;
  }

  public Optional<LocalDate> dateAt(int index) {
    if (index < 0 || index >= len) {
      return Optional.empty();
    }
    return Optional.of(startDate.plusDays(index));
  }

  public List<LocalDate> datesForDay(DayOfWeek day) {
    List<LocalDate> res = new ArrayList<>();
    for (LocalDate d : this) {
      if (d.getDayOfWeek() == day) {
        res.add(d);
      }
    }
    return res;
  }

  public boolean containsDate(LocalDate date) {
    return !date.isBefore(startDate) && !date.isAfter(endDate);
  }

  public boolean containsRange(DateRange other) {
    return !other.startDate.isBefore(this.startDate) && !other.endDate.isAfter(this.endDate);
  }

  public boolean overlaps(DateRange other) {
    return !this.startDate.isAfter(other.endDate) && !this.endDate.isBefore(other.startDate);
  }

  public boolean overlapsAny(List<DateRange> ranges) {
    for (DateRange r : ranges) {
      if (this.overlaps(r)) {
        return true;
      }
    }
    return false;
  }

  public DateRange rangeContainingDate(LocalDate date) {
    DateRange range = createNew(this.startDate, this.endDate);
    while (!range.containsDate(date)) {
      if (date.isAfter(range.endDate)) {
        range = range.next();
      }
      else {
        range = range.prior();
      }
    }
    return range;
  }

  public DateRange prior() {
    if (priorFn != null) {
      return priorFn.apply(this);
    }
    // default shift by length
    return createNew(startDate.minusDays(len), endDate.minusDays(len));
  }

  public DateRange priorN(int number) {
    DateRange r = this;
    for (int i = 0; i < number; i++) {
      r = r.prior();
    }
    return r;
  }

  public DateRange next() {
    if (nextFn != null) {
      return nextFn.apply(this);
    }
    // default shift by length
    return createNew(startDate.plusDays(len), endDate.plusDays(len));
  }

  public DateRange nextN(int number) {
    DateRange r = this;
    for (int i = 0; i < number; i++) {
      r = r.next();
    }
    return r;
  }

  public List<DateRange> rangesBefore(int number) {
    return rangesBeforeImpl(number, false);
  }

  public List<DateRange> rangesBeforeInclusive(int number) {
    return rangesBeforeImpl(number, true);
  }

  public List<DateRange> rangesAfter(int number) {
    return rangesAfterImpl(number, false);
  }

  public List<DateRange> rangesAfterInclusive(int number) {
    return rangesAfterImpl(number, true);
  }

  public List<DateRange> rangesWindow(int before, int after) {
    List<DateRange> res = new ArrayList<>(before + after + 1);
    res.addAll(rangesBeforeImpl(before, true)); // includes self
    List<DateRange> afters = rangesAfterImpl(after, false);
    res.addAll(afters);
    return res;
  }

  public List<DateRange> rangesContainingSpan(LocalDate fromDate, LocalDate toDate) {
    if (fromDate == null || toDate == null) {
      throw new IllegalArgumentException("dates required");
    }
    if (toDate.isBefore(fromDate)) {
      throw new IllegalArgumentException("to before from");
    }
    List<DateRange> res = new ArrayList<>();
    DateRange r = rangeContainingDate(fromDate);
    res.add(r.copy());
    while (r.endDate.isBefore(toDate)) {
      r = r.next();
      res.add(r.copy());
    }
    Collections.sort(res);
    return res;
  }

  private List<DateRange> rangesBeforeImpl(int number, boolean includeSelf) {
    List<DateRange> res = new ArrayList<>(number + 1);
    if (includeSelf) {
      res.add(copy());
    }
    DateRange cur = copy();
    for (int i = 0; i < number; i++) {
      cur = cur.prior();
      res.add(cur.copy());
    }
    // Reverse to match Rust ordering (earlier first)
    Collections.reverse(res);
    return res;
  }

  private List<DateRange> rangesAfterImpl(int number, boolean includeSelf) {
    List<DateRange> res = new ArrayList<>(number + 1);
    if (includeSelf) {
      res.add(copy());
    }
    DateRange cur = copy();
    for (int i = 0; i < number; i++) {
      cur = cur.next();
      res.add(cur.copy());
    }
    return res;
  }

  private DateRange createNew(LocalDate start, LocalDate end) {
    return new DateRange(start, end, this.priorFn, this.nextFn, this.startDay);
  }

  private DateRange copy() {
    return createNew(this.startDate, this.endDate);
  }

  @Override
  public Iterator<LocalDate> iterator() {
    return new Iterator<LocalDate>() {
      private LocalDate cur = startDate;

      @Override
      public boolean hasNext() {
        return !cur.isAfter(endDate);
      }

      @Override
      public LocalDate next() {
        LocalDate r = cur;
        cur = cur.plusDays(1);
        return r;
      }
    };
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DateRange)) {
      return false;
    }
    DateRange that = (DateRange)o;
    return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDate, endDate);
  }

  @Override
  public int compareTo(DateRange o) {
    return this.startDate.compareTo(o.startDate);
  }
}
