package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.datetime.DateUtils.addYears;
import static com.stano.datetime.DateUtils.subtractYears;

public final class AnnualDateRange {
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate endDate = endForStart(startDate);
    return DateRange.ofWithPriorNext(startDate, endDate, AnnualDateRange::prior, AnnualDateRange::next);
  }

  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate startDate = subtractYears(endDate, 1).plusDays(1);
    return DateRange.ofWithPriorNext(startDate, endDate, AnnualDateRange::prior, AnnualDateRange::next);
  }

  static DateRange prior(DateRange dr) {
    LocalDate start = subtractYears(dr.startDate(), 1);
    LocalDate end = endForStart(start);
    return DateRange.ofWithPriorNext(start, end, AnnualDateRange::prior, AnnualDateRange::next);
  }

  static DateRange next(DateRange dr) {
    LocalDate start = addYears(dr.startDate(), 1);
    LocalDate end = endForStart(start);
    return DateRange.ofWithPriorNext(start, end, AnnualDateRange::prior, AnnualDateRange::next);
  }

  private static LocalDate endForStart(LocalDate startDate) {
    // Special-case Feb 29 starts: end at Feb 28 next year
    if (startDate.getMonthValue() == 2 && startDate.getDayOfMonth() == 29) {
      return LocalDate.of(startDate.getYear() + 1, 2, 28);
    }
    else {
      return addYears(startDate, 1).minusDays(1);
    }
  }

  private AnnualDateRange() {
  }
}
