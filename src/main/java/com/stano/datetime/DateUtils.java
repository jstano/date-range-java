package com.stano.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Date arithmetic helpers used to compute calendar-month and day-of-week boundaries.
 *
 * <p>This class is not instantiable.
 */
public final class DateUtils {
  private DateUtils() {}

  /**
   * Gets the first day of the month containing the given date.
   *
   * @param d The date whose containing month is used.
   * @return The date of the first day of that month.
   */
  public static LocalDate firstDayOfMonth(LocalDate d) {
    return d.withDayOfMonth(1);
  }

  /**
   * Gets the last day of the month containing the given date.
   *
   * @param d The date whose containing month is used.
   * @return The date of the last day of that month.
   */
  public static LocalDate lastDayOfMonth(LocalDate d) {
    YearMonth ym = YearMonth.from(d);
    return ym.atEndOfMonth();
  }

  /**
   * Adds the given number of months to a date.
   *
   * @param d The date to add months to.
   * @param months The number of months to add.
   * @return A new date that is the given number of months after d.
   */
  public static LocalDate addMonths(LocalDate d, int months) {
    return d.plusMonths(months);
  }

  /**
   * Subtracts the given number of months from a date.
   *
   * @param d The date to subtract months from.
   * @param months The number of months to subtract.
   * @return A new date that is the given number of months before d.
   */
  public static LocalDate subtractMonths(LocalDate d, int months) {
    return d.minusMonths(months);
  }

  /**
   * Adds the given number of years to a date.
   *
   * @param d The date to add years to.
   * @param years The number of years to add.
   * @return A new date that is the given number of years after d.
   */
  public static LocalDate addYears(LocalDate d, int years) {
    return d.plusYears(years);
  }

  /**
   * Subtracts the given number of years from a date.
   *
   * @param d The date to subtract years from.
   * @param years The number of years to subtract.
   * @return A new date that is the given number of years before d.
   */
  public static LocalDate subtractYears(LocalDate d, int years) {
    return d.minusYears(years);
  }

  /**
   * Calculates the number of days from a date forward to the next occurrence of a given day of the
   * week, in the range 0-6.
   *
   * @param date The starting date.
   * @param endDay The day of the week to offset to.
   * @return The number of days between date and the next occurrence of endDay.
   */
  public static long calculateDayOfWeekOffset(LocalDate date, DayOfWeek endDay) {
    int offset = endDay.getValue() - date.getDayOfWeek().getValue(); // Mon=1..Sun=7
    if (offset < 0) {
      offset += 7;
    }
    return offset;
  }
}
