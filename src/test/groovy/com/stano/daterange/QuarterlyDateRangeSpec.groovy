package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class QuarterlyDateRangeSpec extends Specification {

  def "withStartDate and withEndDate produce 3-month quarters with navigation"() {
    expect:
    def s = QuarterlyDateRange.withStartDate(LocalDate.of(2023, 4, 10))
    s.startDate() == LocalDate.of(2023,4,1)
    s.endDate() == LocalDate.of(2023,6,30)

    def e = QuarterlyDateRange.withEndDate(LocalDate.of(2023,6,30))
    e.startDate() == LocalDate.of(2023,4,1)
    e.endDate() == LocalDate.of(2023,6,30)

    and:
    s.prior().startDate() == LocalDate.of(2023,1,1)
    s.prior().endDate() == LocalDate.of(2023,3,31)
    s.next().startDate() == LocalDate.of(2023,7,1)
    s.next().endDate() == LocalDate.of(2023,9,30)
  }
}
