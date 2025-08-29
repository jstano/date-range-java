package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.daterange.DateUtils.addMonths;
import static com.stano.daterange.DateUtils.subtractMonths;

public final class SemiAnnualDateRange {
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate endDate = addMonths(startDate, 6).minusDays(1);
    return DateRange.ofWithPriorNext(startDate, endDate, SemiAnnualDateRange::prior, SemiAnnualDateRange::next);
  }

  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate startDate = subtractMonths(endDate, 6).plusDays(1);
    return DateRange.ofWithPriorNext(startDate, endDate, SemiAnnualDateRange::prior, SemiAnnualDateRange::next);
  }

  static DateRange prior(DateRange dr) {
    LocalDate start = subtractMonths(dr.startDate(), 6);
    LocalDate end = subtractMonths(dr.endDate(), 6);
    return DateRange.ofWithPriorNext(start, end, SemiAnnualDateRange::prior, SemiAnnualDateRange::next);
  }

  static DateRange next(DateRange dr) {
    LocalDate start = addMonths(dr.startDate(), 6);
    LocalDate end = addMonths(dr.endDate(), 6);
    return DateRange.ofWithPriorNext(start, end, SemiAnnualDateRange::prior, SemiAnnualDateRange::next);
  }

  private SemiAnnualDateRange() {
  }
}
