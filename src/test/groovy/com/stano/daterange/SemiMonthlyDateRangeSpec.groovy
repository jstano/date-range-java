package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class SemiMonthlyDateRangeSpec extends Specification {

  def "withEndDate produces halves 1..15 or 16..end"() {
    expect:
    def dr1 = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024, 5, 15))
    dr1.startDate() == LocalDate.of(2024,5,1)
    dr1.endDate() == LocalDate.of(2024,5,15)

    def dr2 = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,5,31))
    dr2.startDate() == LocalDate.of(2024,5,16)
    dr2.endDate() == LocalDate.of(2024,5,31)
  }

  def "prior switches between halves and crosses month when needed"() {
    given:
    def secondHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,5,31)) // 16..31

    expect:
    def priorToSecond = secondHalf.prior()
    priorToSecond.startDate() == LocalDate.of(2024,5,1)
    priorToSecond.endDate() == LocalDate.of(2024,5,15)

    and:
    def firstHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,5,15)) // 1..15
    def priorToFirst = firstHalf.prior()
    priorToFirst.startDate() == LocalDate.of(2024,4,16)
    priorToFirst.endDate() == LocalDate.of(2024,4,30)
  }

  def "next switches halves and rolls into next month"() {
    given:
    def firstHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,5,15)) // 1..15

    expect:
    def n1 = firstHalf.next()
    n1.startDate() == LocalDate.of(2024,5,16)
    n1.endDate() == LocalDate.of(2024,5,31)

    and:
    def secondHalf = n1
    def n2 = secondHalf.next()
    n2.startDate() == LocalDate.of(2024,6,1)
    n2.endDate() == LocalDate.of(2024,6,15)
  }

  def "next rolls from December second half to January first half (year rollover)"() {
    given:
    def decSecondHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,12,31)) // 2024-12-16..2024-12-31

    expect:
    decSecondHalf.startDate() == LocalDate.of(2024,12,16)
    decSecondHalf.endDate() == LocalDate.of(2024,12,31)

    and:
    def janFirstHalf = decSecondHalf.next()
    janFirstHalf.startDate() == LocalDate.of(2025,1,1)
    janFirstHalf.endDate() == LocalDate.of(2025,1,15)
  }
}
