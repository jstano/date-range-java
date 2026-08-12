package com.stano.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Static facade for reading the current date and time, backed by a {@link Clock} (a {@link
 * UTCClock} by default).
 */
public final class DateTimeProvider {

  private static Clock clock = new UTCClock();

  /**
   * Gets the current date.
   *
   * @return The current date.
   */
  public static LocalDate currentDate() {

    return clock.currentDate();
  }

  /**
   * Gets the current date and time.
   *
   * @return The current date and time.
   */
  public static LocalDateTime currentDateTime() {

    return clock.currentDateTime();
  }

  /**
   * Gets the current time.
   *
   * @return The current time.
   */
  public static LocalTime currentTime() {

    return clock.currentTime();
  }

  private DateTimeProvider() {}
}
