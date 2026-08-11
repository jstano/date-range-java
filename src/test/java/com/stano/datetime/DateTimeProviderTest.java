package com.stano.datetime;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeProviderTest {

  @Test
  void methodsShouldReturnValues() throws ReflectiveOperationException {
    LocalDateTime now = LocalDateTime.of(2015, 8, 21, 8, 21, 17, 45);
    setClock(ConstantClock.of(now));

    assertEquals(now.toLocalDate(), DateTimeProvider.currentDate());
    assertEquals(now, DateTimeProvider.currentDateTime());
    assertEquals(now.toLocalTime(), DateTimeProvider.currentTime());
  }

  // DateTimeProvider.clock is a private static field with no production setter;
  // reflection mirrors how the original Spock spec swapped it out for a fixed clock.
  private static void setClock(Clock clock) throws ReflectiveOperationException {
    Field field = DateTimeProvider.class.getDeclaredField("clock");
    field.setAccessible(true);
    field.set(null, clock);
  }
}
