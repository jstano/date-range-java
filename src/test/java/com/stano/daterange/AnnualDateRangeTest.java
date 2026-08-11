package com.stano.daterange;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnualDateRangeTest {

  @Test
  void withStartDateAndWithEndDateCover1YearAndNavigate() {
    DateRange s = AnnualDateRange.withStartDate(LocalDate.of(2021, 3, 1));
    assertEquals(LocalDate.of(2021, 3, 1), s.getStartDate());
    assertEquals(LocalDate.of(2022, 2, 28), s.getEndDate());

    DateRange e = AnnualDateRange.withEndDate(LocalDate.of(2022, 2, 28));
    assertEquals(LocalDate.of(2021, 3, 1), e.getStartDate());
    assertEquals(LocalDate.of(2022, 2, 28), e.getEndDate());

    assertEquals(LocalDate.of(2020, 3, 1), s.prior().getStartDate());
    assertEquals(LocalDate.of(2021, 2, 28), s.prior().getEndDate());
    assertEquals(LocalDate.of(2022, 3, 1), s.next().getStartDate());
    assertEquals(LocalDate.of(2023, 2, 28), s.next().getEndDate());
  }

  @Test
  void startOnFeb29EndsOnFeb28NextYear() {
    DateRange s = AnnualDateRange.withStartDate(LocalDate.of(2020, 2, 29));
    assertEquals(LocalDate.of(2021, 2, 28), s.getEndDate());

    assertEquals(LocalDate.of(2021, 2, 28), s.next().getStartDate());
    assertEquals(LocalDate.of(2022, 2, 27), s.next().getEndDate());
  }
}
