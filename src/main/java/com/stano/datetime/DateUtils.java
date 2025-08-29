package com.stano.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

public final class DateUtils {
  private DateUtils() {
  }

  public static LocalDate firstDayOfMonth(LocalDate d) {
    return d.withDayOfMonth(1);
  }

  public static LocalDate lastDayOfMonth(LocalDate d) {
    YearMonth ym = YearMonth.from(d);
    return ym.atEndOfMonth();
  }

  public static LocalDate addMonths(LocalDate d, int months) {
    return d.plusMonths(months);
  }

  public static LocalDate subtractMonths(LocalDate d, int months) {
    return d.minusMonths(months);
  }

  public static LocalDate addYears(LocalDate d, int years) {
    return d.plusYears(years);
  }

  public static LocalDate subtractYears(LocalDate d, int years) {
    return d.minusYears(years);
  }

  public static long calculateDayOfWeekOffset(LocalDate date, DayOfWeek endDay) {
    int offset = endDay.getValue() - date.getDayOfWeek().getValue(); // Mon=1..Sun=7
    if (offset < 0) {
      offset += 7;
    }
    return offset;
  }
}
