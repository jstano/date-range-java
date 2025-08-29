package com.stano.daterange


import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate

class BiWeeklyDateRangeSpec extends Specification {

  def "withStartDate and withEndDate produce 14-day ranges"() {
    expect:
    BiWeeklyDateRange.withStartDate(LocalDate.of(2012, 1, 1)).endDate() == LocalDate.of(2012, 1, 14)
    BiWeeklyDateRange.withEndDate(LocalDate.of(2012,1,14)).startDate() == LocalDate.of(2012,1,1)
  }

  def "withTargetDate aligns end to requested day of week (2-week window)"() {
    expect:
    BiWeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.FRIDAY).startDate() == LocalDate.of(2014,12,6)
    BiWeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.FRIDAY).endDate() == LocalDate.of(2014,12,19)
  }

  def "withTargetDate wraps to next week when endDay is earlier in the week (offset < 0 branch)"() {
    given:
    // 2014-12-18 is a Thursday; requesting MONDAY should wrap to next week's Monday
    LocalDate target = LocalDate.of(2014, 12, 18)

    when:
    def range = BiWeeklyDateRange.withTargetDate(target, DayOfWeek.MONDAY)

    then:
    range.endDate() == LocalDate.of(2014, 12, 22) // Thu -> next Mon (offset -3 + 7 = 4)
    range.startDate() == LocalDate.of(2014, 12, 9) // end - 13 days
  }
}
