package com.stano.daterange


import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate

class WeeklyDateRangeSpec extends Specification {

	def "withStartDate and withEndDate produce 7-day ranges"() {
		expect:
		WeeklyDateRange.withStartDate(LocalDate.of(2012, 1, 1)).getEndDate() == LocalDate.of(2012, 1, 7)
		WeeklyDateRange.withEndDate(LocalDate.of(2012,1,7)).getStartDate() == LocalDate.of(2012, 1, 1)
	}

	def "withTargetDate aligns end to requested day of week"() {
		expect:
		WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.FRIDAY).getStartDate() == LocalDate.of(2014, 12, 13)
		WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.FRIDAY).getEndDate() == LocalDate.of(2014, 12, 19)

		WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.THURSDAY).getStartDate() == LocalDate.of(2014, 12, 12)
		WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.THURSDAY).getEndDate() == LocalDate.of(2014, 12, 18)

		WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.WEDNESDAY).getStartDate() == LocalDate.of(2014, 12, 18)
		WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.WEDNESDAY).getEndDate() == LocalDate.of(2014, 12, 24)

		WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.TUESDAY).getStartDate() == LocalDate.of(2014, 12, 17)
		WeeklyDateRange.withTargetDate(LocalDate.of(2014,12,18), DayOfWeek.TUESDAY).getEndDate() == LocalDate.of(2014, 12, 23)
	}

	def "prior/next on composed range behave weekly"() {
		given:
		def dr = WeeklyDateRange.withEndDate(LocalDate.of(2019,1,7))

		expect:
		dr.prior().getEndDate() == LocalDate.of(2018, 12, 31)
		dr.next().getStartDate() == LocalDate.of(2019, 1, 8)
		dr.nextN(2).getEndDate() == LocalDate.of(2019, 1, 21)
	}
}
