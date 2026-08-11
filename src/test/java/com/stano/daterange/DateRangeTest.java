package com.stano.daterange;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DateRangeTest {

  static Stream<Arguments> containsRangeProvider() {
    return Stream.of(
        // identical
        Arguments.of(DateRange.of(LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 14)), true),
        // fully inside
        Arguments.of(DateRange.of(LocalDate.of(2023, 5, 3), LocalDate.of(2023, 5, 10)), true),
        // shares start, ends earlier
        Arguments.of(DateRange.of(LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 5)), true),
        // shares end, starts later
        Arguments.of(DateRange.of(LocalDate.of(2023, 5, 10), LocalDate.of(2023, 5, 14)), true),
        // overlaps but starts before base -> not contained
        Arguments.of(DateRange.of(LocalDate.of(2023, 4, 30), LocalDate.of(2023, 5, 2)), false),
        // overlaps but ends after base -> not contained
        Arguments.of(DateRange.of(LocalDate.of(2023, 5, 13), LocalDate.of(2023, 5, 15)), false),
        // entirely before
        Arguments.of(DateRange.of(LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 25)), false),
        // entirely after
        Arguments.of(DateRange.of(LocalDate.of(2023, 5, 20), LocalDate.of(2023, 5, 25)), false),
        // single-day contained (at start boundary)
        Arguments.of(DateRange.of(LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 1)), true),
        // single-day outside
        Arguments.of(DateRange.of(LocalDate.of(2023, 4, 30), LocalDate.of(2023, 4, 30)), false));
  }

  @ParameterizedTest
  @MethodSource("containsRangeProvider")
  @DisplayName("containsRange covers boundary and non-contained scenarios")
  void containsRangeCoversBoundaryAndNonContainedScenarios(DateRange other, boolean expected) {
    DateRange base = DateRange.of(LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 14));

    assertEquals(expected, base.containsRange(other));
  }

  @Test
  void constructorValidationAndBasicGetters() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new DateRange(LocalDate.of(2020, 1, 5), LocalDate.of(2020, 1, 4)));

    IllegalArgumentException ex1 =
        assertThrows(
            IllegalArgumentException.class, () -> new DateRange(null, LocalDate.of(2020, 1, 4)));
    assertEquals("dates required", ex1.getMessage());

    IllegalArgumentException ex2 =
        assertThrows(
            IllegalArgumentException.class, () -> new DateRange(LocalDate.of(2020, 1, 4), null));
    assertEquals("dates required", ex2.getMessage());

    IllegalArgumentException ex3 =
        assertThrows(IllegalArgumentException.class, () -> new DateRange(null, null));
    assertEquals("dates required", ex3.getMessage());

    DateRange dr = DateRange.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 3));

    assertEquals(LocalDate.of(2020, 1, 1), dr.getStartDate());
    assertEquals(LocalDate.of(2020, 1, 3), dr.getEndDate());
    assertEquals(3, dr.getNumberOfDays());
  }

  @Test
  void datesAndDateAtAndIterator() {
    DateRange dr = DateRange.of(LocalDate.of(2020, 3, 30), LocalDate.of(2020, 4, 2));

    assertEquals(
        List.of(
            LocalDate.of(2020, 3, 30),
            LocalDate.of(2020, 3, 31),
            LocalDate.of(2020, 4, 1),
            LocalDate.of(2020, 4, 2)),
        dr.dates());
    assertTrue(dr.dateAt(-1).isEmpty());
    assertEquals(LocalDate.of(2020, 3, 30), dr.dateAt(0).get());
    assertEquals(LocalDate.of(2020, 4, 2), dr.dateAt(3).get());
    assertTrue(dr.dateAt(4).isEmpty());

    // iterator yields each day
    List<LocalDate> iterList = new java.util.ArrayList<>();
    for (LocalDate d : dr) {
      iterList.add(d);
    }
    assertEquals(dr.dates(), iterList);
  }

  @Test
  void datesForDayAndContainsChecks() {
    DateRange dr = DateRange.of(LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 14));

    assertTrue(
        dr.datesForDay(DayOfWeek.MONDAY).stream()
            .allMatch(d -> d.getDayOfWeek() == DayOfWeek.MONDAY));
    assertTrue(dr.containsDate(LocalDate.of(2023, 5, 1)));
    assertTrue(dr.containsDate(LocalDate.of(2023, 5, 14)));
    assertFalse(dr.containsDate(LocalDate.of(2023, 4, 30)));

    DateRange inner = DateRange.of(LocalDate.of(2023, 5, 3), LocalDate.of(2023, 5, 10));
    assertTrue(dr.containsRange(inner));
    assertFalse(inner.containsRange(dr));
  }

  @Test
  void overlapsAndOverlapsAny() {
    DateRange a = DateRange.of(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 10));
    DateRange b = DateRange.of(LocalDate.of(2023, 1, 10), LocalDate.of(2023, 1, 20));
    DateRange c = DateRange.of(LocalDate.of(2023, 1, 21), LocalDate.of(2023, 1, 30));

    assertTrue(a.overlaps(b));
    assertFalse(a.overlaps(c));
    assertTrue(a.overlapsAny(List.of(c, b)));
    assertFalse(c.overlapsAny(List.of(a)));
  }

  @Test
  void defaultPriorNextShiftByLengthAndNVariants() {
    DateRange dr = DateRange.of(LocalDate.of(2020, 6, 1), LocalDate.of(2020, 6, 7)); // 7 days

    assertEquals(LocalDate.of(2020, 5, 25), dr.prior().getStartDate());
    assertEquals(LocalDate.of(2020, 5, 31), dr.prior().getEndDate());
    assertEquals(LocalDate.of(2020, 6, 8), dr.next().getStartDate());
    assertEquals(LocalDate.of(2020, 6, 15), dr.nextN(2).getStartDate());
    assertEquals(LocalDate.of(2020, 5, 24), dr.priorN(2).getEndDate());
  }

  @Test
  void rangesBeforeAfterInclusiveAndWindowOrdering() {
    DateRange dr = DateRange.of(LocalDate.of(2022, 10, 10), LocalDate.of(2022, 10, 12));

    List<DateRange> bex = dr.rangesBefore(2);
    List<DateRange> bexi = dr.rangesBeforeInclusive(2);
    List<DateRange> aft = dr.rangesAfter(2);
    List<DateRange> afti = dr.rangesAfterInclusive(2);
    List<DateRange> win = dr.rangesWindow(2, 2);

    // before lists are in chronological order (earlier first)
    assertEquals(
        List.of(LocalDate.of(2022, 10, 4), LocalDate.of(2022, 10, 7)),
        bex.stream().map(DateRange::getStartDate).toList());
    assertEquals(
        List.of(LocalDate.of(2022, 10, 4), LocalDate.of(2022, 10, 7), LocalDate.of(2022, 10, 10)),
        bexi.stream().map(DateRange::getStartDate).toList());

    assertEquals(
        List.of(LocalDate.of(2022, 10, 13), LocalDate.of(2022, 10, 16)),
        aft.stream().map(DateRange::getStartDate).toList());
    assertEquals(
        List.of(LocalDate.of(2022, 10, 10), LocalDate.of(2022, 10, 13), LocalDate.of(2022, 10, 16)),
        afti.stream().map(DateRange::getStartDate).toList());

    assertEquals(
        List.of(
            LocalDate.of(2022, 10, 4),
            LocalDate.of(2022, 10, 7),
            LocalDate.of(2022, 10, 10),
            LocalDate.of(2022, 10, 13),
            LocalDate.of(2022, 10, 16)),
        win.stream().map(DateRange::getStartDate).toList());
  }

  @Test
  void rangesContainingSpanAndRangeContainingDateWithComposedPriorNext() {
    // 2024-01-01..2024-01-07
    DateRange weekly = WeeklyDateRange.withEndDate(LocalDate.of(2024, 1, 7));

    // rangeContainingDate navigates to the right weekly range
    assertEquals(
        LocalDate.of(2024, 1, 15),
        weekly.rangeContainingDate(LocalDate.of(2024, 1, 20)).getStartDate());

    List<DateRange> spans =
        weekly.rangesContainingSpan(LocalDate.of(2024, 1, 5), LocalDate.of(2024, 1, 20));
    assertEquals(
        List.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 8), LocalDate.of(2024, 1, 15)),
        spans.stream().map(DateRange::getStartDate).toList());
    assertEquals(
        List.of(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 14), LocalDate.of(2024, 1, 21)),
        spans.stream().map(DateRange::getEndDate).toList());
  }

  @Test
  void rangeContainingDateNavigatesUsingDefaultNextPriorNoComposition() {
    // Base range of 7 days: 2020-06-01..2020-06-07
    DateRange base = DateRange.of(LocalDate.of(2020, 6, 1), LocalDate.of(2020, 6, 7));

    // returns same range when date is inside
    DateRange same = base.rangeContainingDate(LocalDate.of(2020, 6, 6));
    assertEquals(LocalDate.of(2020, 6, 1), same.getStartDate());
    assertEquals(LocalDate.of(2020, 6, 7), same.getEndDate());

    // navigates forward one window when date is after the range
    DateRange forwardOne = base.rangeContainingDate(LocalDate.of(2020, 6, 10));
    assertEquals(LocalDate.of(2020, 6, 8), forwardOne.getStartDate());
    assertEquals(LocalDate.of(2020, 6, 14), forwardOne.getEndDate());

    // navigates backward one window when date is before the range
    DateRange backwardOne = base.rangeContainingDate(LocalDate.of(2020, 5, 28));
    assertEquals(LocalDate.of(2020, 5, 25), backwardOne.getStartDate());
    assertEquals(LocalDate.of(2020, 5, 31), backwardOne.getEndDate());

    // can hop multiple windows forward to reach far date
    DateRange far = base.rangeContainingDate(LocalDate.of(2020, 7, 1));
    assertEquals(LocalDate.of(2020, 6, 29), far.getStartDate());
    assertEquals(LocalDate.of(2020, 7, 5), far.getEndDate());
  }

  @Test
  void rangesContainingSpanUsesDefaultStepAndValidatesErrors() {
    // Base range defines 7-day windows
    DateRange base = DateRange.of(LocalDate.of(2020, 6, 1), LocalDate.of(2020, 6, 7));

    IllegalArgumentException ex1 =
        assertThrows(
            IllegalArgumentException.class,
            () -> base.rangesContainingSpan(null, LocalDate.of(2020, 6, 10)));
    assertEquals("dates required", ex1.getMessage());

    IllegalArgumentException ex2 =
        assertThrows(
            IllegalArgumentException.class,
            () -> base.rangesContainingSpan(LocalDate.of(2020, 6, 1), null));
    assertEquals("dates required", ex2.getMessage());

    IllegalArgumentException ex3 =
        assertThrows(
            IllegalArgumentException.class,
            () -> base.rangesContainingSpan(LocalDate.of(2020, 6, 10), LocalDate.of(2020, 6, 5)));
    assertEquals("to before from", ex3.getMessage());

    // span entirely inside one base window
    List<DateRange> single =
        base.rangesContainingSpan(LocalDate.of(2020, 6, 2), LocalDate.of(2020, 6, 6));
    assertEquals(
        List.of(LocalDate.of(2020, 6, 1)), single.stream().map(DateRange::getStartDate).toList());
    assertEquals(
        List.of(LocalDate.of(2020, 6, 7)), single.stream().map(DateRange::getEndDate).toList());

    // span crosses multiple windows
    List<DateRange> multi =
        base.rangesContainingSpan(LocalDate.of(2020, 6, 5), LocalDate.of(2020, 6, 20));
    assertEquals(
        List.of(LocalDate.of(2020, 6, 1), LocalDate.of(2020, 6, 8), LocalDate.of(2020, 6, 15)),
        multi.stream().map(DateRange::getStartDate).toList());
    assertEquals(
        List.of(LocalDate.of(2020, 6, 7), LocalDate.of(2020, 6, 14), LocalDate.of(2020, 6, 21)),
        multi.stream().map(DateRange::getEndDate).toList());
  }

  @Test
  void equalsHashCodeCompareTo() {
    DateRange a1a = DateRange.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
    DateRange a1b = DateRange.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
    DateRange a2 = DateRange.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 3));
    DateRange b = DateRange.of(LocalDate.of(2020, 1, 2), LocalDate.of(2020, 1, 3));
    DateRange c = DateRange.of(LocalDate.of(2019, 12, 31), LocalDate.of(2020, 1, 3));

    assertTrue(a1a.equals(a1a));
    assertTrue(a1a.equals(a1b));
    assertFalse(a1a.equals(a2));
    assertFalse(a1a.equals(c));
    assertFalse(a1a.equals(null));
    assertFalse(a1a.equals("abc"));

    assertEquals(a1a.hashCode(), a1b.hashCode());
    assertTrue(a1a.hashCode() != a2.hashCode());
    assertEquals(0, a1a.compareTo(a1a));
    assertEquals(0, a1a.compareTo(a1b));
    assertTrue(a1a.compareTo(b) < 0);
    assertTrue(a1a.compareTo(c) > 0);
  }
}
