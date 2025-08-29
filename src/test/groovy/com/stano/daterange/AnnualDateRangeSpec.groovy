package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class AnnualDateRangeSpec extends Specification {

  def "withStartDate and withEndDate cover 1 year and navigate"() {
    expect:
    def s = AnnualDateRange.withStartDate(LocalDate.of(2021, 3, 1))
    s.startDate() == LocalDate.of(2021,3,1)
    s.endDate() == LocalDate.of(2022,2,28)

    def e = AnnualDateRange.withEndDate(LocalDate.of(2022,2,28))
    e.startDate() == LocalDate.of(2021,3,1)
    e.endDate() == LocalDate.of(2022,2,28)

    and:
    s.prior().startDate() == LocalDate.of(2020,3,1)
    s.prior().endDate() == LocalDate.of(2021,2,28)
    s.next().startDate() == LocalDate.of(2022,3,1)
    s.next().endDate() == LocalDate.of(2023,2,28)
  }

  def "start on Feb 29 ends on Feb 28 next year"() {
    expect:
    def s = AnnualDateRange.withStartDate(LocalDate.of(2020,2,29))
    s.endDate() == LocalDate.of(2021,2,28)

    and:
    s.next().startDate() == LocalDate.of(2021,2,28)
    s.next().endDate() == LocalDate.of(2022,2,27)
  }
}
