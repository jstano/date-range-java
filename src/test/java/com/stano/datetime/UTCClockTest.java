package com.stano.datetime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UTCClockTest {

  @Test
  void methodsShouldReturnValues() {
    UTCClock clock = new UTCClock();

    assertNotNull(clock.currentDate());
    assertNotNull(clock.currentDateTime());
    assertNotNull(clock.currentTime());
  }
}
