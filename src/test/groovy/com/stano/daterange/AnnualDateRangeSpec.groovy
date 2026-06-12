package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class AnnualDateRangeSpec extends Specification {

	def "withStartDate and withEndDate cover 1 year and navigate"() {
		expect:
		def s = AnnualDateRange.withStartDate(LocalDate.of(2021, 3, 1))
		s.getStartDate() == LocalDate.of(2021, 3, 1)
		s.getEndDate() == LocalDate.of(2022, 2, 28)

		def e = AnnualDateRange.withEndDate(LocalDate.of(2022,2,28))
		e.getStartDate() == LocalDate.of(2021, 3, 1)
		e.getEndDate() == LocalDate.of(2022, 2, 28)

		and:
		s.prior().getStartDate() == LocalDate.of(2020, 3, 1)
		s.prior().getEndDate() == LocalDate.of(2021, 2, 28)
		s.next().getStartDate() == LocalDate.of(2022, 3, 1)
		s.next().getEndDate() == LocalDate.of(2023, 2, 28)
	}

	def "start on Feb 29 ends on Feb 28 next year"() {
		expect:
		def s = AnnualDateRange.withStartDate(LocalDate.of(2020,2,29))
		s.getEndDate() == LocalDate.of(2021, 2, 28)

		and:
		s.next().getStartDate() == LocalDate.of(2021, 2, 28)
		s.next().getEndDate() == LocalDate.of(2022, 2, 27)
	}
}
