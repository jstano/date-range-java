/**
 * Clock abstractions and date/time conversion and arithmetic helpers used throughout the library.
 *
 * <p>{@link com.stano.datetime.Clock} and its implementations ({@link com.stano.datetime.UTCClock},
 * {@link com.stano.datetime.ConstantClock}) and the {@link com.stano.datetime.DateTimeProvider}
 * facade provide access to the current date and time. {@link com.stano.datetime.DateUtils}, {@link
 * com.stano.datetime.DTUtil}, and {@link com.stano.datetime.JavaTimeUtil} provide date/time
 * arithmetic and conversions between {@code java.time} and legacy {@code java.util.Date}/{@code
 * java.sql.*} types. {@link com.stano.datetime.DateTimeConstants} holds numeric time-unit
 * conversion constants.
 */
package com.stano.datetime;
