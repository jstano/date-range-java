package com.stano.daterange;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.stano.datetime.DateUtils.calculateDayOfWeekOffset;

public final class WeeklyDateRange {
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate end = startDate.plusDays(6);
    return DateRange.of(startDate, end);
  }

  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate start = endDate.minusDays(6);
    return DateRange.of(start, endDate);
  }

  public static DateRange withTargetDate(LocalDate target, DayOfWeek endDay) {
    long offset = calculateDayOfWeekOffset(target, endDay);
    LocalDate end = target.plusDays(offset);
    LocalDate start = end.minusDays(6);
    return DateRange.of(start, end);
  }

  private WeeklyDateRange() {
  }
}
