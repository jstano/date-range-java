package com.stano.daterange


import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate

class DateRangeSpec extends Specification {
  def "containsRange covers boundary and non-contained scenarios"() {
    given:
    def base = DateRange.of(LocalDate.of(2023,5,1), LocalDate.of(2023,5,14))

    expect:
    base.containsRange(other) == expected

    where:
    other                                                                                       || expected
    // identical
    DateRange.of(LocalDate.of(2023,5,1), LocalDate.of(2023,5,14))                               || true
    // fully inside
    DateRange.of(LocalDate.of(2023,5,3), LocalDate.of(2023,5,10))                               || true
    // shares start, ends earlier
    DateRange.of(LocalDate.of(2023,5,1), LocalDate.of(2023,5,5))                                || true
    // shares end, starts later
    DateRange.of(LocalDate.of(2023,5,10), LocalDate.of(2023,5,14))                              || true
    // overlaps but starts before base -> not contained
    DateRange.of(LocalDate.of(2023,4,30), LocalDate.of(2023,5,2))                               || false
    // overlaps but ends after base -> not contained
    DateRange.of(LocalDate.of(2023,5,13), LocalDate.of(2023,5,15))                              || false
    // entirely before
    DateRange.of(LocalDate.of(2023,4,20), LocalDate.of(2023,4,25))                              || false
    // entirely after
    DateRange.of(LocalDate.of(2023,5,20), LocalDate.of(2023,5,25))                              || false
    // single-day contained (at start boundary)
    DateRange.of(LocalDate.of(2023,5,1), LocalDate.of(2023,5,1))                                || true
    // single-day outside
    DateRange.of(LocalDate.of(2023,4,30), LocalDate.of(2023,4,30))                              || false
  }

  def "constructor validation and basic getters"() {
    when: 'end before start'
    new DateRange(LocalDate.of(2020, 1, 5), LocalDate.of(2020, 1, 4))

    then:
    thrown(IllegalArgumentException)

    when: 'null start'
    new DateRange(null, LocalDate.of(2020, 1, 4))

    then:
    def ex1 = thrown(IllegalArgumentException)
    ex1.message == 'dates required'

    when: 'null end'
    new DateRange(LocalDate.of(2020, 1, 4), null)

    then:
    def ex2 = thrown(IllegalArgumentException)
    ex2.message == 'dates required'

    when: 'both null'
    new DateRange(null, null)

    then:
    def ex3 = thrown(IllegalArgumentException)
    ex3.message == 'dates required'

    when:
    def dr = DateRange.of(LocalDate.of(2020,1,1), LocalDate.of(2020,1,3))

    then:
    dr.startDate() == LocalDate.of(2020,1,1)
    dr.endDate() == LocalDate.of(2020,1,3)
    dr.len() == 3
  }

  def "dates and dateAt and iterator"() {
    given:
    def dr = DateRange.of(LocalDate.of(2020,3,30), LocalDate.of(2020,4,2))

    expect:
    dr.dates() == [LocalDate.of(2020,3,30), LocalDate.of(2020,3,31), LocalDate.of(2020,4,1), LocalDate.of(2020,4,2)]
    dr.dateAt(-1).isEmpty()
    dr.dateAt(0).get() == LocalDate.of(2020,3,30)
    dr.dateAt(3).get() == LocalDate.of(2020,4,2)
    dr.dateAt(4).isEmpty()

    and: 'iterator yields each day'
    def iterList = []
    for (LocalDate d : dr) iterList.add(d)
    iterList == dr.dates()
  }

  def "datesForDay and contains checks"() {
    given:
    def dr = DateRange.of(LocalDate.of(2023,5,1), LocalDate.of(2023,5,14))

    expect:
    dr.datesForDay(DayOfWeek.MONDAY).every { it.getDayOfWeek() == DayOfWeek.MONDAY }
    dr.containsDate(LocalDate.of(2023,5,1))
    dr.containsDate(LocalDate.of(2023,5,14))
    !dr.containsDate(LocalDate.of(2023,4,30))

    and:
    def inner = DateRange.of(LocalDate.of(2023,5,3), LocalDate.of(2023,5,10))
    dr.containsRange(inner)
    !inner.containsRange(dr)
  }

  def "overlaps and overlapsAny"() {
    given:
    def a = DateRange.of(LocalDate.of(2023,1,1), LocalDate.of(2023,1,10))
    def b = DateRange.of(LocalDate.of(2023,1,10), LocalDate.of(2023,1,20))
    def c = DateRange.of(LocalDate.of(2023,1,21), LocalDate.of(2023,1,30))

    expect:
    a.overlaps(b)
    !a.overlaps(c)
    a.overlapsAny([c, b])
    !c.overlapsAny([a])
  }

  def "default prior/next shift by length and N variants"() {
    given:
    def dr = DateRange.of(LocalDate.of(2020,6,1), LocalDate.of(2020,6,7)) // 7 days

    expect:
    dr.prior().startDate() == LocalDate.of(2020,5,25)
    dr.prior().endDate() == LocalDate.of(2020,5,31)
    dr.next().startDate() == LocalDate.of(2020,6,8)
    dr.nextN(2).startDate() == LocalDate.of(2020,6,15)
    dr.priorN(2).endDate() == LocalDate.of(2020,5,24)
  }

