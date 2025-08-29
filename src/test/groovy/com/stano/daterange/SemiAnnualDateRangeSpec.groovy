package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class SemiAnnualDateRangeSpec extends Specification {

  def "withStartDate and withEndDate cover 6 months and navigate"() {
    expect:
    def s = SemiAnnualDateRange.withStartDate(LocalDate.of(2023, 2, 1))
    s.startDate() == LocalDate.of(2023,2,1)
    s.endDate() == LocalDate.of(2023,7,31)

    def e = SemiAnnualDateRange.withEndDate(LocalDate.of(2023,12,31))
    e.startDate() == LocalDate.of(2023,7,1)
    e.endDate() == LocalDate.of(2023,12,31)

    and:
    s.next().startDate() == LocalDate.of(2023,8,1)
    s.next().endDate() == LocalDate.of(2024,1,31)
    s.prior().startDate() == LocalDate.of(2022,8,1)
    s.prior().endDate() == LocalDate.of(2023,1,31)
  }
}
