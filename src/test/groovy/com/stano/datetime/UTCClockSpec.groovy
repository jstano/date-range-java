package com.stano.datetime

import spock.lang.Specification

class UTCClockSpec extends Specification {
  def "methods should return values"() {
    def clock = new UTCClock()

    expect:
    clock.currentDate()
    clock.currentDateTime()
    clock.currentTime()
  }
}
