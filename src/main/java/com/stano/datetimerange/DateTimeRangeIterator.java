package com.stano.datetimerange;

import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * Iterates over the date-times within a {@link DateTimeRange} in fixed-length increments, starting
 * at the range's start date-time and stopping once the next date-time would reach or pass the
 * range's end date-time.
 *
 * <p>This iterator does not support {@link #remove()}.
 */
public class DateTimeRangeIterator implements Iterator<LocalDateTime> {
  private final DateTimeRange dateTimeRange;
  private final int periodLengthInMinutes;

  private LocalDateTime nextDateTime;

  /**
   * Creates a new iterator over dateTimeRange, stepping periodLengthInMinutes minutes at a time.
   *
   * @param dateTimeRange The range to iterate over.
   * @param periodLengthInMinutes The number of minutes to advance on each step.
   */
  public DateTimeRangeIterator(DateTimeRange dateTimeRange, int periodLengthInMinutes) {
    this.dateTimeRange = dateTimeRange;
    this.periodLengthInMinutes = periodLengthInMinutes;
    this.nextDateTime = dateTimeRange.getStartDateTime();
  }

  @Override
  public boolean hasNext() {
    return nextDateTime != null;
  }

  @Override
  public LocalDateTime next() {
    LocalDateTime result = nextDateTime;

    nextDateTime = nextDateTime.plusMinutes(periodLengthInMinutes);

    if (nextDateTime.compareTo(dateTimeRange.getEndDateTime()) >= 0) {
      nextDateTime = null;
    }

    return result;
  }

  /**
   * Always throws, since removal is not supported by this iterator.
   *
   * @throws UnsupportedOperationException always.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException(
        "The remove method is not supported by DateTimeRangeIterator.");
  }
}
