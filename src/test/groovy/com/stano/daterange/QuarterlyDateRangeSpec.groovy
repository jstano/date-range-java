package com.stano.daterange


import spock.lang.Specification

import java.time.LocalDate

class QuarterlyDateRangeSpec extends Specification {

	def "withStartDate and withEndDate produce 3-month quarters with navigation"() {
		expect:
		def s = QuarterlyDateRange.withStartDate(LocalDate.of(2023, 4, 10))
		s.getStartDate() == LocalDate.of(2023, 4, 1)
		s.getEndDate() == LocalDate.of(2023, 6, 30)

		def e = QuarterlyDateRange.withEndDate(LocalDate.of(2023,6,30))
		e.getStartDate() == LocalDate.of(2023, 4, 1)
		e.getEndDate() == LocalDate.of(2023, 6, 30)

		and:
		s.prior().getStartDate() == LocalDate.of(2023, 1, 1)
		s.prior().getEndDate() == LocalDate.of(2023, 3, 31)
		s.next().getStartDate() == LocalDate.of(2023, 7, 1)
		s.next().getEndDate() == LocalDate.of(2023, 9, 30)
	}
}
