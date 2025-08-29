package com.stano.daterange


import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate

class WeeklyDateRangeSpec extends Specification {

  def "withStartDate and withEndDate produce 7-day ranges"() {
    expect:
    WeeklyDateRange.withStartDate(LocalDate.of(2012, 1, 1)).endDate() == LocalDate.of(2012, 1, 7)
    WeeklyDateRange.withEndDate(LocalDate.of(2012,1,7)).startDate() == LocalDate.of(2012,1,1)
  }

  def "withTargetDate aligns end to requested day of week"() {
    expect:
    WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.FRIDAY).startDate() == LocalDate.of(2014,12,13)
    WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.FRIDAY).endDate() == LocalDate.of(2014,12,19)

    WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.THURSDAY).startDate() == LocalDate.of(2014,12,12)
    WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.THURSDAY).endDate() == LocalDate.of(2014,12,18)

    WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.WEDNESDAY).startDate() == LocalDate.of(2014,12,18)
    WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.WEDNESDAY).endDate() == LocalDate.of(2014,12,24)

    WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.TUESDAY).startDate() == LocalDate.of(2014,12,17)
    WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.TUESDAY).endDate() == LocalDate.of(2014,12,23)
  }

  def "prior/next on composed range behave weekly"() {
    given:
    def dr = WeeklyDateRange.withEndDate(LocalDate.of(2019,1,7))

    expect:
    dr.prior().endDate() == LocalDate.of(2018,12,31)
    dr.next().startDate() == LocalDate.of(2019,1,8)
    dr.nextN(2).endDate() == LocalDate.of(2019,1,21)
  }
}
