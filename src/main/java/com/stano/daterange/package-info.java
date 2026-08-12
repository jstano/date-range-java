/**
 * Immutable date range types and factory helpers.
 *
 * <p>{@link com.stano.daterange.DateRange} is an inclusive range of dates, iterable over its days,
 * with prior/next navigation, containment, and overlap helpers. The {@code *DateRange} factory
 * classes ({@link com.stano.daterange.WeeklyDateRange}, {@link
 * com.stano.daterange.BiWeeklyDateRange}, {@link com.stano.daterange.SemiMonthlyDateRange}, {@link
 * com.stano.daterange.MonthlyDateRange}, {@link com.stano.daterange.QuarterlyDateRange}, {@link
 * com.stano.daterange.SemiAnnualDateRange}, {@link com.stano.daterange.AnnualDateRange}) build
 * {@code DateRange} instances whose prior/next navigation follows the appropriate calendar-based
 * rules for that period type.
 */
package com.stano.daterange;
