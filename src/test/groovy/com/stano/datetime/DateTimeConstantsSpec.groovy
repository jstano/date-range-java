package com.stano.datetime

import spock.lang.Specification

class DateTimeConstantsSpec extends Specification {

   def "verify the constants are correct"() {

      expect:
      DateTimeConstants.MILLIS_PER_SECOND == 1000
      DateTimeConstants.MILLIS_PER_MINUTE == 1000 * 60
      DateTimeConstants.MILLIS_PER_HOUR == 1000 * 60 * 60
      DateTimeConstants.MILLIS_PER_DAY == 1000 * 60 * 60 * 24
      DateTimeConstants.MILLIS_PER_WEEK == 1000 * 60 * 60 * 24 * 7

      DateTimeConstants.SECONDS_PER_MINUTE == 60
      DateTimeConstants.SECONDS_PER_HOUR == 60 * 60
      DateTimeConstants.SECONDS_PER_DAY == 60 * 60 * 24
      DateTimeConstants.SECONDS_PER_WEEK == 60 * 60 * 24 * 7

      DateTimeConstants.MINUTES_PER_HOUR == 60
      DateTimeConstants.MINUTES_PER_DAY == 60 * 24
      DateTimeConstants.MINUTES_PER_WEEK == 60 * 24 * 7

      DateTimeConstants.HOURS_PER_DAY == 24
      DateTimeConstants.HOURS_PER_WEEK == 24 * 7

      DateTimeConstants.DAYS_PER_WEEK == 7
   }

   def "call the private constructor to get 100% coverage"() {

      expect:
      new DateTimeConstants() != null
   }
}
