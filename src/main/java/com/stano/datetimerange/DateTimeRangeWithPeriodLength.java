package com.stano.datetimerange;

import com.stano.datetime.DateTimeConstants;
import com.stano.integerrange.IntegerRange;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;

public final class DateTimeRangeWithPeriodLength implements Iterable<LocalDateTime>, Serializable {
   private final DateTimeRange dateTimeRange;
   private final int periodLengthInMinutes;

   public static DateTimeRangeWithPeriodLength of(DateTimeRange dateTimeRange, int periodLengthInMinutes) {
      return new DateTimeRangeWithPeriodLength(dateTimeRange, periodLengthInMinutes);
   }

   public static DateTimeRangeWithPeriodLength of(LocalDateTime startDateTime, LocalDateTime endDateTime, int periodLengthInMinutes) {
      return new DateTimeRangeWithPeriodLength(DateTimeRange.of(startDateTime, endDateTime), periodLengthInMinutes);
   }

   public int getStartIndex() {
      LocalDateTime startDateTime = dateTimeRange.getStartDateTime();

      return (startDateTime.getHour() * DateTimeConstants.MINUTES_PER_HOUR + startDateTime.getMinute()) / periodLengthInMinutes;
   }

   public int getEndIndex() {
      LocalDateTime endDateTime = dateTimeRange.getEndDateTime();

      int endIndex = (endDateTime.getHour() * DateTimeConstants.MINUTES_PER_HOUR + endDateTime.getMinute());

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

      DateTimeRangeWithPeriodLength that = (DateTimeRangeWithPeriodLength)o;

      return dateTimeRange.equals(that.dateTimeRange);
   }

   @Override
   public int hashCode() {
      return dateTimeRange.hashCode();
   }

   @Override
   public Iterator<LocalDateTime> iterator() {
      return new DateTimeRangeIterator(dateTimeRange, periodLengthInMinutes);
   }

   private DateTimeRangeWithPeriodLength(DateTimeRange dateTimeRange, int periodLengthInMinutes) {
      this.dateTimeRange = dateTimeRange;
      this.periodLengthInMinutes = periodLengthInMinutes;
   }

   public int getPeriodLengthInMinutes() {
      return periodLengthInMinutes;
   }

   public DateTimeRange getDateTimeRange() {
      return dateTimeRange;
   }

   public IntegerRange getIndexRange() {
      return IntegerRange.of(getStartIndex(),
                             getEndIndex() - 1);
   }

   public int getNumberOfPeriodsInRange() {
      return (int)(dateTimeRange.getDuration().toMinutes() / periodLengthInMinutes);
   }
}
