package com.stano.datetime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DateTimeConstantsTest {

  @Test
  void verifyTheConstantsAreCorrect() {
    assertEquals(1000, DateTimeConstants.MILLIS_PER_SECOND);
    assertEquals(1000 * 60, DateTimeConstants.MILLIS_PER_MINUTE);
    assertEquals(1000 * 60 * 60, DateTimeConstants.MILLIS_PER_HOUR);
    assertEquals(1000 * 60 * 60 * 24, DateTimeConstants.MILLIS_PER_DAY);
    assertEquals(1000 * 60 * 60 * 24 * 7, DateTimeConstants.MILLIS_PER_WEEK);

    assertEquals(60, DateTimeConstants.SECONDS_PER_MINUTE);
    assertEquals(60 * 60, DateTimeConstants.SECONDS_PER_HOUR);
    assertEquals(60 * 60 * 24, DateTimeConstants.SECONDS_PER_DAY);
    assertEquals(60 * 60 * 24 * 7, DateTimeConstants.SECONDS_PER_WEEK);

    assertEquals(60, DateTimeConstants.MINUTES_PER_HOUR);
    assertEquals(60 * 24, DateTimeConstants.MINUTES_PER_DAY);
    assertEquals(60 * 24 * 7, DateTimeConstants.MINUTES_PER_WEEK);

    assertEquals(24, DateTimeConstants.HOURS_PER_DAY);
    assertEquals(24 * 7, DateTimeConstants.HOURS_PER_WEEK);

    assertEquals(7, DateTimeConstants.DAYS_PER_WEEK);
  }

  @Test
  void callThePrivateConstructorToGet100PercentCoverage() throws ReflectiveOperationException {
    java.lang.reflect.Constructor<DateTimeConstants> constructor =
        DateTimeConstants.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    assertNotNull(constructor.newInstance());
  }
}
