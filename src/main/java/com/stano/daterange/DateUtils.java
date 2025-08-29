package com.stano.daterange;

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
}
