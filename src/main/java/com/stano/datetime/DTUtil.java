package com.stano.datetime;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Helpers for comparing {@link LocalDateTime} values and computing the duration between them.
 *
 * <p>This class is not instantiable.
 */
public final class DTUtil {
  /**
   * Gets the earlier of two date-times. A null argument is treated as later than any non-null
   * value.
   *
   * @param time1 The first date-time to compare.
   * @param time2 The second date-time to compare.
   * @return The earlier of time1 and time2, or the non-null one if either is null.
   */
  public static LocalDateTime earliest(LocalDateTime time1, LocalDateTime time2) {
    if (time1 == null || time2 == null) {
      return (time1 != null) ? time1 : time2;
    }

    return !time1.isAfter(time2) ? time1 : time2;
  }

  /**
   * Gets the later of two date-times. A null argument is treated as earlier than any non-null
   * value.
   *
   * @param time1 The first date-time to compare.
   * @param time2 The second date-time to compare.
   * @return The later of time1 and time2, or the non-null one if either is null.
   */
  public static LocalDateTime latest(LocalDateTime time1, LocalDateTime time2) {
    if (time1 == null || time2 == null) {
      return (time1 != null) ? time1 : time2;
    }

    return !time1.isBefore(time2) ? time1 : time2;
  }

  /**
   * Calculates the whole number of hours between two date-times.
   *
   * @param startDateTime The start date-time.
   * @param endDateTime The end date-time.
   * @return The duration between startDateTime and endDateTime, truncated to whole hours.
   */
  public static int durationInHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return (int) Duration.between(startDateTime, endDateTime).getSeconds()
        / DateTimeConstants.SECONDS_PER_HOUR;
  }

  /**
   * Calculates the whole number of minutes between two date-times.
   *
   * @param startDateTime The start date-time.
   * @param endDateTime The end date-time.
   * @return The duration between startDateTime and endDateTime, truncated to whole minutes.
   */
  public static int durationInMinutes(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return (int) Duration.between(startDateTime, endDateTime).getSeconds()
        / DateTimeConstants.SECONDS_PER_MINUTE;
  }

  /**
   * Calculates the whole number of seconds between two date-times.
   *
   * @param startDateTime The start date-time.
   * @param endDateTime The end date-time.
   * @return The duration between startDateTime and endDateTime, in seconds.
   */
  public static int durationInSeconds(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return (int) Duration.between(startDateTime, endDateTime).getSeconds();
  }

  /**
   * Calculates the duration between two date-times, in seconds, rounded to 4 significant digits.
   *
   * @param startDateTime The start date-time.
   * @param endDateTime The end date-time.
   * @return The duration between startDateTime and endDateTime, in fractional seconds.
   */
  public static BigDecimal durationInFractionalSeconds(
      LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return BigDecimal.valueOf(
            (double) Duration.between(startDateTime, endDateTime).toMillis()
                / (double) DateTimeConstants.MILLIS_PER_SECOND)
        .round(new MathContext(4));
  }

  /**
   * Calculates the duration between two date-times, in fractional hours.
   *
   * @param startDateTime The start date-time.
   * @param endDateTime The end date-time.
   * @return The duration between startDateTime and endDateTime, in fractional hours.
   */
  public static double durationInFractionalHours(
      LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return ((double) Duration.between(startDateTime, endDateTime).getSeconds())
        / DateTimeConstants.SECONDS_PER_HOUR;
  }

  private DTUtil() {}
}
