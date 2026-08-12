package com.stano.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A {@link Clock} that always returns a fixed date and time.
 *
 * <p>Useful for tests that need deterministic, repeatable "current time" values. This class is
 * immutable and thread-safe.
 */
public class ConstantClock implements Clock {

  private final LocalDateTime localDateTime;

  /**
   * Creates a new Clock that always returns the given date and time.
   *
   * @param localDateTime The fixed date and time this clock will always return.
   * @return A new Clock fixed at the given date and time.
   */
  public static Clock of(LocalDateTime localDateTime) {

    return new ConstantClock(localDateTime);
  }

  @Override
  public LocalDate currentDate() {

    return localDateTime.toLocalDate();
  }

  @Override
  public LocalDateTime currentDateTime() {

    return localDateTime;
  }

  @Override
  public LocalTime currentTime() {

    return localDateTime.toLocalTime();
  }

  private ConstantClock(LocalDateTime localDateTime) {

    this.localDateTime = localDateTime;
  }
}
