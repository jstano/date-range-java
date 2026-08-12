package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.datetime.DateUtils.lastDayOfMonth;

/**
 * Static factory for building semi-monthly {@link DateRange}s (the 1st through the 15th of a month,
 * or the 16th through the last day of a month) that navigate {@link DateRange#prior()} and {@link
 * DateRange#next()} between consecutive semi-monthly periods.
 *
 * <p>This class is not instantiable.
 */
public final class SemiMonthlyDateRange {
  private static final int FIFTEENTH = 15;

  /**
   * Creates a semi-monthly range ending on the given date. If endDate is the 15th of the month, the
   * range spans the 1st through the 15th; otherwise it spans the 16th through the last day of the
   * month.
   *
   * @param endDate The end date of the range; must be either the 15th or the last day of its month.
   * @return A new semi-monthly DateRange ending on endDate.
   */
  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate start = calculateStartDateFromEndDate(endDate);
    return DateRange.ofWithPriorNext(
        start, endDate, SemiMonthlyDateRange::prior, SemiMonthlyDateRange::next);
  }

  static DateRange prior(DateRange dr) {
    LocalDate end = dr.getStartDate().minusDays(1);
    LocalDate start;
    if (dr.getStartDate().getDayOfMonth() == 1) {
      // current is 1..15 -> prior is 16..last of previous month
      start = LocalDate.of(end.getYear(), end.getMonth(), FIFTEENTH + 1);
    } else {
      // current is 16..end -> prior is 1..15 of same month
      start = LocalDate.of(end.getYear(), end.getMonth(), 1);
    }
    return DateRange.ofWithPriorNext(
        start, end, SemiMonthlyDateRange::prior, SemiMonthlyDateRange::next);
  }

  static DateRange next(DateRange dr) {
    LocalDate start;
    if (dr.getEndDate().getDayOfMonth() == FIFTEENTH) {
      start = LocalDate.of(dr.getEndDate().getYear(), dr.getEndDate().getMonth(), FIFTEENTH + 1);
    } else {
      // next is 1..15 of next month
      LocalDate end = dr.getEndDate();
      int nextMonth = end.getMonthValue() % 12 + 1;
      int year = (nextMonth == 1) ? end.getYear() + 1 : end.getYear();
      start = LocalDate.of(year, nextMonth, 1);
    }
    LocalDate end =
        (start.getDayOfMonth() == 1)
            ? LocalDate.of(start.getYear(), start.getMonth(), FIFTEENTH)
            : lastDayOfMonth(start);
    return DateRange.ofWithPriorNext(
        start, end, SemiMonthlyDateRange::prior, SemiMonthlyDateRange::next);
  }

  private static LocalDate calculateStartDateFromEndDate(LocalDate endDate) {
    if (endDate.getDayOfMonth() == FIFTEENTH) {
      return LocalDate.of(endDate.getYear(), endDate.getMonth(), 1);
    } else {
      return LocalDate.of(endDate.getYear(), endDate.getMonth(), FIFTEENTH + 1);
    }
  }

  private SemiMonthlyDateRange() {}
}
