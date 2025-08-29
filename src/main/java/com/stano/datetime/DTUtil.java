package com.stano.datetime;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.LocalDateTime;

public final class DTUtil {
  public static LocalDateTime earliest(LocalDateTime time1, LocalDateTime time2) {
    if (time1 == null || time2 == null) {
      return (time1 != null) ? time1 : time2;
    }

    return !time1.isAfter(time2) ? time1 : time2;
  }

  public static LocalDateTime latest(LocalDateTime time1, LocalDateTime time2) {
    if (time1 == null || time2 == null) {
      return (time1 != null) ? time1 : time2;
    }

    return !time1.isBefore(time2) ? time1 : time2;
  }

  public static int durationInHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return (int)Duration.between(startDateTime, endDateTime).getSeconds() / DateTimeConstants.SECONDS_PER_HOUR;
  }

  public static int durationInMinutes(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return (int)Duration.between(startDateTime, endDateTime).getSeconds() / DateTimeConstants.SECONDS_PER_MINUTE;
  }

  public static int durationInSeconds(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return (int)Duration.between(startDateTime, endDateTime).getSeconds();
  }

  public static BigDecimal durationInFractionalSeconds(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return BigDecimal.valueOf((double)Duration.between(startDateTime, endDateTime).toMillis() / (double)DateTimeConstants.MILLIS_PER_SECOND)
                     .round(new MathContext(4));
  }

  public static double durationInFractionalHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return ((double)Duration.between(startDateTime, endDateTime).getSeconds()) / DateTimeConstants.SECONDS_PER_HOUR;
  }

  private DTUtil() {
  }
}
