package com.stano.datetimerange;

import com.stano.datetime.DTUtil;
import com.stano.datetime.DateTimeConstants;
import com.stano.timerange.TimeRange;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * An inclusive range of date-times, from a start date-time through an end date-time.
 *
 * <p>This class is immutable and thread-safe.
 */
public final class DateTimeRange implements Comparable<DateTimeRange>, Serializable {
  private final LocalDateTime startDateTime;
  private final LocalDateTime endDateTime;

  /**
   * Creates a new DateTimeRange spanning the given start and end date-times.
   *
   * @param startDateTime The start date-time of the range.
   * @param endDateTime The end date-time of the range.
   * @return A new DateTimeRange with the given start and end date-times.
   */
  public static DateTimeRange of(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return new DateTimeRange(startDateTime, endDateTime);
  }

  /**
   * Creates a new DateTimeRange by applying a TimeRange's start and end times to the given date. If
   * the TimeRange's end time is before its start time, the range is treated as crossing midnight
   * and the end date-time falls on the day after date.
   *
   * @param timeRange The TimeRange whose start/end times are applied to date.
   * @param date The date the range starts on.
   * @return A new DateTimeRange combining timeRange's times with date.
   */
  public static DateTimeRange fromTimeRangeOnDate(TimeRange timeRange, LocalDate date) {
    LocalTime startTime = timeRange.getStartTime();
    LocalTime endTime = timeRange.getEndTime();

    if (endTime.isBefore(startTime)) {
      return new DateTimeRange(date.atTime(startTime), date.plusDays(1).atTime(endTime));
    }

    return new DateTimeRange(date.atTime(startTime), date.atTime(endTime));
  }

  /**
   * Creates a new DateTimeRange spanning the entirety of the given date, from midnight through
   * midnight the following day.
   *
   * @param date The date to span.
   * @return A new DateTimeRange covering all of date.
   */
  public static DateTimeRange allDay(LocalDate date) {
    return new DateTimeRange(
        date.atTime(LocalTime.MIDNIGHT), date.atTime(LocalTime.MIDNIGHT).plusDays(1));
  }

  /**
   * Gets the start date-time of the range.
   *
   * @return The start date-time.
   */
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  /**
   * Gets the end date-time of the range.
   *
   * @return The end date-time.
   */
  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  /**
   * Gets the duration of the range.
   *
   * @return The Duration between the start and end date-times.
   */
  public Duration getDuration() {
    return Duration.between(startDateTime, endDateTime);
  }

  /**
   * Checks if another range overlaps this range, inclusive of shared boundary date-times.
   *
   * @param dateTimeRange The DateTimeRange to check.
   * @return true if dateTimeRange shares at least one instant with this range.
   */
  public boolean overlaps(DateTimeRange dateTimeRange) {
    if (dateTimeRange == null) {
      return false;
    }

    return startDateTime.compareTo(dateTimeRange.getEndDateTime()) <= 0
        && endDateTime.compareTo(dateTimeRange.getStartDateTime()) >= 0;
  }

  /**
   * Checks if another range overlaps this range, excluding ranges that only share a boundary
   * date-time.
   *
   * @param dateTimeRange The DateTimeRange to check.
   * @return true if dateTimeRange overlaps this range by more than just a shared endpoint.
   */
  public boolean overlapsExclusive(DateTimeRange dateTimeRange) {
    if (dateTimeRange == null) {
      return false;
    }

    return startDateTime.isBefore(dateTimeRange.getEndDateTime())
        && endDateTime.isAfter(dateTimeRange.getStartDateTime());
  }

  /**
   * Checks if another range is fully contained within this range.
   *
   * @param dateTimeRange The DateTimeRange to check.
   * @return true if dateTimeRange's start and end date-times both fall within this range.
   */
  public boolean overlapsCompletely(DateTimeRange dateTimeRange) {
    return dateTimeRange.getStartDateTime().compareTo(startDateTime) >= 0
        && dateTimeRange.getEndDateTime().compareTo(endDateTime) <= 0;
  }

  /**
   * Calculates the duration for which another range overlaps this range.
   *
   * @param otherRange The DateTimeRange to check.
   * @return The duration of the overlap, or a zero duration if the ranges don't overlap or
   *     otherRange is null.
   */
  public Duration overlapDuration(DateTimeRange otherRange) {
    DateTimeRange overlappingRange = overlapRange(otherRange);

    return overlappingRange == null ? Duration.ofMillis(0) : overlappingRange.getDuration();
  }

  /**
   * Calculates the range representing the overlap between this range and another range.
   *
   * @param otherRange The DateTimeRange to check.
   * @return A new DateTimeRange spanning the overlap, or null if the ranges don't overlap.
   */
  public DateTimeRange overlapRange(DateTimeRange otherRange) {
    if (!overlaps(otherRange)) {
      return null;
    }

    LocalDateTime startOverlapDateTime =
        DTUtil.latest(startDateTime, otherRange.getStartDateTime());
    LocalDateTime endOverlapDateTime = DTUtil.earliest(endDateTime, otherRange.getEndDateTime());

    return DateTimeRange.of(startOverlapDateTime, endOverlapDateTime);
  }

  /**
   * Checks if a date-time falls within this range, inclusive of the start and end date-times.
   *
   * @param dateTime The date-time to check.
   * @return true if dateTime is on or after the start date-time and on or before the end date-time;
   *     false if dateTime is null.
   */
  public boolean containsDateTime(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.compareTo(startDateTime) >= 0 && dateTime.compareTo(endDateTime) <= 0;
    }

    return false;
  }

  /**
   * Checks if a date-time falls within this range, excluding both the start and end date-times.
   *
   * @param dateTime The date-time to check.
   * @return true if dateTime is strictly after the start date-time and strictly before the end
   *     date-time; false if dateTime is null.
   */
  public boolean containsDateTimeExclusive(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.compareTo(startDateTime) > 0 && dateTime.compareTo(endDateTime) < 0;
    }

    return false;
  }

  /**
   * Checks if a date-time falls within this range, including the start date-time but excluding the
   * end date-time.
   *
   * @param dateTime The date-time to check.
   * @return true if dateTime is on or after the start date-time and strictly before the end
   *     date-time; false if dateTime is null.
   */
  public boolean containsDateTimeExclusiveOfEndDateTime(LocalDateTime dateTime) {
    if (dateTime != null) {
      return dateTime.compareTo(startDateTime) >= 0 && dateTime.compareTo(endDateTime) < 0;
    }

    return false;
  }

  /**
   * Gets the duration of the range expressed as fractional hours.
   *
   * @return The duration of the range, in hours.
   */
  public double getFractionalHours() {
    return (double) getDuration().getSeconds() / (double) DateTimeConstants.SECONDS_PER_HOUR;
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
