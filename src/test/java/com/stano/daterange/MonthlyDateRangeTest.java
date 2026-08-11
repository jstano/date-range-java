package com.stano.daterange;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonthlyDateRangeTest {

  @Test
  void withEndDateOnFirstCalendarMonthRangeAndNavigation() {
    LocalDate end = LocalDate.of(2023, 3, 31);
    DateRange dr = MonthlyDateRange.withEndDateOnFirst(end);

    assertEquals(LocalDate.of(2023, 3, 1), dr.getStartDate());
    assertEquals(LocalDate.of(2023, 3, 31), dr.getEndDate());
    assertEquals(1, dr.startDay().get());

    // prior goes to previous full month
    DateRange prior = dr.prior();
    assertEquals(LocalDate.of(2023, 2, 1), prior.getStartDate());
    assertEquals(LocalDate.of(2023, 2, 28), prior.getEndDate());
    assertEquals(1, prior.startDay().get());

    // next goes to next full month
    DateRange next = dr.next();
    assertEquals(LocalDate.of(2023, 4, 1), next.getStartDate());
    assertEquals(LocalDate.of(2023, 4, 30), next.getEndDate());
    assertEquals(1, next.startDay().get());
  }

  @Test
  void withEndDateAndStartDayNot1AnchoredDayWindow() {
    LocalDate end = LocalDate.of(2023, 3, 20);

    DateRange dr = MonthlyDateRange.withEndDateAndStartDay(end, 21); // anchored on 21st

    assertEquals(LocalDate.of(2023, 2, 21), dr.getStartDate());
    assertEquals(LocalDate.of(2023, 3, 20), dr.getEndDate());
    assertEquals(21, dr.startDay().get());

    DateRange prior = dr.prior();
    DateRange next = dr.next();

    assertEquals(LocalDate.of(2023, 1, 21), prior.getStartDate());
    assertEquals(LocalDate.of(2023, 2, 20), prior.getEndDate());
    assertEquals(21, prior.startDay().get());

    assertEquals(LocalDate.of(2023, 3, 21), next.getStartDate());
    assertEquals(LocalDate.of(2023, 4, 20), next.getEndDate());
    assertEquals(21, next.startDay().get());
  }
}
