package com.stano.timerange;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

/**
 * This class represents a range of times.
 */
public final class TimeRange implements Comparable<TimeRange>, Serializable {
  private final LocalTime startTime;
  private final LocalTime endTime;

  public static TimeRange of(LocalTime startTime, LocalTime endTime) {
    return new TimeRange(startTime, endTime);
  }

  /**
   * Get the starting time in the range.
   *
   * @return The start time of the range.
   */
  public LocalTime getStartTime() {
    return startTime;
  }

  /**
   * Get the ending time in the range.
   *
   * @return The end time of the range.
   */
  public LocalTime getEndTime() {
    return endTime;
  }

  /**
   * Get the duration of the time range.
   *
   * @return A Duration object that represents the duration of the range.
   */
  public Duration getDuration() {
    return Duration.between(startTime, endTime);
  }

  /**
   * Checks if a time range overlaps this time range.
   *
   * @param timeRange The TimeRange to check.
   * @return true if the timeRange overlaps this timeRange.
   */
  public boolean overlaps(TimeRange timeRange) {
    if (timeRange == null) {
      return false;
    }
    // If either range ends in Midnight then the checks need to be handled as a special case as Midnight evaluates to 0:00:00
    //   which prevents the isOnOrBefore() and isOnOrAfter() methods in the 3rd Party Joda package from working as expected.

    if (endTime.equals(LocalTime.MIDNIGHT) && timeRange.getEndTime().equals(LocalTime.MIDNIGHT)) {
      return true;
    }

    if (endTime.equals(LocalTime.MIDNIGHT)) {
      return timeRange.endTime.compareTo(startTime) >= 0;
    }

    if (timeRange.getEndTime().equals(LocalTime.MIDNIGHT)) {
      return timeRange.getStartTime().compareTo(endTime) <= 0;
    }

    return startTime.compareTo(timeRange.getEndTime()) <= 0 && endTime.compareTo(timeRange.getStartTime()) >= 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof TimeRange that)) {
      return false;
    }

    if (!endTime.equals(that.getEndTime())) {
      return false;
    }

    return startTime.equals(that.getStartTime());
  }

  @Override
  public int hashCode() {
    int result = startTime.hashCode();
    result = 31 * result + endTime.hashCode();
    return result;
  }

  @Override
  public int compareTo(TimeRange timeRange) {
    if (this == timeRange) {
      return 0;
    }

    if (timeRange == null) {
      return -1;
    }

    int result = startTime.compareTo(timeRange.getStartTime());

    if (result == 0) {
      result = endTime.compareTo(timeRange.getEndTime());
    }

    return result;
  }

  private TimeRange(LocalTime startTime, LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }
}
