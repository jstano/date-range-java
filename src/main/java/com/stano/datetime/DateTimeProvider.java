package com.stano.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DateTimeProvider {

   private static Clock clock = new UTCClock();

   public static LocalDate currentDate() {

      return clock.currentDate();
   }

   public static LocalDateTime currentDateTime() {

      return clock.currentDateTime();
   }

   public static LocalTime currentTime() {

      return clock.currentTime();
   }

   private DateTimeProvider() {

   }
}
