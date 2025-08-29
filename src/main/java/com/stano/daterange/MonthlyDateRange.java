package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.daterange.DateUtils.*;

public final class MonthlyDateRange {
  public static DateRange withEndDateOnFirst(LocalDate endDate) {
    return withEndDateAndStartDay(endDate, 1);
  }

  public static DateRange withEndDateAndStartDay(LocalDate endDate, int startDay) {
    LocalDate startDate = calculateStartDateFromEndDate(endDate, startDay);
    return DateRange.ofWithPriorNextStartDay(
      startDate,
      endDate,
      MonthlyDateRange::prior,
      MonthlyDateRange::next,
      startDay
    );
  }

  static DateRange prior(DateRange dr) {
    int startDay = dr.startDay().orElse(1);
    if (startDay == 1) {
      LocalDate newEnd = dr.startDate().minusDays(1);
      LocalDate newStart = newEnd.withDayOfMonth(1);
      return DateRange.ofWithPriorNextStartDay(newStart, newEnd, MonthlyDateRange::prior, MonthlyDateRange::next, startDay);
    }
    else {
      LocalDate newStart = subtractMonths(dr.startDate(), 1);
      LocalDate newEnd = dr.startDate().minusDays(1);
      return DateRange.ofWithPriorNextStartDay(newStart, newEnd, MonthlyDateRange::prior, MonthlyDateRange::next, startDay);
    }
  }

  static DateRange next(DateRange dr) {
    int startDay = dr.startDay().orElse(1);
    if (startDay == 1) {
      LocalDate newStart = dr.endDate().plusDays(1);
      LocalDate newEnd = lastDayOfMonth(newStart);
      return DateRange.ofWithPriorNextStartDay(newStart, newEnd, MonthlyDateRange::prior, MonthlyDateRange::next, startDay);
    }
    else {
      LocalDate newStart = dr.endDate().plusDays(1);
      LocalDate newEnd = addMonths(dr.endDate(), 1);
      return DateRange.ofWithPriorNextStartDay(newStart, newEnd, MonthlyDateRange::prior, MonthlyDateRange::next, startDay);
    }
  }

  private static LocalDate calculateStartDateFromEndDate(LocalDate endDate, int startDay) {
    if (startDay == 1) {
      return LocalDate.of(endDate.getYear(), endDate.getMonth(), 1);
    }
    else {
      // day after endDate, minus one month. In java time: endDate.plusDays(1).minusMonths(1)
      return endDate.plusDays(1).minusMonths(1);
    }
  }

  private MonthlyDateRange() {
  }
}
