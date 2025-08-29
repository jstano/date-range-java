package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.daterange.DateUtils.*;

public final class QuarterlyDateRange {
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate start = firstDayOfMonth(startDate);
    LocalDate end = lastDayOfMonth(addMonths(firstDayOfMonth(startDate), 2));
    return DateRange.ofWithPriorNext(start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate start = subtractMonths(firstDayOfMonth(endDate), 2);
    LocalDate end = lastDayOfMonth(endDate);
    return DateRange.ofWithPriorNext(start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  static DateRange prior(DateRange dr) {
    LocalDate start = subtractMonths(dr.startDate(), 3);
    LocalDate end = lastDayOfMonth(subtractMonths(firstDayOfMonth(dr.endDate()), 3));
    return DateRange.ofWithPriorNext(start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  static DateRange next(DateRange dr) {
    LocalDate start = addMonths(dr.startDate(), 3);
    LocalDate end = lastDayOfMonth(addMonths(firstDayOfMonth(dr.endDate()), 3));
    return DateRange.ofWithPriorNext(start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  private QuarterlyDateRange() {
  }
}
