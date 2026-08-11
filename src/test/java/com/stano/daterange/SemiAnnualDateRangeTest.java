package com.stano.daterange;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemiAnnualDateRangeTest {

  @Test
  void withStartDateAndWithEndDateCover6MonthsAndNavigate() {
    DateRange s = SemiAnnualDateRange.withStartDate(LocalDate.of(2023, 2, 1));
    assertEquals(LocalDate.of(2023, 2, 1), s.getStartDate());
    assertEquals(LocalDate.of(2023, 7, 31), s.getEndDate());

    DateRange e = SemiAnnualDateRange.withEndDate(LocalDate.of(2023, 12, 31));
    assertEquals(LocalDate.of(2023, 7, 1), e.getStartDate());
    assertEquals(LocalDate.of(2023, 12, 31), e.getEndDate());

    assertEquals(LocalDate.of(2023, 8, 1), s.next().getStartDate());
    assertEquals(LocalDate.of(2024, 1, 31), s.next().getEndDate());
    assertEquals(LocalDate.of(2022, 8, 1), s.prior().getStartDate());
    assertEquals(LocalDate.of(2023, 1, 31), s.prior().getEndDate());
  }
}
