package com.stano.daterange;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.stano.datetime.DateUtils.calculateDayOfWeekOffset;

/**
 * Static factory for building 14-day {@link DateRange}s that navigate {@link DateRange#prior()} and
 * {@link DateRange#next()} by shifting two full weeks at a time.
 *
 * <p>This class is not instantiable.
 */
public final class BiWeeklyDateRange {
  /**
   * Creates a 14-day range starting on the given date.
   *
   * @param startDate The start date of the range.
   * @return A new 14-day DateRange starting on startDate.
   */
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate end = startDate.plusDays(13);
    return DateRange.of(startDate, end);
  }

  /**
   * Creates a 14-day range ending on the given date.
   *
   * @param endDate The end date of the range.
   * @return A new 14-day DateRange ending on endDate.
   */
  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate start = endDate.minusDays(13);
    return DateRange.of(start, endDate);
  }

  /**
   * Creates a 14-day range ending on the next occurrence of endDay on or after target.
   *
   * @param target The date to start searching from.
   * @param endDay The day of the week the range should end on.
   * @return A new 14-day DateRange ending on the next occurrence of endDay on or after target.
   */
  public static DateRange withTargetDate(LocalDate target, DayOfWeek endDay) {
    long offset = calculateDayOfWeekOffset(target, endDay);
    LocalDate end = target.plusDays(offset);
    LocalDate start = end.minusDays(13);
    return DateRange.of(start, end);
  }

  private BiWeeklyDateRange() {}
}
