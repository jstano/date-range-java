package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.datetime.DateUtils.addYears;
import static com.stano.datetime.DateUtils.subtractYears;

/**
 * Static factory for building 1-year {@link DateRange}s that navigate {@link DateRange#prior()} and
 * {@link DateRange#next()} between consecutive years.
 *
 * <p>A range starting on February 29th ends on February 28th of the following year, since the
 * following year is not guaranteed to be a leap year.
 *
 * <p>This class is not instantiable.
 */
public final class AnnualDateRange {
  /**
   * Creates a 1-year range starting on the given date.
   *
   * @param startDate The start date of the range.
   * @return A new 1-year DateRange starting on startDate.
   */
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate endDate = endForStart(startDate);
    return DateRange.ofWithPriorNext(
        startDate, endDate, AnnualDateRange::prior, AnnualDateRange::next);
  }

  /**
   * Creates a 1-year range ending on the given date.
   *
   * @param endDate The end date of the range.
   * @return A new 1-year DateRange ending on endDate.
   */
  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate startDate = subtractYears(endDate, 1).plusDays(1);
    return DateRange.ofWithPriorNext(
        startDate, endDate, AnnualDateRange::prior, AnnualDateRange::next);
  }

  static DateRange prior(DateRange dr) {
    LocalDate start = subtractYears(dr.getStartDate(), 1);
    LocalDate end = endForStart(start);
    return DateRange.ofWithPriorNext(start, end, AnnualDateRange::prior, AnnualDateRange::next);
  }

  static DateRange next(DateRange dr) {
    LocalDate start = addYears(dr.getStartDate(), 1);
    LocalDate end = endForStart(start);
    return DateRange.ofWithPriorNext(start, end, AnnualDateRange::prior, AnnualDateRange::next);
  }

  private static LocalDate endForStart(LocalDate startDate) {
    // Special-case Feb 29 starts: end at Feb 28 next year
    if (startDate.getMonthValue() == 2 && startDate.getDayOfMonth() == 29) {
      return LocalDate.of(startDate.getYear() + 1, 2, 28);
    } else {
      return addYears(startDate, 1).minusDays(1);
    }
  }

  private AnnualDateRange() {}
}
