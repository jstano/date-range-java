package com.stano.daterange;

import java.time.DayOfWeek;
import java.time.LocalDate;

public final class BiWeeklyDateRange {
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate end = startDate.plusDays(13);
    return DateRange.of(startDate, end);
  }

  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate start = endDate.minusDays(13);
    return DateRange.of(start, endDate);
  }

  public static DateRange withTargetDate(LocalDate target, DayOfWeek endDay) {
    long offset = calculateDayOfWeekOffset(target, endDay);
    LocalDate end = target.plusDays(offset);
    LocalDate start = end.minusDays(13);
    return DateRange.of(start, end);
  }

  private static long calculateDayOfWeekOffset(LocalDate date, DayOfWeek endDay) {
    int offset = endDay.getValue() - date.getDayOfWeek().getValue();
    if (offset < 0) {
      offset += 7;
    }
    return offset;
  }

  private BiWeeklyDateRange() {
  }
}
