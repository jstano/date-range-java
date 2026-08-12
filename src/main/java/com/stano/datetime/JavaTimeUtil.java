package com.stano.datetime;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Conversion helpers between the legacy {@code java.util.Date}/{@code java.sql.*} types and {@code
 * java.time} types, and between US-style day numbers and {@link DayOfWeek}.
 *
 * <p>All conversions involving {@code java.util.Date}/{@code java.sql.*} types are performed using
 * the UTC time zone. This class is not instantiable.
 */
public final class JavaTimeUtil {
  /** The UTC time zone used by all conversions in this class. */
  public static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

  private static final int[] US_DAY_NUMBER_MAP = {2, 3, 4, 5, 6, 7, 1};

  /**
   * Converts a legacy Date to a LocalDate, interpreting the Date's instant in the UTC time zone.
   *
   * @param date The Date to convert.
   * @return The equivalent LocalDate in UTC.
   */
  public static LocalDate javaDateToLocalDate(Date date) {
    Instant instant = Instant.ofEpochMilli(date.getTime());

    return LocalDateTime.ofInstant(instant, UTC_ZONE_ID).toLocalDate();
  }

  /**
   * Converts a legacy Date to a LocalDateTime, interpreting the Date's instant in the UTC time
   * zone.
   *
   * @param date The Date to convert.
   * @return The equivalent LocalDateTime in UTC.
   */
  public static LocalDateTime javaDateToLocalDateTime(Date date) {
    Instant instant = Instant.ofEpochMilli(date.getTime());

    return LocalDateTime.ofInstant(instant, UTC_ZONE_ID);
  }

  /**
   * Converts a legacy Date to a LocalTime, interpreting the Date's instant in the UTC time zone.
   *
   * @param date The Date to convert.
   * @return The equivalent LocalTime in UTC.
   */
  public static LocalTime javaDateToLocalTime(Date date) {
    Instant instant = Instant.ofEpochMilli(date.getTime());

    return LocalDateTime.ofInstant(instant, UTC_ZONE_ID).toLocalTime();
  }

  /**
   * Converts a {@code java.sql.Date} to a LocalDate, interpreting the value in the UTC time zone.
   *
   * @param date The sql Date to convert.
   * @return The equivalent LocalDate in UTC.
   */
  public static LocalDate sqlDateToLocalDate(java.sql.Date date) {
    return javaDateToLocalDate(date);
  }

  /**
   * Converts a {@code java.sql.Time} to a LocalTime, interpreting the value in the UTC time zone.
   *
   * @param time The sql Time to convert.
   * @return The equivalent LocalTime in UTC.
   */
  public static LocalTime sqlTimeToLocalTime(Time time) {
    return javaDateToLocalTime(time);
  }

  /**
   * Converts a {@code java.sql.Timestamp} to a LocalDateTime, interpreting the value in the UTC
   * time zone.
   *
   * @param timestamp The sql Timestamp to convert.
   * @return The equivalent LocalDateTime in UTC.
   */
  public static LocalDateTime sqlTimestampToLocalDateTime(Timestamp timestamp) {
    return javaDateToLocalDateTime(timestamp);
  }

  /**
   * Converts a LocalDate to a legacy Date, treating the LocalDate as midnight in the UTC time zone.
   *
   * @param localDate The LocalDate to convert.
   * @return The equivalent Date.
   */
  public static Date localDateToJavaDate(LocalDate localDate) {
    return new Date(localDate.atStartOfDay(UTC_ZONE_ID).toInstant().toEpochMilli());
  }

  /**
   * Converts a LocalDateTime to a legacy Date, treating the LocalDateTime as being in the UTC time
   * zone.
   *
   * @param localDateTime The LocalDateTime to convert.
   * @return The equivalent Date.
   */
  public static Date localDateTimeToJavaDate(LocalDateTime localDateTime) {
    return new Date(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
  }

  /**
   * Converts a LocalDate to a {@code java.sql.Date}, treating the LocalDate as midnight in the UTC
   * time zone.
   *
   * @param localDate The LocalDate to convert.
   * @return The equivalent sql Date.
   */
  public static java.sql.Date localDateToSqlDate(LocalDate localDate) {
    return new java.sql.Date(localDate.atStartOfDay(UTC_ZONE_ID).toInstant().toEpochMilli());
  }

  /**
   * Converts a LocalDateTime to a {@code java.sql.Timestamp}, treating the LocalDateTime as being
   * in the UTC time zone.
   *
   * @param localDateTime The LocalDateTime to convert.
   * @return The equivalent sql Timestamp.
   */
  public static Timestamp localDateTimeToSqlTimestamp(LocalDateTime localDateTime) {
    return new Timestamp(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
  }

  /**
   * Converts a LocalTime to a {@code java.sql.Time}, treating the LocalTime as occurring on the
   * epoch date in the UTC time zone.
   *
   * @param localTime The LocalTime to convert.
   * @return The equivalent sql Time.
   */
  public static Time localTimeToSqlTime(LocalTime localTime) {
    return new Time(
        localTime.atDate(LocalDate.ofEpochDay(0)).toInstant(ZoneOffset.UTC).toEpochMilli());
  }

  /**
   * Converts a LocalDateTime from one time zone to the equivalent LocalDateTime in another time
   * zone.
   *
   * @param localDateTime The LocalDateTime to convert.
   * @param fromZoneId The time zone that localDateTime is expressed in.
   * @param toZoneId The time zone to convert localDateTime into.
   * @return The equivalent LocalDateTime in toZoneId.
   */
  public static LocalDateTime toLocalDateTimeAtZone(
      LocalDateTime localDateTime, ZoneId fromZoneId, ZoneId toZoneId) {
    return localDateTime.atZone(fromZoneId).withZoneSameInstant(toZoneId).toLocalDateTime();
  }

  /**
   * Converts a US-style day number (1=Sunday .. 7=Saturday) to a DayOfWeek.
   *
   * @param usDayNumber The US-style day number, from 1 (Sunday) to 7 (Saturday).
   * @return The equivalent DayOfWeek.
   * @throws IllegalArgumentException if usDayNumber is not between 1 and 7.
   */
  public static DayOfWeek usDayNumberToDayOfWeek(int usDayNumber) {
    return switch (usDayNumber) {
      case 1 -> DayOfWeek.SUNDAY;
      case 2 -> DayOfWeek.MONDAY;
      case 3 -> DayOfWeek.TUESDAY;
      case 4 -> DayOfWeek.WEDNESDAY;
      case 5 -> DayOfWeek.THURSDAY;
      case 6 -> DayOfWeek.FRIDAY;
      case 7 -> DayOfWeek.SATURDAY;
      default ->
          throw new IllegalArgumentException(String.format("Invalid day number: %d", usDayNumber));
    };
  }

  /**
   * Converts a DayOfWeek to a US-style day number (1=Sunday .. 7=Saturday).
   *
   * @param dayOfWeek The DayOfWeek to convert.
   * @return The equivalent US-style day number, from 1 (Sunday) to 7 (Saturday).
   */
  public static int dayOfWeekToUsDayNumber(DayOfWeek dayOfWeek) {
    int isoDayNumber = dayOfWeek.getValue();

    return US_DAY_NUMBER_MAP[isoDayNumber - 1];
  }

  /**
   * Gets a new Calendar instance set to the UTC time zone.
   *
   * @return A new Calendar in the UTC time zone.
   */
  public static Calendar getUTCCalender() {
    return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
  }

  private JavaTimeUtil() {}
}
