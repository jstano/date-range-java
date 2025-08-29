package com.stano.datetimerange;

import com.stano.datetime.DTUtil;
import com.stano.datetime.DateTimeConstants;
import com.stano.timerange.TimeRange;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DateTimeRange implements Comparable<DateTimeRange>, Serializable {
  private final LocalDateTime startDateTime;
  private final LocalDateTime endDateTime;

  public static DateTimeRange of(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return new DateTimeRange(startDateTime, endDateTime);
  }

  public static DateTimeRange fromTimeRangeOnDate(TimeRange timeRange, LocalDate date) {
    LocalTime startTime = timeRange.getStartTime();
    LocalTime endTime = timeRange.getEndTime();

    if (endTime.isBefore(startTime)) {
      return new DateTimeRange(date.atTime(startTime),
                               date.plusDays(1).atTime(endTime));
    }

    return new DateTimeRange(date.atTime(startTime),
                             date.atTime(endTime));
  }

  public static DateTimeRange allDay(LocalDate date) {
    return new DateTimeRange(date.atTime(LocalTime.MIDNIGHT),
                             date.atTime(LocalTime.MIDNIGHT).plusDays(1));
  }

  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  public Duration getDuration() {
    return Duration.between(startDateTime, endDateTime);
  }

  public boolean overlaps(DateTimeRange dateTimeRange) {
    if (dateTimeRange == null) {
      return false;
    }

    return startDateTime.compareTo(dateTimeRange.getEndDateTime()) <= 0 && endDateTime.compareTo(dateTimeRange.getStartDateTime()) >= 0;
  }

  public boolean overlapsExclusive(DateTimeRange dateTimeRange) {
    if (dateTimeRange == null) {
      return false;
    }

    return startDateTime.isBefore(dateTimeRange.getEndDateTime()) && endDateTime.isAfter(dateTimeRange.getStartDateTime());
  }

  public boolean overlapsCompletely(DateTimeRange dateTimeRange) {
    return dateTimeRange.getStartDateTime().compareTo(startDateTime) >= 0 && dateTimeRange.getEndDateTime().compareTo(endDateTime) <= 0;
  }

  public Duration overlapDuration(DateTimeRange otherRange) {
    DateTimeRange overlappingRange = overlapRange(otherRange);

    return overlappingRange == null ? Duration.ofMillis(0) : overlappingRange.getDuration();
  }

  public DateTimeRange overlapRange(DateTimeRange otherRange) {
    if (!overlaps(otherRange)) {
      return null;
    }

    LocalDateTime startOverlapDateTime = DTUtil.latest(startDateTime, otherRange.getStartDateTime());
    LocalDateTime endOverlapDateTime = DTUtil.earliest(endDateTime, otherRange.getEndDateTime());

    return DateTimeRange.of(startOverlapDateTime, endOverlapDateTime);
  }

  public boolean containsDateTime(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.compareTo(startDateTime) >= 0 && dateTime.compareTo(endDateTime) <= 0;
    }

    return false;
  }

  public boolean containsDateTimeExclusive(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.compareTo(startDateTime) > 0 && dateTime.compareTo(endDateTime) < 0;
    }

    return false;
  }

  public boolean containsDateTimeExclusiveOfEndDateTime(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.compareTo(startDateTime) >= 0 && dateTime.compareTo(endDateTime) < 0;
    }

    return false;
  }

  public double getFractionalHours() {
    return (double)getDuration().getSeconds() / (double)DateTimeConstants.SECONDS_PER_HOUR;
  }

  @Override
  public int compareTo(DateTimeRange dateTimeRange) {
    if (this == dateTimeRange) {
      return 0;
    }

    if (dateTimeRange == null) {
      return -1;
    }

    int result = startDateTime.compareTo(dateTimeRange.getStartDateTime());

    if (result == 0) {
      result = endDateTime.compareTo(dateTimeRange.getEndDateTime());
    }

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof DateTimeRange that)) {
      return false;
    }

    if (!endDateTime.equals(that.getEndDateTime())) {
      return false;
    }

    return startDateTime.equals(that.getStartDateTime());
  }

  @Override
  public int hashCode() {
    int result = startDateTime.hashCode();
    result = 31 * result + endDateTime.hashCode();
    return result;
  }

  private DateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
  }
}
