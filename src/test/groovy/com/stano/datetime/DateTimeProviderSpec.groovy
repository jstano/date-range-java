package com.stano.datetime

import spock.lang.Specification

import java.time.LocalDateTime

class DateTimeProviderSpec extends Specification {

   def "methods should return values"() {

      def now = LocalDateTime.of(2015, 8, 21, 8, 21, 17, 45)
      DateTimeProvider.clock = ConstantClock.of(now)

      expect:
      DateTimeProvider.currentDate() == now.toLocalDate()
      DateTimeProvider.currentDateTime() == now
      DateTimeProvider.currentTime() == now.toLocalTime()
   }
}
