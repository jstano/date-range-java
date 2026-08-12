/**
 * Immutable date-time range types combining a date and a time-of-day range.
 *
 * <p>{@link com.stano.datetimerange.DateTimeRange} is an inclusive range of date-times, with
 * containment/overlap helpers and factories for building a range from a {@link
 * com.stano.timerange.TimeRange} applied to a date, or spanning a whole day. {@link
 * com.stano.datetimerange.DateTimeRangeWithPeriodLength} pairs a {@code DateTimeRange} with a fixed
 * period length in minutes, and {@link com.stano.datetimerange.DateTimeRangeIterator} iterates over
 * a range's date-times in fixed-length steps.
 */
package com.stano.datetimerange;
