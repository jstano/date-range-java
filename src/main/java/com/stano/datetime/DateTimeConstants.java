package com.stano.datetime;

/**
 * Holds numeric constants for converting between common units of time (nanoseconds, milliseconds,
 * seconds, minutes, hours, days, weeks).
 *
 * <p>This class is not instantiable.
 */
public final class DateTimeConstants {

  /** The number of nanoseconds in one second. */
  public static final long NANOS_PER_SECOND = 1_000_000_000L;

  /** The number of nanoseconds in one millisecond. */
  public static final int NANOS_PER_MILLI = 1_000_000;

  /** The number of milliseconds in one second. */
  public static final int MILLIS_PER_SECOND = 1000;

  /** The number of milliseconds in one minute. */
  public static final int MILLIS_PER_MINUTE = 60000;

  /** The number of milliseconds in one hour. */
  public static final int MILLIS_PER_HOUR = 3600000;

  /** The number of milliseconds in one day. */
  public static final int MILLIS_PER_DAY = 86400000;

  /** The number of milliseconds in one week. */
  public static final int MILLIS_PER_WEEK = 604800000;

  /** The number of seconds in one minute. */
  public static final int SECONDS_PER_MINUTE = 60;

  /** The number of seconds in one hour. */
  public static final int SECONDS_PER_HOUR = 3600;

  /** The number of seconds in one day. */
  public static final int SECONDS_PER_DAY = 86400;

  /** The number of seconds in one week. */
  public static final int SECONDS_PER_WEEK = 604800;

  /** The number of minutes in one hour. */
  public static final int MINUTES_PER_HOUR = 60;

  /** The number of minutes in one day. */
  public static final int MINUTES_PER_DAY = 1440;

  /** The number of minutes in one week. */
  public static final int MINUTES_PER_WEEK = 10080;

  /** The number of hours in one day. */
  public static final int HOURS_PER_DAY = 24;

  /** The number of hours in one week. */
  public static final int HOURS_PER_WEEK = 168;

  /** The number of days in one week. */
  public static final int DAYS_PER_WEEK = 7;

  private DateTimeConstants() {}
}
