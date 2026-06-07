package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class SemiMonthlyDateRangeSpec extends Specification {

  def "withEndDate produces halves 1..15 or 16..end"() {
    expect:
    def dr1 = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024, 5, 15))
    dr1.getStartDate() == LocalDate.of(2024, 5, 1)
    dr1.getEndDate() == LocalDate.of(2024, 5, 15)

    def dr2 = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,5,31))
    dr2.getStartDate() == LocalDate.of(2024, 5, 16)
    dr2.getEndDate() == LocalDate.of(2024, 5, 31)
  }

  def "prior switches between halves and crosses month when needed"() {
    given:
    def secondHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,5,31)) // 16..31

    expect:
    def priorToSecond = secondHalf.prior()
    priorToSecond.getStartDate() == LocalDate.of(2024, 5, 1)
    priorToSecond.getEndDate() == LocalDate.of(2024, 5, 15)

    and:
    def firstHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,5,15)) // 1..15
    def priorToFirst = firstHalf.prior()
    priorToFirst.getStartDate() == LocalDate.of(2024, 4, 16)
    priorToFirst.getEndDate() == LocalDate.of(2024, 4, 30)
  }

  def "next switches halves and rolls into next month"() {
    given:
    def firstHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,5,15)) // 1..15

    expect:
    def n1 = firstHalf.next()
    n1.getStartDate() == LocalDate.of(2024, 5, 16)
    n1.getEndDate() == LocalDate.of(2024, 5, 31)

    and:
    def secondHalf = n1
    def n2 = secondHalf.next()
    n2.getStartDate() == LocalDate.of(2024, 6, 1)
    n2.getEndDate() == LocalDate.of(2024, 6, 15)
  }

  def "next rolls from December second half to January first half (year rollover)"() {
    given:
    def decSecondHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024,12,31)) // 2024-12-16..2024-12-31

    expect:
    decSecondHalf.getStartDate() == LocalDate.of(2024, 12, 16)
    decSecondHalf.getEndDate() == LocalDate.of(2024, 12, 31)

    and:
    def janFirstHalf = decSecondHalf.next()
    janFirstHalf.getStartDate() == LocalDate.of(2025, 1, 1)
    janFirstHalf.getEndDate() == LocalDate.of(2025, 1, 15)
  }
}
