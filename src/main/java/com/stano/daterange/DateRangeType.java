package com.stano.daterange;

public enum DateRangeType {
   WEEKLY(52),
   BI_WEEKLY(26),
   SEMI_MONTHLY(24),
   MONTHLY(12);

   public int getPeriodsPerYear() {
      return periodsPerYear;
   }

   public boolean isWeekBased() {
      return this == WEEKLY || this == BI_WEEKLY;
   }

   DateRangeType(int periodsPerYear) {
      this.periodsPerYear = periodsPerYear;
   }

   private final int periodsPerYear;
}
