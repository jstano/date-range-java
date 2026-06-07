package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class SemiAnnualDateRangeSpec extends Specification {

  def "withStartDate and withEndDate cover 6 months and navigate"() {
    expect:
    def s = SemiAnnualDateRange.withStartDate(LocalDate.of(2023, 2, 1))
    s.getStartDate() == LocalDate.of(2023, 2, 1)
    s.getEndDate() == LocalDate.of(2023, 7, 31)

    def e = SemiAnnualDateRange.withEndDate(LocalDate.of(2023,12,31))
    e.getStartDate() == LocalDate.of(2023, 7, 1)
    e.getEndDate() == LocalDate.of(2023, 12, 31)

    and:
    s.next().getStartDate() == LocalDate.of(2023, 8, 1)
    s.next().getEndDate() == LocalDate.of(2024, 1, 31)
    s.prior().getStartDate() == LocalDate.of(2022, 8, 1)
    s.prior().getEndDate() == LocalDate.of(2023, 1, 31)
  }
}
