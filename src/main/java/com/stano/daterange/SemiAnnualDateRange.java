package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.datetime.DateUtils.addMonths;
import static com.stano.datetime.DateUtils.subtractMonths;

/**
 * Static factory for building 6-calendar-month {@link DateRange}s that navigate {@link
 * DateRange#prior()} and {@link DateRange#next()} between consecutive half-year periods.
 *
 * <p>This class is not instantiable.
 */
public final class SemiAnnualDateRange {
  /**
   * Creates a 6-month range starting on the given date.
   *
   * @param startDate The start date of the range.
   * @return A new 6-month DateRange starting on startDate.
   */
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate endDate = addMonths(startDate, 6).minusDays(1);
    return DateRange.ofWithPriorNext(
        startDate, endDate, SemiAnnualDateRange::prior, SemiAnnualDateRange::next);
  }

  /**
   * Creates a 6-month range ending on the given date.
   *
   * @param endDate The end date of the range.
   * @return A new 6-month DateRange ending on endDate.
   */
  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate startDate = subtractMonths(endDate, 6).plusDays(1);
    return DateRange.ofWithPriorNext(
        startDate, endDate, SemiAnnualDateRange::prior, SemiAnnualDateRange::next);
  }

  static DateRange prior(DateRange dr) {
    LocalDate start = subtractMonths(dr.getStartDate(), 6);
    LocalDate end = subtractMonths(dr.getEndDate(), 6);
    return DateRange.ofWithPriorNext(
        start, end, SemiAnnualDateRange::prior, SemiAnnualDateRange::next);
  }

  static DateRange next(DateRange dr) {
    LocalDate start = addMonths(dr.getStartDate(), 6);
    LocalDate end = addMonths(dr.getEndDate(), 6);
    return DateRange.ofWithPriorNext(
        start, end, SemiAnnualDateRange::prior, SemiAnnualDateRange::next);
  }

  private SemiAnnualDateRange() {}
}