  def "rangesBefore/After Inclusive and window ordering"() {
    given:
    def dr = DateRange.of(LocalDate.of(2022,10,10), LocalDate.of(2022,10,12))

    when:
    def bex = dr.rangesBefore(2)
    def bexi = dr.rangesBeforeInclusive(2)
    def aft = dr.rangesAfter(2)
    def afti = dr.rangesAfterInclusive(2)
    def win = dr.rangesWindow(2, 2)

    then: 'before lists are in chronological order (earlier first)'
    bex*.startDate() == [LocalDate.of(2022,10,4), LocalDate.of(2022,10,7)]
    bexi*.startDate() == [LocalDate.of(2022,10,4), LocalDate.of(2022,10,7), LocalDate.of(2022,10,10)]

    and:
    aft*.startDate() == [LocalDate.of(2022,10,13), LocalDate.of(2022,10,16)]
    afti*.startDate() == [LocalDate.of(2022,10,10), LocalDate.of(2022,10,13), LocalDate.of(2022,10,16)]

    and:
    win*.startDate() == [LocalDate.of(2022,10,4), LocalDate.of(2022,10,7), LocalDate.of(2022,10,10), LocalDate.of(2022,10,13), LocalDate.of(2022,10,16)]
  }

  def "rangesContainingSpan and rangeContainingDate with composed prior/next"() {
    given:
    def weekly = WeeklyDateRange.withEndDate(LocalDate.of(2024, 1, 7)) // 2024-01-01..2024-01-07

    expect: 'rangeContainingDate navigates to the right weekly range'
    weekly.rangeContainingDate(LocalDate.of(2024,1,20)).startDate() == LocalDate.of(2024,1,15)

    and:
    def spans = weekly.rangesContainingSpan(LocalDate.of(2024,1,5), LocalDate.of(2024,1,20))
    spans*.startDate() == [LocalDate.of(2024,1,1), LocalDate.of(2024,1,8), LocalDate.of(2024,1,15)]
    spans*.endDate() == [LocalDate.of(2024,1,7), LocalDate.of(2024,1,14), LocalDate.of(2024,1,21)]
  }

  def "rangeContainingDate navigates using default next/prior (no composition)"() {
    given:
    // Base range of 7 days: 2020-06-01..2020-06-07
    def base = DateRange.of(LocalDate.of(2020,6,1), LocalDate.of(2020,6,7))

    expect: 'returns same range when date is inside'
    def same = base.rangeContainingDate(LocalDate.of(2020,6,6))
    same.startDate() == LocalDate.of(2020,6,1)
    same.endDate() == LocalDate.of(2020,6,7)

    and: 'navigates forward one window when date is after the range'
    def forwardOne = base.rangeContainingDate(LocalDate.of(2020,6,10))
    forwardOne.startDate() == LocalDate.of(2020,6,8)
    forwardOne.endDate() == LocalDate.of(2020,6,14)

    and: 'navigates backward one window when date is before the range'
    def backwardOne = base.rangeContainingDate(LocalDate.of(2020,5,28))
    backwardOne.startDate() == LocalDate.of(2020,5,25)
    backwardOne.endDate() == LocalDate.of(2020,5,31)

    and: 'can hop multiple windows forward to reach far date'
    def far = base.rangeContainingDate(LocalDate.of(2020,7,1))
    far.startDate() == LocalDate.of(2020,6,29)
    far.endDate() == LocalDate.of(2020,7,5)
  }

  def "rangesContainingSpan uses default step and validates errors"() {
    given:
    // Base range defines 7-day windows
    def base = DateRange.of(LocalDate.of(2020,6,1), LocalDate.of(2020,6,7))

    when: 'null fromDate'
    base.rangesContainingSpan(null, LocalDate.of(2020,6,10))
    then:
    def ex1 = thrown(IllegalArgumentException)
    ex1.message == 'dates required'

    when: 'null toDate'
    base.rangesContainingSpan(LocalDate.of(2020,6,1), null)
    then:
    def ex2 = thrown(IllegalArgumentException)
    ex2.message == 'dates required'

    when: 'toDate before fromDate'
    base.rangesContainingSpan(LocalDate.of(2020,6,10), LocalDate.of(2020,6,5))
    then:
    def ex3 = thrown(IllegalArgumentException)
    ex3.message == 'to before from'

    when: 'span entirely inside one base window'
    def single = base.rangesContainingSpan(LocalDate.of(2020,6,2), LocalDate.of(2020,6,6))
    then:
    single*.startDate() == [LocalDate.of(2020,6,1)]
    single*.endDate() == [LocalDate.of(2020,6,7)]

    when: 'span crosses multiple windows'
    def multi = base.rangesContainingSpan(LocalDate.of(2020,6,5), LocalDate.of(2020,6,20))
    then:
    multi*.startDate() == [LocalDate.of(2020,6,1), LocalDate.of(2020,6,8), LocalDate.of(2020,6,15)]
    multi*.endDate() == [LocalDate.of(2020,6,7), LocalDate.of(2020,6,14), LocalDate.of(2020,6,21)]
  }

  def "equals, hashCode, compareTo"() {
    given:
    def a1a = DateRange.of(LocalDate.of(2020,1,1), LocalDate.of(2020,1,2))
    def a1b = DateRange.of(LocalDate.of(2020,1,1), LocalDate.of(2020,1,2))
    def a2 = DateRange.of(LocalDate.of(2020,1,1), LocalDate.of(2020,1,3))
    def b = DateRange.of(LocalDate.of(2020,1,2), LocalDate.of(2020,1,3))
    def c = DateRange.of(LocalDate.of(2019,12,31), LocalDate.of(2020,1,3))

    expect:
    a1a.equals(a1a)
    a1a.equals(a1b)
    !a1a.equals(a2)
    !a1a.equals(c)
    !a1a.equals(null)
    !a1a.equals("abc")

    a1a.hashCode() == a1b.hashCode()
    a1a.hashCode() != a2.hashCode()
    a1a.compareTo(a1a) == 0
    a1a.compareTo(a1b) == 0
    a1a.compareTo(b) < 0
    a1a.compareTo(c) > 0
  }
}
