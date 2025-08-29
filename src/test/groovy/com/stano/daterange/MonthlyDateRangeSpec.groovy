package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class MonthlyDateRangeSpec extends Specification {

  def "withEndDateOnFirst: calendar month range and navigation"() {
    given:
    def end = LocalDate.of(2023,3,31)
    def dr = MonthlyDateRange.withEndDateOnFirst(end)

    expect:
    dr.startDate() == LocalDate.of(2023,3,1)
    dr.endDate() == LocalDate.of(2023,3,31)
    dr.startDay().get() == 1

    and: 'prior goes to previous full month'
    def prior = dr.prior()
    prior.startDate() == LocalDate.of(2023,2,1)
    prior.endDate() == LocalDate.of(2023,2,28)
    prior.startDay().get() == 1

    and: 'next goes to next full month'
    def next = dr.next()
    next.startDate() == LocalDate.of(2023,4,1)
    next.endDate() == LocalDate.of(2023,4,30)
    next.startDay().get() == 1
  }

  def "withEndDateAndStartDay != 1: anchored day window"() {
    given:
    def end = LocalDate.of(2023,3,20)

    when:
    def dr = MonthlyDateRange.withEndDateAndStartDay(end, 21) // anchored on 21st

    then:
    dr.startDate() == LocalDate.of(2023,2,21)
    dr.endDate() == LocalDate.of(2023,3,20)
    dr.startDay().get() == 21

    when:
    def prior = dr.prior()
    def next = dr.next()

    then:
    prior.startDate() == LocalDate.of(2023,1,21)
    prior.endDate() == LocalDate.of(2023,2,20)
    prior.startDay().get() == 21

    next.startDate() == LocalDate.of(2023,3,21)
    next.endDate() == LocalDate.of(2023,4,20)
    next.startDay().get() == 21
  }
}
