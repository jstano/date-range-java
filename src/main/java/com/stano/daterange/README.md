Package: com.stano.daterange_rs

Overview
--------
This is a Java port of the Rust daterange module using the same composition pattern:
- DateRange is an immutable value object holding startDate, endDate, inclusive length, and optional prior/next functions.
- Concrete builders (Weekly/BiWeekly/SemiMonthly/Monthly/Quarterly/SemiAnnual/Annual) create DateRange instances and wire custom prior/next functions where needed to keep navigation semantics identical to Rust.

Key classes
-----------
- DateRange: core type with methods:
  - startDate(), endDate(), len(), startDay()
  - dates(), dateAt(int), datesForDay(DayOfWeek)
  - containsDate(LocalDate), containsRange(DateRange), overlaps(DateRange), overlapsAny(List<DateRange>)
  - rangeContainingDate(LocalDate)
  - prior(), next(), priorN(int), nextN(int)
  - rangesBefore(int), rangesBeforeInclusive(int), rangesAfter(int), rangesAfterInclusive(int), rangesWindow(int,int)
  - rangesContainingSpan(LocalDate, LocalDate)
- DateUtils: firstDayOfMonth, lastDayOfMonth, add/subtract months/years.
- WeeklyDateRange, BiWeeklyDateRange: simple fixed-length ranges; default prior/next shift by length.
- SemiMonthlyDateRange: 1..15 and 16..end-of-month ranges with composition-based prior/next.
- MonthlyDateRange: supports calendar months (startDay=1) and custom startDay segments (e.g., 16..15). Stores startDay metadata in DateRange to preserve composition.
- QuarterlyDateRange, SemiAnnualDateRange, AnnualDateRange: calendar-based segments with composition prior/next.

Copy to java-utils project
--------------------------
Copy this folder to your java-utils project to match the requested path:
- From: /Users/jstano/workspace/date-range-rs/src/main/java/com/stano/daterange_rs
- To:   /Users/jstano/workspace/java-utils/src/main/java/com/stano/daterange_rs
The package name already matches (com.stano.daterange_rs).

Usage example
-------------
import com.stano.daterange_rs.*;
import java.time.*;

DateRange week = WeeklyDateRange.withStartDate(LocalDate.of(2025, 1, 1));
LocalDate first = week.startDate(); // 2025-01-01
LocalDate last  = week.endDate();   // 2025-01-07
DateRange nextWeek = week.next();
boolean overlaps = week.overlaps(nextWeek); // false

DateRange month = MonthlyDateRange.withEndDateOnFirst(LocalDate.of(2025, 1, 31));
DateRange found = month.rangeContainingDate(LocalDate.of(2025, 3, 15));

Notes
-----
- equals/hashCode compare start and end only (parity with Rust tests).
- Default prior/next shifts by the inclusive length when no custom functions are provided (Weekly, BiWeekly behave this way).
- AnnualDateRange handles Feb 29 starts: end is Feb 28 of the following year.

Testing (optional)
------------------
If desired, add JUnit tests mirroring the Rust tests in your java-utils project. I can supply those upon request.
