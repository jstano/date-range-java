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
 * An inclusive range of dates, from a start date through an end date.
 *
 * <p>A DateRange can navigate to adjacent ranges via {@link #prior()} and {@link #next()}. By
 * default these shift the range by its own length in days, but the {@code *DateRange} factory
 * classes (e.g. {@link WeeklyDateRange}, {@link MonthlyDateRange}) create ranges whose prior/next
 * navigation instead follows calendar-based rules (e.g. calendar months).
 *
 * <p>This class is immutable and thread-safe.
 */
public final class DateRange implements Iterable<LocalDate>, Comparable<DateRange> {
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final int numberOfDays; // inclusive length in days
  private final UnaryOperator<DateRange> priorFn; // maybe null
  private final UnaryOperator<DateRange> nextFn; // maybe null
  private final Integer startDay; // optional: used by Monthly segments

  /**
   * Creates a new DateRange spanning the given start and end dates, using the default
   * shift-by-length navigation for {@link #prior()} and {@link #next()}.
   *
   * @param startDate The start date of the range.
   * @param endDate The end date of the range; must not be before startDate.
   */
  public DateRange(LocalDate startDate, LocalDate endDate) {
    this(startDate, endDate, null, null, null);
  }

  /**
   * Creates a new DateRange spanning the given start and end dates, using the default
   * shift-by-length navigation for {@link #prior()} and {@link #next()}.
   *
   * @param startDate The start date of the range.
   * @param endDate The end date of the range; must not be before startDate.
   * @return A new DateRange with the given start and end dates.
   */
  public static DateRange of(LocalDate startDate, LocalDate endDate) {
    return new DateRange(startDate, endDate);
  }

  static DateRange ofWithPriorNext(
      LocalDate startDate,
      LocalDate endDate,
      UnaryOperator<DateRange> priorFn,
      UnaryOperator<DateRange> nextFn) {
    return new DateRange(startDate, endDate, priorFn, nextFn, null);
  }

  static DateRange ofWithPriorNextStartDay(
      LocalDate startDate,
      LocalDate endDate,
      UnaryOperator<DateRange> priorFn,
      UnaryOperator<DateRange> nextFn,
      Integer startDay) {
    return new DateRange(startDate, endDate, priorFn, nextFn, startDay);
  }

  private DateRange(
      LocalDate startDate,
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
    long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1; // inclusive
    this.numberOfDays = Math.toIntExact(numberOfDays);
    this.priorFn = priorFn;
    this.nextFn = nextFn;
    this.startDay = startDay;
  }

  /**
   * Gets the start date of the range.
   *
   * @return The start date.
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * Gets the end date of the range.
   *
   * @return The end date.
   */
  public LocalDate getEndDate() {
    return endDate;
  }

  /**
   * Gets the number of days spanned by the range, inclusive of both the start and end dates.
   *
   * @return The number of days in the range.
   */
  public int getNumberOfDays() {
    return numberOfDays;
  }

  /**
   * Gets the configured start day of month, if this range was created by a factory (such as {@link
   * MonthlyDateRange}) that supports a non-default month start day.
   *
   * @return The configured start day, or empty if this range has no configured start day.
   */
  public Optional<Integer> startDay() {
    return Optional.ofNullable(startDay);
  }

  /**
   * Gets every date in the range, from the start date through the end date, inclusive.
   *
   * @return A list of every date in the range, in chronological order.
   */
  public List<LocalDate> dates() {
    List<LocalDate> res = new ArrayList<>(numberOfDays);
    LocalDate cur = startDate;
    while (!cur.isAfter(endDate)) {
      res.add(cur);
      cur = cur.plusDays(1);
    }
    return res;
  }

  /**
   * Gets the date at the given zero-based offset from the start of the range.
   *
   * @param index The zero-based offset from the start date.
   * @return The date at that offset, or empty if index is negative or beyond the end of the range.
   */
  public Optional<LocalDate> dateAt(int index) {
    if (index < 0 || index >= numberOfDays) {
      return Optional.empty();
    }
    return Optional.of(startDate.plusDays(index));
  }

  /**
   * Gets every date in the range that falls on the given day of the week.
   *
   * @param day The day of the week to match.
   * @return A list of matching dates, in chronological order.
   */
  public List<LocalDate> datesForDay(DayOfWeek day) {
    List<LocalDate> res = new ArrayList<>();
    for (LocalDate d : this) {
      if (d.getDayOfWeek() == day) {
        res.add(d);
      }
    }
    return res;
  }

  /**
   * Checks if a date falls within this range, inclusive of the start and end dates.
   *
   * @param date The date to check.
   * @return true if date is on or after the start date and on or before the end date.
   */
  public boolean containsDate(LocalDate date) {
    return !date.isBefore(startDate) && !date.isAfter(endDate);
  }

  /**
   * Checks if another range is fully contained within this range.
   *
   * @param other The DateRange to check.
   * @return true if other's start and end dates both fall within this range.
   */
  public boolean containsRange(DateRange other) {
    return !other.startDate.isBefore(this.startDate) && !other.endDate.isAfter(this.endDate);
  }

  /**
   * Checks if another range overlaps this range.
   *
   * @param other The DateRange to check.
   * @return true if other shares at least one day with this range.
   */
  public boolean overlaps(DateRange other) {
    return !this.startDate.isAfter(other.endDate) && !this.endDate.isBefore(other.startDate);
  }

  /**
   * Checks if this range overlaps any of the given ranges.
   *
   * @param ranges The ranges to check.
   * @return true if this range overlaps at least one range in ranges.
   */
  public boolean overlapsAny(List<DateRange> ranges) {
    for (DateRange r : ranges) {
      if (this.overlaps(r)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Finds the range, reached by repeatedly navigating {@link #prior()}/{@link #next()} from this
   * range, that contains the given date.
   *
   * @param date The date to locate.
   * @return The range containing date.
   */
  public DateRange rangeContainingDate(LocalDate date) {
    DateRange range = createNew(this.startDate, this.endDate);
    while (!range.containsDate(date)) {
      if (date.isAfter(range.endDate)) {
        range = range.next();
      } else {
        range = range.prior();
      }
    }
    return range;
  }

  /**
   * Gets the range immediately before this one.
   *
   * <p>If this range was created with custom navigation (e.g. via one of the {@code *DateRange}
   * factory classes), that navigation rule is used; otherwise the range is shifted back by its own
   * length in days.
   *
   * @return The prior DateRange.
   */
  public DateRange prior() {
    if (priorFn != null) {
      return priorFn.apply(this);
    }
    // default shift by length
    return createNew(startDate.minusDays(numberOfDays), endDate.minusDays(numberOfDays));
  }

  /**
   * Gets the range reached by navigating {@link #prior()} the given number of times.
   *
   * @param number The number of times to navigate to the prior range.
   * @return The resulting DateRange.
   */
  public DateRange priorN(int number) {
    DateRange r = this;
    for (int i = 0; i < number; i++) {
      r = r.prior();
    }
    return r;
  }

  /**
   * Gets the range immediately after this one.
   *
   * <p>If this range was created with custom navigation (e.g. via one of the {@code *DateRange}
   * factory classes), that navigation rule is used; otherwise the range is shifted forward by its
   * own length in days.
   *
   * @return The next DateRange.
   */
  public DateRange next() {
    if (nextFn != null) {
      return nextFn.apply(this);
    }
    // default shift by length
    return createNew(startDate.plusDays(numberOfDays), endDate.plusDays(numberOfDays));
  }

  /**
   * Gets the range reached by navigating {@link #next()} the given number of times.
   *
   * @param number The number of times to navigate to the next range.
   * @return The resulting DateRange.
   */
  public DateRange nextN(int number) {
    DateRange r = this;
    for (int i = 0; i < number; i++) {
      r = r.next();
    }
    return r;
  }

  /**
   * Gets the given number of ranges immediately before this one, not including this range.
   *
   * @param number The number of prior ranges to return.
   * @return A list of the prior ranges, in chronological order (earliest first).
   */
  public List<DateRange> rangesBefore(int number) {
    return rangesBeforeImpl(number, false);
  }

  /**
   * Gets the given number of ranges immediately before this one, including this range.
   *
   * @param number The number of prior ranges to return, not counting this range.
   * @return A list of the prior ranges followed by this range, in chronological order.
   */
  public List<DateRange> rangesBeforeInclusive(int number) {
    return rangesBeforeImpl(number, true);
  }

  /**
   * Gets the given number of ranges immediately after this one, not including this range.
   *
   * @param number The number of subsequent ranges to return.
   * @return A list of the subsequent ranges, in chronological order.
   */
  public List<DateRange> rangesAfter(int number) {
    return rangesAfterImpl(number, false);
  }

  /**
   * Gets the given number of ranges immediately after this one, including this range.
   *
   * @param number The number of subsequent ranges to return, not counting this range.
   * @return A list of this range followed by the subsequent ranges, in chronological order.
   */
  public List<DateRange> rangesAfterInclusive(int number) {
    return rangesAfterImpl(number, true);
  }

  /**
   * Gets a window of ranges centered on this range: the given number of ranges before this one,
   * this range itself, and the given number of ranges after this one.
   *
   * @param before The number of ranges to include before this one.
   * @param after The number of ranges to include after this one.
   * @return A list of the ranges in the window, in chronological order.
   */
  public List<DateRange> rangesWindow(int before, int after) {
    List<DateRange> res = new ArrayList<>(before + after + 1);
    res.addAll(rangesBeforeImpl(before, true)); // includes self
    List<DateRange> afters = rangesAfterImpl(after, false);
    res.addAll(afters);
    return res;
  }

  /**
   * Gets the consecutive ranges, reached by navigating {@link #prior()}/{@link #next()} from this
   * range, needed to fully cover the span from fromDate through toDate.
   *
   * @param fromDate The start of the span to cover.
   * @param toDate The end of the span to cover; must not be before fromDate.
   * @return A list of the covering ranges, in chronological order.
   */
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

  /**
   * Gets an iterator over every date in the range, from the start date through the end date,
   * inclusive.
   *
   * @return An iterator over the dates in the range.
   */
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
    DateRange that = (DateRange) o;
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
