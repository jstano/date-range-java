package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.datetime.DateUtils.lastDayOfMonth;

public final class SemiMonthlyDateRange {
  private static final int FIFTEENTH = 15;

  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate start = calculateStartDateFromEndDate(endDate);
    return DateRange.ofWithPriorNext(start, endDate, SemiMonthlyDateRange::prior, SemiMonthlyDateRange::next);
  }

  static DateRange prior(DateRange dr) {
    LocalDate end = dr.startDate().minusDays(1);
    LocalDate start;
    if (dr.startDate().getDayOfMonth() == 1) {
      // current is 1..15 -> prior is 16..last of previous month
      start = LocalDate.of(end.getYear(), end.getMonth(), FIFTEENTH + 1);
    }
    else {
      // current is 16..end -> prior is 1..15 of same month
      start = LocalDate.of(end.getYear(), end.getMonth(), 1);
    }
    return DateRange.ofWithPriorNext(start, end, SemiMonthlyDateRange::prior, SemiMonthlyDateRange::next);
  }

  static DateRange next(DateRange dr) {
    LocalDate start;
    if (dr.endDate().getDayOfMonth() == FIFTEENTH) {
      start = LocalDate.of(dr.endDate().getYear(), dr.endDate().getMonth(), FIFTEENTH + 1);
    }
    else {
      // next is 1..15 of next month
      LocalDate end = dr.endDate();
      int nextMonth = end.getMonthValue() % 12 + 1;
      int year = (nextMonth == 1) ? end.getYear() + 1 : end.getYear();
      start = LocalDate.of(year, nextMonth, 1);
    }
    LocalDate end = (start.getDayOfMonth() == 1)
                    ? LocalDate.of(start.getYear(), start.getMonth(), FIFTEENTH)
                    : lastDayOfMonth(start);
    return DateRange.ofWithPriorNext(start, end, SemiMonthlyDateRange::prior, SemiMonthlyDateRange::next);
  }

  private static LocalDate calculateStartDateFromEndDate(LocalDate endDate) {
    if (endDate.getDayOfMonth() == FIFTEENTH) {
      return LocalDate.of(endDate.getYear(), endDate.getMonth(), 1);
    }
    else {
      return LocalDate.of(endDate.getYear(), endDate.getMonth(), FIFTEENTH + 1);
    }
  }

  private SemiMonthlyDateRange() {
  }
}
