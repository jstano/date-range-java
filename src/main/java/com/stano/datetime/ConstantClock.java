package com.stano.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConstantClock implements Clock {

   private final LocalDateTime localDateTime;

   public static Clock of(LocalDateTime localDateTime) {

      return new ConstantClock(localDateTime);
   }

   @Override
   public LocalDate currentDate() {

      return localDateTime.toLocalDate();
   }

   @Override
   public LocalDateTime currentDateTime() {

      return localDateTime;
   }

   @Override
   public LocalTime currentTime() {

      return localDateTime.toLocalTime();
   }

   private ConstantClock(LocalDateTime localDateTime) {

      this.localDateTime = localDateTime;
   }
}
