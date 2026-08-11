package com.stano.timerange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TimeRangeTest {

  @Test
  void testWithTimes() {
    LocalTime startTime = LocalTime.of(8, 30, 17);
    LocalTime endTime = LocalTime.of(17, 45, 38);
    TimeRange timeRange = TimeRange.of(startTime, endTime);

    assertEquals(startTime, timeRange.getStartTime());
    assertEquals(endTime, timeRange.getEndTime());
  }

  @Test
  void testGetDuration() {
    LocalTime startTime = LocalTime.of(8, 0, 0);
    LocalTime endTime = LocalTime.of(16, 0, 0);
    TimeRange timeRange = TimeRange.of(startTime, endTime);

    assertEquals(28800000, timeRange.getDuration().toMillis());
  }

  @Test
  void testGetDurationNegative() {
    LocalTime startTime = LocalTime.of(16, 0, 0);
    LocalTime endTime = LocalTime.of(8, 0, 0);
    TimeRange timeRange = TimeRange.of(startTime, endTime);

    assertEquals(-28800000, timeRange.getDuration().toMillis());
  }

  @Test
  void testEquals() {
    TimeRange timeRange1 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(17, 45, 38));
    TimeRange timeRange2 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(17, 45, 38));
    TimeRange timeRange3 = TimeRange.of(LocalTime.of(9, 30, 17), LocalTime.of(17, 45, 38));
    TimeRange timeRange4 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(15, 17, 45));
    TimeRange timeRange5 = TimeRange.of(LocalTime.of(8, 17, 14), LocalTime.of(12, 18, 54));
    TimeRange timeRange6 = TimeRange.of(LocalTime.of(23, 0, 0), LocalTime.MIDNIGHT);
    TimeRange timeRange7 = TimeRange.of(LocalTime.of(23, 0, 0), LocalTime.MIDNIGHT);

    assertFalse(timeRange1.equals(null));
    assertFalse(timeRange1.equals("ABC"));
    assertTrue(timeRange1.equals(timeRange1));
    assertTrue(timeRange1.equals(timeRange2));
    assertFalse(timeRange1.equals(timeRange3));
    assertFalse(timeRange1.equals(timeRange4));
    assertFalse(timeRange1.equals(timeRange5));
    assertTrue(timeRange6.equals(timeRange7));
  }

  @Test
  void testHashCode() {
    LocalTime startTime = LocalTime.of(8, 17, 48);
    LocalTime endTime = LocalTime.of(17, 45, 38);
    TimeRange timeRange = TimeRange.of(startTime, endTime);

    int expectedHashCode = 31 * startTime.hashCode() + endTime.hashCode();

    assertEquals(expectedHashCode, timeRange.hashCode());
  }

  static Stream<Arguments> overlapsProvider() {
    return Stream.of(
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)),
            true,
            tr(lt(2, 0), lt(22, 0)),
            "checked range spans before and after"),
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)),
            true,
            tr(lt(8, 0), lt(16, 0)),
            "checked range exactly matches"),
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)), false, tr(lt(2, 0), lt(4, 0)), "checked range before"),
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)), true, tr(lt(2, 0), lt(10, 0)), "checked range overlaps start"),
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)), true, tr(lt(10, 0), lt(14, 0)), "checked range inside"),
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)), true, tr(lt(10, 0), lt(22, 0)), "checked range overlaps end"),
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)), false, tr(lt(17, 0), lt(22, 0)), "checked range after"),
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)),
            true,
            tr(lt(2, 0), LocalTime.MIDNIGHT),
            "checked range spans before and after with Midnight"),
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)),
            true,
            tr(lt(10, 0), LocalTime.MIDNIGHT),
            "checked range spans end with Midnight"),
        Arguments.of(
            tr(lt(8, 0), lt(16, 0)),
            false,
            tr(lt(17, 0), LocalTime.MIDNIGHT),
            "checked range is after with Midnight"),
        Arguments.of(
            tr(lt(8, 0), LocalTime.MIDNIGHT),
            true,
            tr(lt(2, 0), LocalTime.MIDNIGHT),
            "range with Midnight end - checked range starts before and ends at Midnight"),
        Arguments.of(
            tr(lt(8, 0), LocalTime.MIDNIGHT),
            true,
            tr(lt(8, 0), LocalTime.MIDNIGHT),
            "range with Midnight end - checked range exactly matches"),
        Arguments.of(
            tr(lt(8, 0), LocalTime.MIDNIGHT),
            false,
            tr(lt(2, 0), lt(4, 0)),
            "range with Midnight end - checked range before"),
        Arguments.of(
            tr(lt(8, 0), LocalTime.MIDNIGHT),
            true,
            tr(lt(2, 0), lt(10, 0)),
            "range with Midnight end - checked range overlaps start"),
        Arguments.of(
            tr(lt(8, 0), LocalTime.MIDNIGHT),
            true,
            tr(lt(10, 0), lt(14, 0)),
            "range with Midnight end - checked range inside"),
        Arguments.of(
            tr(lt(8, 0), LocalTime.MIDNIGHT),
            true,
            tr(lt(10, 0), LocalTime.MIDNIGHT),
            "range with Midnight end - checked range starts inside and ends at Midnight"),
        Arguments.of(tr(lt(8, 0), LocalTime.MIDNIGHT), false, null, "null"));
  }

  @ParameterizedTest(name = "{3}")
  @MethodSource("overlapsProvider")
  void testOverlaps(
      TimeRange timeRange, boolean overlapsWith, TimeRange timeRangeToCheck, String scenario) {
    assertEquals(overlapsWith, timeRange.overlaps(timeRangeToCheck));
  }

  @Test
  void testCompareTo() {
    TimeRange timeRange1 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(17, 45, 38));
    TimeRange timeRange2 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(17, 45, 38));
    TimeRange timeRange3 = TimeRange.of(LocalTime.of(6, 30, 17), LocalTime.of(17, 45, 38));
    TimeRange timeRange4 = TimeRange.of(LocalTime.of(9, 30, 17), LocalTime.of(15, 17, 45));
    TimeRange timeRange5 = TimeRange.of(LocalTime.of(6, 17, 14), LocalTime.of(12, 18, 54));
    TimeRange timeRange6 = TimeRange.of(LocalTime.of(8, 30, 17), LocalTime.of(19, 18, 54));

    assertEquals(0, timeRange1.compareTo(timeRange1));
    assertEquals(0, timeRange1.compareTo(timeRange2));
    assertEquals(-1, timeRange1.compareTo(null));
    assertTrue(timeRange1.compareTo(timeRange3) == 1);
    assertTrue(timeRange1.compareTo(timeRange4) == -1);
    assertTrue(timeRange1.compareTo(timeRange5) == 1);
    assertTrue(timeRange1.compareTo(timeRange6) == -1);
  }

  static LocalTime lt(int hour, int minute) {
    return LocalTime.of(hour, minute);
  }

  static TimeRange tr(LocalTime startTime, LocalTime endTime) {
    return TimeRange.of(startTime, endTime);
  }
}
