package com.stano.daterange;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BiWeeklyDateRangeTest {

  @Test
  void withStartDateAndWithEndDateProduce14DayRanges() {
    assertEquals(
        LocalDate.of(2012, 1, 14),
        BiWeeklyDateRange.withStartDate(LocalDate.of(2012, 1, 1)).getEndDate());
    assertEquals(
        LocalDate.of(2012, 1, 1),
        BiWeeklyDateRange.withEndDate(LocalDate.of(2012, 1, 14)).getStartDate());
  }

  @Test
  void withTargetDateAlignsEndToRequestedDayOfWeek2WeekWindow() {
    assertEquals(
        LocalDate.of(2014, 12, 6),
        BiWeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.FRIDAY)
            .getStartDate());
    assertEquals(
        LocalDate.of(2014, 12, 19),
        BiWeeklyDateRange.withTargetDate(LocalDate.of(2014, 12, 18), DayOfWeek.FRIDAY)
            .getEndDate());
  }

  @Test
  void withTargetDateWrapsToNextWeekWhenEndDayIsEarlierInTheWeekOffsetLessThanZeroBranch() {
    // 2014-12-18 is a Thursday; requesting MONDAY should wrap to next week's Monday
    LocalDate target = LocalDate.of(2014, 12, 18);

    DateRange range = BiWeeklyDateRange.withTargetDate(target, DayOfWeek.MONDAY);

    assertEquals(
        LocalDate.of(2014, 12, 22), range.getEndDate()); // Thu -> next Mon (offset -3 + 7 = 4)
    assertEquals(LocalDate.of(2014, 12, 9), range.getStartDate()); // end - 13 days
  }
}
