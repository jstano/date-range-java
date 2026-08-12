package com.stano.datetimerange;

import com.stano.datetime.DateTimeConstants;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.stream.IntStream;

/**
 * A {@link DateTimeRange} paired with a fixed period length in minutes, used to divide the range
 * into equal periods and compute period-index positions within a day.
 *
 * <p>This class is immutable and thread-safe.
 */
public final class DateTimeRangeWithPeriodLength implements Iterable<LocalDateTime>, Serializable {
  private final DateTimeRange dateTimeRange;
  private final int periodLengthInMinutes;

  /**
   * Creates a new DateTimeRangeWithPeriodLength from a DateTimeRange and a period length.
   *
   * @param dateTimeRange The date-time range.
   * @param periodLengthInMinutes The length of each period, in minutes.
   * @return A new DateTimeRangeWithPeriodLength.
   */
  public static DateTimeRangeWithPeriodLength of(
      DateTimeRange dateTimeRange, int periodLengthInMinutes) {
    return new DateTimeRangeWithPeriodLength(dateTimeRange, periodLengthInMinutes);
  }

  /**
   * Creates a new DateTimeRangeWithPeriodLength spanning the given start and end date-times, with
   * the given period length.
   *
   * @param startDateTime The start date-time of the range.
   * @param endDateTime The end date-time of the range.
   * @param periodLengthInMinutes The length of each period, in minutes.
   * @return A new DateTimeRangeWithPeriodLength.
   */
  public static DateTimeRangeWithPeriodLength of(
      LocalDateTime startDateTime, LocalDateTime endDateTime, int periodLengthInMinutes) {
    return new DateTimeRangeWithPeriodLength(
        DateTimeRange.of(startDateTime, endDateTime), periodLengthInMinutes);
  }

  /**
   * Gets the period index of the range's start date-time within its start day, where index 0 is
   * midnight and each period is periodLengthInMinutes long.
   *
   * @return The period index of the start date-time.
   */
  public int getStartIndex() {
    LocalDateTime startDateTime = dateTimeRange.getStartDateTime();

    return (startDateTime.getHour() * DateTimeConstants.MINUTES_PER_HOUR
            + startDateTime.getMinute())
        / periodLengthInMinutes;
  }

  /**
   * Gets the period index of the range's end date-time, measured in periods from midnight of the
   * start date-time's day. If the end date-time falls on a later day than the start date-time, the
   * index is offset by a full day's worth of periods.
   *
   * @return The period index of the end date-time.
   */
  public int getEndIndex() {
    LocalDateTime endDateTime = dateTimeRange.getEndDateTime();

    int endIndex =
        (endDateTime.getHour() * DateTimeConstants.MINUTES_PER_HOUR + endDateTime.getMinute());

    if (endDateTime.toLocalDate().isAfter(dateTimeRange.getStartDateTime().toLocalDate())) {
      endIndex += DateTimeConstants.MINUTES_PER_DAY;
    }

    return endIndex / periodLengthInMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DateTimeRangeWithPeriodLength that = (DateTimeRangeWithPeriodLength) o;

    return dateTimeRange.equals(that.dateTimeRange);
  }

  @Override
  public int hashCode() {
    return dateTimeRange.hashCode();
  }

  /**
   * Gets an iterator over the date-times in the range, stepping by the configured period length.
   *
   * @return An iterator over the date-times in the range.
   */
  @Override
  public Iterator<LocalDateTime> iterator() {
    return new DateTimeRangeIterator(dateTimeRange, periodLengthInMinutes);
  }

  private DateTimeRangeWithPeriodLength(DateTimeRange dateTimeRange, int periodLengthInMinutes) {
    this.dateTimeRange = dateTimeRange;
    this.periodLengthInMinutes = periodLengthInMinutes;
  }

  /**
   * Gets the configured period length.
   *
   * @return The length of each period, in minutes.
   */
  public int getPeriodLengthInMinutes() {
    return periodLengthInMinutes;
  }

  /**
   * Gets the underlying date-time range.
   *
   * @return The DateTimeRange.
   */
  public DateTimeRange getDateTimeRange() {
    return dateTimeRange;
  }

  /**
   * Gets a stream of the period indexes spanned by the range, from {@link #getStartIndex()}
   * (inclusive) to {@link #getEndIndex()} (exclusive).
   *
   * @return A stream of period indexes.
   */
  public IntStream getIndexStream() {
    return IntStream.range(getStartIndex(), getEndIndex());
  }

  /**
   * Gets the number of whole periods that fit within the range's duration.
   *
   * @return The number of periods in the range.
   */
  public int getNumberOfPeriodsInRange() {
    return (int) (dateTimeRange.getDuration().toMinutes() / periodLengthInMinutes);
  }
}
