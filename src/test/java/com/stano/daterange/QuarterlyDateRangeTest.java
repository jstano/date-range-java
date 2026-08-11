package com.stano.daterange;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuarterlyDateRangeTest {

  @Test
  void withStartDateAndWithEndDateProduce3MonthQuartersWithNavigation() {
    DateRange s = QuarterlyDateRange.withStartDate(LocalDate.of(2023, 4, 10));
    assertEquals(LocalDate.of(2023, 4, 1), s.getStartDate());
    assertEquals(LocalDate.of(2023, 6, 30), s.getEndDate());

    DateRange e = QuarterlyDateRange.withEndDate(LocalDate.of(2023, 6, 30));
    assertEquals(LocalDate.of(2023, 4, 1), e.getStartDate());
    assertEquals(LocalDate.of(2023, 6, 30), e.getEndDate());

    assertEquals(LocalDate.of(2023, 1, 1), s.prior().getStartDate());
    assertEquals(LocalDate.of(2023, 3, 31), s.prior().getEndDate());
    assertEquals(LocalDate.of(2023, 7, 1), s.next().getStartDate());
    assertEquals(LocalDate.of(2023, 9, 30), s.next().getEndDate());
  }
}
