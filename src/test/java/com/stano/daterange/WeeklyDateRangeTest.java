package com.stano.daterange;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeeklyDateRangeTest {

  @Test
  void withStartDateAndWithEndDateProduce7DayRanges() {
    assertEquals(
        LocalDate.of(2012, 1, 7),
        WeeklyDateRange.withStartDate(LocalDate.of(2012, 1, 1)).getEndDate());
    assertEquals(
        LocalDate.of(2012, 1, 1),
        WeeklyDateRange.withEndDate(LocalDate.of(2012, 1, 7)).getStartDate());
  }

  @Test
  void withTargetDateAlignsEndToRequestedDayOfWeek() {
    assertEquals(
        LocalDate.of(2014, 12, 13),
        WeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.FRIDAY)
            .getStartDate());
    assertEquals(
        LocalDate.of(2014, 12, 19),
        WeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.FRIDAY).getEndDate());

    assertEquals(
        LocalDate.of(2014, 12, 12),
        WeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.THURSDAY)
            .getStartDate());
    assertEquals(
        LocalDate.of(2014, 12, 18),
        WeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.THURSDAY)
            .getEndDate());

    assertEquals(
        LocalDate.of(2014, 12, 18),
        WeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.WEDNESDAY)
            .getStartDate());
    assertEquals(
        LocalDate.of(2014, 12, 24),
        WeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.WEDNESDAY)
            .getEndDate());

    assertEquals(
        LocalDate.of(2014, 12, 17),
        WeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.TUESDAY)
            .getStartDate());
    assertEquals(
        LocalDate.of(2014, 12, 23),
        WeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.TUESDAY).getEndDate());
  }

  @Test
  void priorNextOnComposedRangeBehaveWeekly() {
    DateRange dr = WeeklyDateRange.withEndDate(LocalDate.of(2019, 1, 7));

    assertEquals(LocalDate.of(2018, 12, 31), dr.prior().getEndDate());
    assertEquals(LocalDate.of(2019, 1, 8), dr.next().getStartDate());
    assertEquals(LocalDate.of(2019, 1, 21), dr.nextN(2).getEndDate());
  }
}
