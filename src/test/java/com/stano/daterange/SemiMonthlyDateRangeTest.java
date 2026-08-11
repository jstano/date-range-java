package com.stano.daterange;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemiMonthlyDateRangeTest {

  @Test
  void withEndDateProducesHalves1To15Or16ToEnd() {
    DateRange dr1 = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024, 5, 15));
    assertEquals(LocalDate.of(2024, 5, 1), dr1.getStartDate());
    assertEquals(LocalDate.of(2024, 5, 15), dr1.getEndDate());

    DateRange dr2 = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024, 5, 31));
    assertEquals(LocalDate.of(2024, 5, 16), dr2.getStartDate());
    assertEquals(LocalDate.of(2024, 5, 31), dr2.getEndDate());
  }

  @Test
  void priorSwitchesBetweenHalvesAndCrossesMonthWhenNeeded() {
    DateRange secondHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024, 5, 31)); // 16..31

    DateRange priorToSecond = secondHalf.prior();
    assertEquals(LocalDate.of(2024, 5, 1), priorToSecond.getStartDate());
    assertEquals(LocalDate.of(2024, 5, 15), priorToSecond.getEndDate());

    DateRange firstHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024, 5, 15)); // 1..15
    DateRange priorToFirst = firstHalf.prior();
    assertEquals(LocalDate.of(2024, 4, 16), priorToFirst.getStartDate());
    assertEquals(LocalDate.of(2024, 4, 30), priorToFirst.getEndDate());
  }

  @Test
  void nextSwitchesHalvesAndRollsIntoNextMonth() {
    DateRange firstHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2024, 5, 15)); // 1..15

    DateRange n1 = firstHalf.next();
    assertEquals(LocalDate.of(2024, 5, 16), n1.getStartDate());
    assertEquals(LocalDate.of(2024, 5, 31), n1.getEndDate());

    DateRange secondHalf = n1;
    DateRange n2 = secondHalf.next();
    assertEquals(LocalDate.of(2024, 6, 1), n2.getStartDate());
    assertEquals(LocalDate.of(2024, 6, 15), n2.getEndDate());
  }

  @Test
  void nextRollsFromDecemberSecondHalfToJanuaryFirstHalfYearRollover() {
    DateRange decSecondHalf =
        SemiMonthlyDateRange.withEndDate(LocalDate.of(2024, 12, 31)); // 2024-12-16..2024-12-31

    assertEquals(LocalDate.of(2024, 12, 16), decSecondHalf.getStartDate());
    assertEquals(LocalDate.of(2024, 12, 31), decSecondHalf.getEndDate());

    DateRange janFirstHalf = decSecondHalf.next();
    assertEquals(LocalDate.of(2025, 1, 1), janFirstHalf.getStartDate());
    assertEquals(LocalDate.of(2025, 1, 15), janFirstHalf.getEndDate());
  }
}
