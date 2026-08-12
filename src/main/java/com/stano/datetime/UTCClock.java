package com.stano.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * A {@link Clock} that returns the actual current date and time, read in the UTC time zone.
 *
 * <p>This class is immutable and thread-safe.
 */
public class UTCClock implements Clock {
  private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

  @Override
  public LocalDate currentDate() {
    return LocalDate.now(UTC_ZONE_ID);
  }

  @Override
  public LocalDateTime currentDateTime() {
    return LocalDateTime.now(UTC_ZONE_ID);
  }

  @Override
  public LocalTime currentTime() {
    return LocalTime.now(UTC_ZONE_ID);
  }
}
