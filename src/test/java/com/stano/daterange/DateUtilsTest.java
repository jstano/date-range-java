package com.stano.daterange;

import com.stano.datetime.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilsTest {

  @Test
  void firstAndLastDayOfMonthPlusAddSubMonthsYears() {
    assertEquals(LocalDate.of(2023, 2, 1), DateUtils.firstDayOfMonth(LocalDate.of(2023, 2, 15)));
    assertEquals(LocalDate.of(2023, 2, 28), DateUtils.lastDayOfMonth(LocalDate.of(2023, 2, 2)));

    assertEquals(LocalDate.of(2023, 2, 28), DateUtils.addMonths(LocalDate.of(2023, 1, 31), 1));
    assertEquals(LocalDate.of(2023, 2, 28), DateUtils.subtractMonths(LocalDate.of(2023, 3, 31), 1));

    assertEquals(LocalDate.of(2021, 2, 28), DateUtils.addYears(LocalDate.of(2020, 2, 29), 1));
    assertEquals(LocalDate.of(2020, 3, 1), DateUtils.subtractYears(LocalDate.of(2021, 3, 1), 1));
  }
}
