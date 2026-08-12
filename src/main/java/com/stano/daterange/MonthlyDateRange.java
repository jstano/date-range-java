package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.datetime.DateUtils.*;

/**
 * Static factory for building calendar-month-aligned {@link DateRange}s with a configurable start
 * day, that navigate {@link DateRange#prior()} and {@link DateRange#next()} between consecutive
 * months.
 *
 * <p>This class is not instantiable.
 */
public final class MonthlyDateRange {
  /**
   * Creates a calendar-month range ending on the given date, starting on the 1st of that month.
   *
   * @param endDate The end date of the range; must be the last day of its month.
   * @return A new monthly DateRange starting on the 1st and ending on endDate.
   */
  public static DateRange withEndDateOnFirst(LocalDate endDate) {
    return withEndDateAndStartDay(endDate, 1);
  }

  /**
   * Creates a monthly range ending on the given date, starting on the given day of the month.
   *
   * @param endDate The end date of the range.
   * @param startDay The day of the month the range starts on.
   * @return A new monthly DateRange starting on startDay and ending on endDate.
   */
  public static DateRange withEndDateAndStartDay(LocalDate endDate, int startDay) {
    LocalDate startDate = calculateStartDateFromEndDate(endDate, startDay);
    return DateRange.ofWithPriorNextStartDay(
        startDate, endDate, MonthlyDateRange::prior, MonthlyDateRange::next, startDay);
  }

  static DateRange prior(DateRange dr) {
    int startDay = dr.startDay().orElse(1);
    if (startDay == 1) {
      LocalDate newEnd = dr.getStartDate().minusDays(1);
      LocalDate newStart = newEnd.withDayOfMonth(1);
      return DateRange.ofWithPriorNextStartDay(
          newStart, newEnd, MonthlyDateRange::prior, MonthlyDateRange::next, startDay);
    } else {
      LocalDate newStart = subtractMonths(dr.getStartDate(), 1);
      LocalDate newEnd = dr.getStartDate().minusDays(1);
      return DateRange.ofWithPriorNextStartDay(
          newStart, newEnd, MonthlyDateRange::prior, MonthlyDateRange::next, startDay);
    }
  }

  static DateRange next(DateRange dr) {
    int startDay = dr.startDay().orElse(1);
    if (startDay == 1) {
      LocalDate newStart = dr.getEndDate().plusDays(1);
      LocalDate newEnd = lastDayOfMonth(newStart);
      return DateRange.ofWithPriorNextStartDay(
          newStart, newEnd, MonthlyDateRange::prior, MonthlyDateRange::next, startDay);
    } else {
      LocalDate newStart = dr.getEndDate().plusDays(1);
      LocalDate newEnd = addMonths(dr.getEndDate(), 1);
      return DateRange.ofWithPriorNextStartDay(
          newStart, newEnd, MonthlyDateRange::prior, MonthlyDateRange::next, startDay);
    }
  }

  private static LocalDate calculateStartDateFromEndDate(LocalDate endDate, int startDay) {
    if (startDay == 1) {
      return LocalDate.of(endDate.getYear(), endDate.getMonth(), 1);
    } else {
      // day after endDate, minus one month. In java time: endDate.plusDays(1).minusMonths(1)
      return endDate.plusDays(1).minusMonths(1);
    }
  }

  private MonthlyDateRange() {}
}
