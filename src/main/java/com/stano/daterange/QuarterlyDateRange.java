package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.datetime.DateUtils.*;

/**
 * Static factory for building 3-calendar-month {@link DateRange}s that navigate {@link
 * DateRange#prior()} and {@link DateRange#next()} between consecutive quarters.
 *
 * <p>This class is not instantiable.
 */
public final class QuarterlyDateRange {
  /**
   * Creates a quarterly range covering the calendar month containing startDate and the following
   * two calendar months.
   *
   * @param startDate The date whose containing month begins the range.
   * @return A new quarterly DateRange starting on the 1st of startDate's month.
   */
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate start = firstDayOfMonth(startDate);
    LocalDate end = lastDayOfMonth(addMonths(firstDayOfMonth(startDate), 2));
    return DateRange.ofWithPriorNext(
        start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  /**
   * Creates a quarterly range covering the calendar month containing endDate and the two preceding
   * calendar months.
   *
   * @param endDate The date whose containing month ends the range.
   * @return A new quarterly DateRange ending on the last day of endDate's month.
   */
  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate start = subtractMonths(firstDayOfMonth(endDate), 2);
    LocalDate end = lastDayOfMonth(endDate);
    return DateRange.ofWithPriorNext(
        start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  static DateRange prior(DateRange dr) {
    LocalDate start = subtractMonths(dr.getStartDate(), 3);
    LocalDate end = lastDayOfMonth(subtractMonths(firstDayOfMonth(dr.getEndDate()), 3));
    return DateRange.ofWithPriorNext(
        start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  static DateRange next(DateRange dr) {
    LocalDate start = addMonths(dr.getStartDate(), 3);
    LocalDate end = lastDayOfMonth(addMonths(firstDayOfMonth(dr.getEndDate()), 3));
    return DateRange.ofWithPriorNext(
        start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  private QuarterlyDateRange() {}
}
