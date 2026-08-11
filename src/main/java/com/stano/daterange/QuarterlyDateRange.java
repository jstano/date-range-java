package com.stano.daterange;

import java.time.LocalDate;

import static com.stano.datetime.DateUtils.*;

public final class QuarterlyDateRange {
  public static DateRange withStartDate(LocalDate startDate) {
    LocalDate start = firstDayOfMonth(startDate);
    LocalDate end = lastDayOfMonth(addMonths(firstDayOfMonth(startDate), 2));
    return DateRange.ofWithPriorNext(
        start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  public static DateRange withEndDate(LocalDate endDate) {
    LocalDate start = subtractMonths(firstDayOfMonth(endDate), 2);
    LocalDate end = lastDayOfMonth(endDate);
    return DateRange.ofWithPriorNext(
        start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  static DateRange prior(DateRange dr) {
    LocalDate start = subtractMonths(dr.getStartDate(), 3);
    LocalDate end = lastDayOfMonth(subtractMonths(firstDayOfMonth(dr.getEndDate()), 3));
    return DateRange.ofWithPriorNext(
        start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  static DateRange next(DateRange dr) {
    LocalDate start = addMonths(dr.getStartDate(), 3);
    LocalDate end = lastDayOfMonth(addMonths(firstDayOfMonth(dr.getEndDate()), 3));
    return DateRange.ofWithPriorNext(
        start, end, QuarterlyDateRange::prior, QuarterlyDateRange::next);
  }

  private QuarterlyDateRange() {}
}
