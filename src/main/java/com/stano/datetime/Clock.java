package com.stano.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Provides access to the current date and time, allowing callers to depend on an abstraction
 * instead of directly reading the system clock.
 *
 * <p>Implementations may return the real current time (e.g. {@link UTCClock}) or a fixed value for
 * testing (e.g. {@link ConstantClock}).
 */
public interface Clock {

  /**
   * Gets the current date.
   *
   * @return The current date.
   */
  LocalDate currentDate();

  /**
   * Gets the current date and time.
   *
   * @return The current date and time.
   */
  LocalDateTime currentDateTime();

  /**
   * Gets the current time.
   *
   * @return The current time.
   */
  LocalTime currentTime();
}
