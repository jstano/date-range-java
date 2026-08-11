package com.stano.datetime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DTUtilTest {

  static Stream<Arguments> earliestProvider() {
    return Stream.of(
        Arguments.of(null, null, null),
        Arguments.of(ldt(2014, 9, 23, 10, 0), null, ldt(2014, 9, 23, 10, 0)),
        Arguments.of(null, ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 0)),
        Arguments.of(ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 0)),
        Arguments.of(ldt(2014, 9, 23, 10, 0), ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 0)));
  }

  @ParameterizedTest
  @MethodSource("earliestProvider")
  @DisplayName("minimum of two times, null iff both are null")
  void minimumOfTwoTimesNullIffBothAreNull(
      LocalDateTime time1, LocalDateTime time2, LocalDateTime earliestTime) {
    assertEquals(earliestTime, DTUtil.earliest(time1, time2));
  }

  static Stream<Arguments> latestProvider() {
    return Stream.of(
        Arguments.of(null, null, null),
        Arguments.of(ldt(2014, 9, 23, 10, 0), null, ldt(2014, 9, 23, 10, 0)),
        Arguments.of(null, ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 0)),
        Arguments.of(ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 9, 0)),
        Arguments.of(ldt(2014, 9, 23, 10, 0), ldt(2014, 9, 23, 9, 0), ldt(2014, 9, 23, 10, 0)));
  }

  @ParameterizedTest
  @MethodSource("latestProvider")
  @DisplayName("maximum of two times, null if both are null")
  void maximumOfTwoTimesNullIfBothAreNull(
      LocalDateTime time1, LocalDateTime time2, LocalDateTime latestTime) {
    assertEquals(latestTime, DTUtil.latest(time1, time2));
  }

  static Stream<Arguments> durationInHoursProvider() {
    return Stream.of(
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 16, 0), 8),
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 16, 30), 8),
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 17, 0), 9),
        Arguments.of(ldt(2015, 2, 7, 0, 0), ldt(2015, 2, 8, 0, 0), 24),
        Arguments.of(ldt(2015, 2, 7, 0, 0), ldt(2015, 2, 9, 0, 0), 48),
        Arguments.of(ldt(2015, 2, 7, 16, 0), ldt(2015, 2, 7, 8, 0), -8),
        Arguments.of(ldt(2015, 2, 7, 16, 30), ldt(2015, 2, 7, 8, 0), -8),
        Arguments.of(ldt(2015, 2, 7, 17, 0), ldt(2015, 2, 7, 8, 0), -9));
  }

  @ParameterizedTest
  @MethodSource("durationInHoursProvider")
  @DisplayName("durationInHours should work")
  void durationInHoursShouldWork(
      LocalDateTime startDateTime, LocalDateTime endDateTime, int expectedResult) {
    assertEquals(expectedResult, DTUtil.durationInHours(startDateTime, endDateTime));
  }

  static Stream<Arguments> durationInMinutesProvider() {
    return Stream.of(
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 16, 0), 480),
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 16, 30), 510),
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 17, 0), 540),
        Arguments.of(ldt(2015, 2, 7, 0, 0), ldt(2015, 2, 8, 0, 0), 1_440),
        Arguments.of(ldt(2015, 2, 7, 0, 0), ldt(2015, 2, 9, 0, 0), 2_880),
        Arguments.of(ldt(2015, 2, 7, 16, 0), ldt(2015, 2, 7, 8, 0), -480),
        Arguments.of(ldt(2015, 2, 7, 16, 30), ldt(2015, 2, 7, 8, 0), -510),
        Arguments.of(ldt(2015, 2, 7, 17, 0), ldt(2015, 2, 7, 8, 0), -540));
  }

  @ParameterizedTest
  @MethodSource("durationInMinutesProvider")
  @DisplayName("durationInMinutes should work")
  void durationInMinutesShouldWork(
      LocalDateTime startDateTime, LocalDateTime endDateTime, int expectedResult) {
    assertEquals(expectedResult, DTUtil.durationInMinutes(startDateTime, endDateTime));
  }

  static Stream<Arguments> durationInSecondsProvider() {
    return Stream.of(
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 16, 0), 28_800),
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 16, 30), 30_600),
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 17, 0), 32_400),
        Arguments.of(ldt(2015, 2, 7, 0, 0), ldt(2015, 2, 8, 0, 0), 86_400),
        Arguments.of(ldt(2015, 2, 7, 0, 0), ldt(2015, 2, 9, 0, 0), 172_800),
        Arguments.of(ldt(2015, 2, 7, 16, 0), ldt(2015, 2, 7, 8, 0), -28_800),
        Arguments.of(ldt(2015, 2, 7, 16, 30), ldt(2015, 2, 7, 8, 0), -30_600),
        Arguments.of(ldt(2015, 2, 7, 17, 0), ldt(2015, 2, 7, 8, 0), -32_400));
  }

  @ParameterizedTest
  @MethodSource("durationInSecondsProvider")
  @DisplayName("durationInSeconds should work")
  void durationInSecondsShouldWork(
      LocalDateTime startDateTime, LocalDateTime endDateTime, int expectedResult) {
    assertEquals(expectedResult, DTUtil.durationInSeconds(startDateTime, endDateTime));
  }

  static Stream<Arguments> durationInFractionalSecondsProvider() {
    return Stream.of(
        Arguments.of(millis(0), millis(0), new java.math.BigDecimal("0")),
        Arguments.of(millis(0), millis(1), new java.math.BigDecimal("0.001")),
        Arguments.of(millis(0), millis(10), new java.math.BigDecimal("0.01")),
        Arguments.of(millis(0), millis(100), new java.math.BigDecimal("0.1")),
        Arguments.of(millis(0), seconds(1), new java.math.BigDecimal("1")));
  }

  @ParameterizedTest
  @MethodSource("durationInFractionalSecondsProvider")
  @DisplayName("durationInFractionalSeconds should work")
  void durationInFractionalSecondsShouldWork(
      LocalDateTime startDateTime, LocalDateTime endDateTime, java.math.BigDecimal expectedResult) {
    assertEquals(
        0,
        DTUtil.durationInFractionalSeconds(startDateTime, endDateTime).compareTo(expectedResult));
  }

  static Stream<Arguments> durationInFractionalHoursProvider() {
    return Stream.of(
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 16, 0), 8.0),
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 16, 30), 8.5),
        Arguments.of(ldt(2015, 2, 7, 8, 0), ldt(2015, 2, 7, 17, 0), 9.0),
        Arguments.of(ldt(2015, 2, 7, 0, 0), ldt(2015, 2, 8, 0, 0), 24.0),
        Arguments.of(ldt(2015, 2, 7, 0, 0), ldt(2015, 2, 9, 0, 0), 48.0),
        Arguments.of(ldt(2015, 2, 7, 16, 0), ldt(2015, 2, 7, 8, 0), -8.0),
        Arguments.of(ldt(2015, 2, 7, 16, 30), ldt(2015, 2, 7, 8, 0), -8.5),
        Arguments.of(ldt(2015, 2, 7, 17, 0), ldt(2015, 2, 7, 8, 0), -9.0));
  }

  @ParameterizedTest
  @MethodSource("durationInFractionalHoursProvider")
  @DisplayName("durationInFractionalHours should work")
  void durationInFractionalHoursShouldWork(
      LocalDateTime startDateTime, LocalDateTime endDateTime, double expectedResult) {
    assertEquals(expectedResult, DTUtil.durationInFractionalHours(startDateTime, endDateTime));
  }

  @Test
  @DisplayName("call the private constructor to get 100% coverage")
  void callThePrivateConstructorToGet100PercentCoverage() throws ReflectiveOperationException {
    Constructor<DTUtil> constructor = DTUtil.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    assertNotNull(constructor.newInstance());
  }

  private static LocalDateTime ldt(int year, int month, int day, int hour, int minute) {
    return LocalDate.of(year, month, day).atTime(hour, minute, 0);
  }

  private static LocalDateTime seconds(int seconds) {
    return LocalDateTime.of(2018, 10, 6, 1, 0, seconds);
  }

  private static LocalDateTime millis(int millis) {
    return LocalDateTime.of(2018, 10, 6, 1, 0, 0, millis * DateTimeConstants.NANOS_PER_MILLI);
  }
}
