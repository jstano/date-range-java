package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class DateUtilsSpec extends Specification {

  def "first and last day of month plus add/sub months/years"() {
    expect:
    DateUtils.firstDayOfMonth(LocalDate.of(2023, 2, 15)) == LocalDate.of(2023, 2, 1)
    DateUtils.lastDayOfMonth(LocalDate.of(2023,2,2)) == LocalDate.of(2023,2,28)

    DateUtils.addMonths(LocalDate.of(2023,1,31), 1) == LocalDate.of(2023,2,28)
    DateUtils.subtractMonths(LocalDate.of(2023,3,31), 1) == LocalDate.of(2023,2,28)

    DateUtils.addYears(LocalDate.of(2020,2,29), 1) == LocalDate.of(2021,2,28)
    DateUtils.subtractYears(LocalDate.of(2021,3,1), 1) == LocalDate.of(2020,3,1)
  }
}
