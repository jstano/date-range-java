package com.stano.datetimerange;

import java.time.LocalDateTime;
import java.util.Iterator;

public class DateTimeRangeIterator implements Iterator<LocalDateTime> {
  private final DateTimeRange dateTimeRange;
  private final int periodLengthInMinutes;

  private LocalDateTime nextDateTime;

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

  @Override
  public void remove() {
    throw new UnsupportedOperationException("The remove method is not supported by DateTimeRangeIterator.");
  }
}
