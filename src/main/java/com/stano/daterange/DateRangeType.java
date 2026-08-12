package com.stano.daterange;

/**
 * Identifies a recurring calendar period type used by {@link DateRange} and its factory helper
 * classes, along with how many such periods occur in a year.
 */
public enum DateRangeType {
  /** A 7-day recurring period; 52 periods per year. */
  WEEKLY(52),

  /** A 14-day recurring period; 26 periods per year. */
  BI_WEEKLY(26),

  /** A twice-monthly recurring period (1st-15th and 16th-end of month); 24 periods per year. */
  SEMI_MONTHLY(24),

  /** A calendar-month recurring period; 12 periods per year. */
  MONTHLY(12);

  /**
   * Gets the number of periods of this type that occur in a year.
   *
   * @return The number of periods per year.
   */
  public int getPeriodsPerYear() {
    return periodsPerYear;
  }

  /**
   * Checks if this period type is based on a fixed number of weeks (as opposed to calendar months).
   *
   * @return true if this type is {@link #WEEKLY} or {@link #BI_WEEKLY}.
   */
  public boolean isWeekBased() {
    return this == WEEKLY || this == BI_WEEKLY;
  }

  DateRangeType(int periodsPerYear) {
    this.periodsPerYear = periodsPerYear;
  }

  private final int periodsPerYear;
}
