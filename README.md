# date-range (Java)

A small, focused Java library that provides immutable range types and helpers for:

- Date ranges (weekly, bi‑weekly, semi‑monthly, monthly, quarterly, semi‑annual, annual)
- Time ranges during a day
- DateTime ranges (combining a date and a time range)
- Clock/current-time abstractions and date/time conversion utilities

The library emphasizes simple, composable value objects with predictable navigation (prior/next), containment, overlap, and iteration helpers.


## Status
- Java: 21
- Build: Gradle (Kotlin DSL)
- Tests: JUnit 5
- Code coverage: JaCoCo

Group and version are configured as:
- group: `com.stano`
- version: `1.0.0-SNAPSHOT`


## Getting started
You can build and use this project locally via `mavenLocal()` or consume it directly as a Gradle project dependency.

### Build locally
```bash
./gradlew clean build
```
This will compile the code, run tests, and generate coverage reports (see build/reports/jacoco/test/html/index.html).

### Publish to local Maven repository
```bash
./gradlew publish
```
Artifacts will be written to build/staging-deploy and can be zipped with:
```bash
./gradlew zipStagingDeploy
```
To publish the zipped bundle to Maven Central, run:
```bash
./gradlew publishToMavenCentral
```
If you prefer resolving from `mavenLocal()`, add it in your consuming project and copy/publish the artifacts to `~/.m2/repository` as needed. The coordinates are:
```
com.stano:date-range:1.0.0-SNAPSHOT
```

### Gradle settings in a consumer project
```kotlin
repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation("com.stano:date-range:1.0.0")
}
```


## Library overview

- `com.stano.daterange`
  - [DateRange](#daterange) — immutable inclusive date range, iterable over its days.
  - [DateRangeType](#daterangetype) — enum identifying a recurring period type (weekly, bi-weekly, semi-monthly, monthly).
  - [WeeklyDateRange](#weeklydaterange), [BiWeeklyDateRange](#biweeklydaterange), [SemiMonthlyDateRange](#semimonthlydaterange), [MonthlyDateRange](#monthlydaterange), [QuarterlyDateRange](#quarterlydaterange), [SemiAnnualDateRange](#semiannualdaterange), [AnnualDateRange](#annualdaterange) — factories that build `DateRange` instances with calendar-appropriate prior/next navigation.
- `com.stano.datetime`
  - [Clock](#clock) — abstraction over "the current date/time".
  - [UTCClock](#utcclock) — real current time in UTC.
  - [ConstantClock](#constantclock) — a fixed clock, for deterministic tests.
  - [DateTimeProvider](#datetimeprovider) — static facade for reading the current date/time.
  - [DateUtils](#dateutils) — month/year/day-of-week date arithmetic.
  - [DTUtil](#dtutil) — comparing and computing durations between date-times.
  - [JavaTimeUtil](#javatimeutil) — conversions between `java.time` and legacy `java.util.Date`/`java.sql.*` types.
  - [DateTimeConstants](#datetimeconstants) — numeric time-unit conversion constants.
- `com.stano.datetimerange`
  - [DateTimeRange](#datetimerange) — immutable inclusive date-time range with containment/overlap helpers.
  - [DateTimeRangeIterator](#datetimerangeiterator) — iterates a `DateTimeRange` in fixed-length steps.
  - [DateTimeRangeWithPeriodLength](#datetimerangewithperiodlength) — a `DateTimeRange` paired with a period length, for period-index calculations.
- `com.stano.timerange`
  - [TimeRange](#timerange) — immutable time-of-day range with midnight-aware overlap logic.


## Usage examples

### com.stano.daterange

#### DateRange

**Basics** — construct directly and read back the fields. With no factory involved, `prior()`/`next()` default to shifting by the range's own length in days.
```java
import com.stano.daterange.DateRange;
import java.time.LocalDate;

DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 10));
LocalDate start = range.getStartDate();     // 2025-01-01
LocalDate end = range.getEndDate();         // 2025-01-10
int days = range.getNumberOfDays();         // 10
DateRange next = range.next();              // 2025-01-11 to 2025-01-20 (shifted by 10 days)
DateRange prior = range.prior();            // 2024-12-22 to 2024-12-31
```

**Iterating** — `DateRange` implements `Iterable<LocalDate>`.
```java
import com.stano.daterange.DateRange;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 3));
List<LocalDate> allDates = range.dates();          // [2025-01-01, 2025-01-02, 2025-01-03]
Optional<LocalDate> first = range.dateAt(0);        // 2025-01-01
Optional<LocalDate> outOfBounds = range.dateAt(5);  // empty

for (LocalDate date : range) {
  // visits 2025-01-01, 2025-01-02, 2025-01-03 in order
}

List<LocalDate> wednesdays = range.datesForDay(DayOfWeek.WEDNESDAY); // [2025-01-01]
```

**Containment and overlap:**
```java
import com.stano.daterange.DateRange;
import java.time.LocalDate;
import java.util.List;

DateRange range = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));
DateRange inner = DateRange.of(LocalDate.of(2025, 1, 10), LocalDate.of(2025, 1, 20));
DateRange overlapping = DateRange.of(LocalDate.of(2025, 1, 25), LocalDate.of(2025, 2, 5));
DateRange outside = DateRange.of(LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 10));

boolean containsDate = range.containsDate(LocalDate.of(2025, 1, 15));       // true
boolean containsRange = range.containsRange(inner);                        // true
boolean overlaps = range.overlaps(overlapping);                            // true
boolean overlapsAny = range.overlapsAny(List.of(outside, overlapping));    // true
```

**Navigation and windows:**
```java
import com.stano.daterange.DateRange;
import com.stano.daterange.WeeklyDateRange;
import java.time.LocalDate;
import java.util.List;

DateRange week = WeeklyDateRange.withStartDate(LocalDate.of(2025, 1, 1)); // 2025-01-01 to 2025-01-07

DateRange twoWeeksAhead = week.nextN(2);   // 2025-01-15 to 2025-01-21
DateRange twoWeeksBack = week.priorN(2);   // 2024-12-18 to 2024-12-24

List<DateRange> before = week.rangesBefore(2);            // the 2 weeks before, earliest first
List<DateRange> beforeInclusive = week.rangesBeforeInclusive(2); // same, plus this week last
List<DateRange> window = week.rangesWindow(1, 1);          // prior week, this week, next week

DateRange containing = week.rangeContainingDate(LocalDate.of(2025, 1, 20)); // 2025-01-15 to 2025-01-21

// rangesContainingSpan walks prior()/next() to cover a span; throws IllegalArgumentException
// if either date is null or toDate is before fromDate.
List<DateRange> spanned =
    week.rangesContainingSpan(LocalDate.of(2025, 1, 5), LocalDate.of(2025, 1, 20));
```

**Sorting and equality:**
```java
import com.stano.daterange.DateRange;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

DateRange a = DateRange.of(LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28));
DateRange b = DateRange.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31));

List<DateRange> ranges = new ArrayList<>(List.of(a, b));
ranges.sort(null); // Comparable<DateRange> orders by start date: [b, a]

boolean equal = a.equals(DateRange.of(LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28))); // true

// startDay() is only populated for ranges built via MonthlyDateRange.withEndDateAndStartDay;
// a plain DateRange always reports empty.
Optional<Integer> startDay = a.startDay(); // empty
```

#### DateRangeType

An enum identifying a recurring period type, with how many periods occur per year.
```java
import com.stano.daterange.DateRangeType;

int weeklyPeriods = DateRangeType.WEEKLY.getPeriodsPerYear();        // 52
int monthlyPeriods = DateRangeType.MONTHLY.getPeriodsPerYear();      // 12
boolean weekBased = DateRangeType.BI_WEEKLY.isWeekBased();           // true
boolean notWeekBased = DateRangeType.SEMI_MONTHLY.isWeekBased();     // false
```

#### WeeklyDateRange

Builds 7-day ranges; `next()`/`prior()` shift by a full week.
```java
import com.stano.daterange.DateRange;
import com.stano.daterange.WeeklyDateRange;
import java.time.DayOfWeek;
import java.time.LocalDate;

DateRange fromStart = WeeklyDateRange.withStartDate(LocalDate.of(2025, 1, 1));
// 2025-01-01 to 2025-01-07 (Jan 1, 2025 is a Wednesday)

DateRange fromEnd = WeeklyDateRange.withEndDate(LocalDate.of(2025, 1, 7));
// 2025-01-01 to 2025-01-07 (same range)

// withTargetDate finds the range ending on the next occurrence of endDay on/after target.
DateRange fromTarget = WeeklyDateRange.withTargetDate(LocalDate.of(2025, 1, 1), DayOfWeek.FRIDAY);
// 2024-12-28 to 2025-01-03 (ends on the Friday on/after Jan 1)

DateRange nextWeek = fromStart.next(); // 2025-01-08 to 2025-01-14
```

#### BiWeeklyDateRange

Same three factories as `WeeklyDateRange`, but builds 14-day ranges.
```java
import com.stano.daterange.DateRange;
import com.stano.daterange.BiWeeklyDateRange;
import java.time.DayOfWeek;
import java.time.LocalDate;

DateRange payPeriod = BiWeeklyDateRange.withStartDate(LocalDate.of(2025, 1, 1));
// 2025-01-01 to 2025-01-14

DateRange nextPayPeriod = payPeriod.next(); // 2025-01-15 to 2025-01-28

DateRange fromTarget = BiWeeklyDateRange.withTargetDate(LocalDate.of(2025, 1, 1), DayOfWeek.FRIDAY);
// 2024-12-21 to 2025-01-03 (14-day range ending on the Friday on/after Jan 1)
```

#### SemiMonthlyDateRange

Builds twice-monthly ranges (1st–15th, or 16th–end of month); `next()`/`prior()` alternate between the two halves and correctly roll across month boundaries.
```java
import com.stano.daterange.DateRange;
import com.stano.daterange.SemiMonthlyDateRange;
import java.time.LocalDate;

DateRange firstHalf = SemiMonthlyDateRange.withEndDate(LocalDate.of(2025, 1, 15));
// 2025-01-01 to 2025-01-15

DateRange secondHalf = firstHalf.next();
// 2025-01-16 to 2025-01-31

DateRange nextMonthFirstHalf = secondHalf.next();
// 2025-02-01 to 2025-02-15 (rolls into the following month)
```

#### MonthlyDateRange

Builds calendar-month-aligned ranges, with an optional custom start day for billing-cycle-style periods.
```java
import com.stano.daterange.DateRange;
import com.stano.daterange.MonthlyDateRange;
import java.time.LocalDate;
import java.util.Optional;

DateRange january = MonthlyDateRange.withEndDateOnFirst(LocalDate.of(2025, 1, 31));
// 2025-01-01 to 2025-01-31 (starts on the 1st)

DateRange february = january.next(); // 2025-02-01 to 2025-02-28

// A custom start day builds billing-cycle-style ranges, e.g. the 15th through the 14th.
DateRange billingCycle = MonthlyDateRange.withEndDateAndStartDay(LocalDate.of(2025, 1, 14), 15);
// 2024-12-15 to 2025-01-14

Optional<Integer> startDay = billingCycle.startDay(); // Optional.of(15)
```

#### QuarterlyDateRange

Builds a rolling 3-calendar-month range anchored to the given date's month — **not** a standard Jan/Apr/Jul/Oct calendar quarter.
```java
import com.stano.daterange.DateRange;
import com.stano.daterange.QuarterlyDateRange;
import java.time.LocalDate;

DateRange quarter = QuarterlyDateRange.withStartDate(LocalDate.of(2025, 2, 10));
// 2025-02-01 to 2025-04-30 (start's month, February, plus the following two months)

DateRange nextQuarter = quarter.next(); // 2025-05-01 to 2025-07-31
```

#### SemiAnnualDateRange

Builds a 6-calendar-month range.
```java
import com.stano.daterange.DateRange;
import com.stano.daterange.SemiAnnualDateRange;
import java.time.LocalDate;

DateRange firstHalf = SemiAnnualDateRange.withStartDate(LocalDate.of(2025, 1, 1));
// 2025-01-01 to 2025-06-30

DateRange secondHalf = firstHalf.next();
// 2025-07-01 to 2025-12-30 (next() shifts both dates by 6 months; Jun 30 + 6 months is Dec 30, not Dec 31)
```

#### AnnualDateRange

Builds a 1-year range. A range starting on February 29th ends on February 28th of the following year, since the following year isn't guaranteed to be a leap year.
```java
import com.stano.daterange.DateRange;
import com.stano.daterange.AnnualDateRange;
import java.time.LocalDate;

DateRange leapYear = AnnualDateRange.withStartDate(LocalDate.of(2024, 2, 29));
// 2024-02-29 to 2025-02-28 (2025 is not a leap year)

DateRange next = leapYear.next();
// 2025-02-28 to 2026-02-27
```

### com.stano.datetime

#### Clock

An abstraction over "the current date/time", so callers can depend on an interface instead of the system clock directly.
```java
import com.stano.datetime.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

Clock fixedNoon = new Clock() {
  @Override public LocalDate currentDate() { return LocalDate.of(2025, 1, 1); }
  @Override public LocalDateTime currentDateTime() { return LocalDateTime.of(2025, 1, 1, 12, 0); }
  @Override public LocalTime currentTime() { return LocalTime.of(12, 0); }
};
```

#### UTCClock

Returns the real current date/time, read in the UTC time zone regardless of the host's default zone.
```java
import com.stano.datetime.Clock;
import com.stano.datetime.UTCClock;
import java.time.LocalDateTime;

Clock clock = new UTCClock();
LocalDateTime now = clock.currentDateTime(); // the actual current UTC date and time
```

#### ConstantClock

A `Clock` that always returns the same fixed value — useful for deterministic tests.
```java
import com.stano.datetime.Clock;
import com.stano.datetime.ConstantClock;
import java.time.LocalDate;
import java.time.LocalDateTime;

Clock testClock = ConstantClock.of(LocalDateTime.of(2025, 6, 1, 12, 0));
LocalDate today = testClock.currentDate(); // always 2025-06-01, regardless of when the test runs
```

#### DateTimeProvider

A static facade for reading the current date/time, backed internally by a `UTCClock`.
```java
import com.stano.datetime.DateTimeProvider;
import java.time.LocalDate;

LocalDate today = DateTimeProvider.currentDate(); // the actual current UTC date
```
> **Note:** `DateTimeProvider`'s backing clock field has no public setter, so — despite `ConstantClock` existing — there is currently no public API to swap `DateTimeProvider` onto a fixed clock for tests.

#### DateUtils

Month/year/day-of-week date arithmetic helpers.
```java
import com.stano.datetime.DateUtils;
import java.time.LocalDate;

LocalDate date = LocalDate.of(2025, 3, 17);
LocalDate firstOfMonth = DateUtils.firstDayOfMonth(date);   // 2025-03-01
LocalDate lastOfMonth = DateUtils.lastDayOfMonth(date);     // 2025-03-31
LocalDate plusTwoMonths = DateUtils.addMonths(date, 2);     // 2025-05-17
LocalDate minusOneYear = DateUtils.subtractYears(date, 1);  // 2024-03-17
```
```java
import com.stano.datetime.DateUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;

// Days forward (0-6) from date to the next occurrence of a given day of the week.
long offset = DateUtils.calculateDayOfWeekOffset(LocalDate.of(2025, 1, 1), DayOfWeek.FRIDAY);
// 2 (Jan 1, 2025 is a Wednesday; Friday is 2 days later)
```

#### DTUtil

Compares `LocalDateTime` values and computes durations between them.
```java
import com.stano.datetime.DTUtil;
import java.time.LocalDateTime;

LocalDateTime earlier = LocalDateTime.of(2025, 1, 1, 9, 0);
LocalDateTime later = LocalDateTime.of(2025, 1, 1, 17, 0);

LocalDateTime earliest = DTUtil.earliest(earlier, later); // earlier
LocalDateTime latest = DTUtil.latest(earlier, later);     // later

// A null argument always loses to a non-null one, for both methods.
LocalDateTime whenOneIsNull = DTUtil.earliest(null, later); // later
```
```java
import com.stano.datetime.DTUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;

LocalDateTime start = LocalDateTime.of(2025, 1, 1, 9, 0, 0);
LocalDateTime end = LocalDateTime.of(2025, 1, 1, 10, 45, 0);

int hours = DTUtil.durationInHours(start, end);              // 1 (truncated)
int minutes = DTUtil.durationInMinutes(start, end);           // 105
int seconds = DTUtil.durationInSeconds(start, end);            // 6300
BigDecimal fractionalSeconds = DTUtil.durationInFractionalSeconds(start, end); // 6300 (4 sig figs)
double fractionalHours = DTUtil.durationInFractionalHours(start, end);         // 1.75
```

#### JavaTimeUtil

Conversions between `java.time` types and legacy `java.util.Date`/`java.sql.*` types, all UTC-based.

**`java.util.Date` conversions:**
```java
import com.stano.datetime.JavaTimeUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

LocalDate localDate = JavaTimeUtil.javaDateToLocalDate(new Date());
LocalDateTime localDateTime = JavaTimeUtil.javaDateToLocalDateTime(new Date());
Date backToDate = JavaTimeUtil.localDateToJavaDate(LocalDate.of(2025, 1, 1));
```

**`java.sql.*` conversions:**
```java
import com.stano.datetime.JavaTimeUtil;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

java.sql.Date sqlDate = JavaTimeUtil.localDateToSqlDate(LocalDate.of(2025, 1, 1));
LocalDate fromSqlDate = JavaTimeUtil.sqlDateToLocalDate(sqlDate);
Timestamp timestamp = JavaTimeUtil.localDateTimeToSqlTimestamp(LocalDateTime.of(2025, 1, 1, 12, 0));
LocalDateTime fromTimestamp = JavaTimeUtil.sqlTimestampToLocalDateTime(timestamp);
```

**Time zones, US day numbers, and UTC calendars:**
```java
import com.stano.datetime.JavaTimeUtil;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

LocalDateTime nyTime = JavaTimeUtil.toLocalDateTimeAtZone(
    LocalDateTime.of(2025, 3, 26, 8, 30), ZoneId.of("UTC"), ZoneId.of("America/New_York"));
// 2025-03-26T04:30 (New York is already in EDT, UTC-4, after the Mar 9 spring-forward)

DayOfWeek sunday = JavaTimeUtil.usDayNumberToDayOfWeek(1);   // DayOfWeek.SUNDAY (1=Sunday..7=Saturday)
int usNumber = JavaTimeUtil.dayOfWeekToUsDayNumber(DayOfWeek.SUNDAY); // 1

Calendar utcCalendar = JavaTimeUtil.getUTCCalender(); // Calendar.getInstance(TimeZone.getTimeZone("UTC"))
ZoneId utcZone = JavaTimeUtil.UTC_ZONE_ID;             // ZoneId.of("UTC")
```

#### DateTimeConstants

Numeric constants for converting between common units of time.
```java
import com.stano.datetime.DateTimeConstants;

int minutesInThreeDays = 3 * DateTimeConstants.MINUTES_PER_DAY;   // 4320
int secondsInAnHour = DateTimeConstants.SECONDS_PER_HOUR;         // 3600
int daysInAWeek = DateTimeConstants.DAYS_PER_WEEK;                 // 7
```

### com.stano.datetimerange

#### DateTimeRange

**Construction:**
```java
import com.stano.datetimerange.DateTimeRange;
import com.stano.timerange.TimeRange;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

DateTimeRange range = DateTimeRange.of(
    LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 17, 0));
Duration duration = range.getDuration();          // PT8H
double hours = range.getFractionalHours();          // 8.0

DateTimeRange fullDay = DateTimeRange.allDay(LocalDate.of(2025, 1, 1));
// 2025-01-01T00:00 to 2025-01-02T00:00

// Combines a TimeRange's start/end times with a date; a TimeRange whose end time is
// before its start time is treated as crossing midnight.
TimeRange overnight = TimeRange.of(LocalTime.of(22, 0), LocalTime.of(1, 0));
DateTimeRange shift = DateTimeRange.fromTimeRangeOnDate(overnight, LocalDate.of(2025, 3, 10));
// 2025-03-10T22:00 to 2025-03-11T01:00
```

**Overlap:**
```java
import com.stano.datetimerange.DateTimeRange;
import java.time.Duration;
import java.time.LocalDateTime;

DateTimeRange business = DateTimeRange.of(
    LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 17, 0));
DateTimeRange touchingEnd = DateTimeRange.of(
    LocalDateTime.of(2025, 1, 1, 17, 0), LocalDateTime.of(2025, 1, 1, 18, 0));
DateTimeRange contained = DateTimeRange.of(
    LocalDateTime.of(2025, 1, 1, 10, 0), LocalDateTime.of(2025, 1, 1, 12, 0));
DateTimeRange evening = DateTimeRange.of(
    LocalDateTime.of(2025, 1, 1, 16, 0), LocalDateTime.of(2025, 1, 1, 20, 0));

boolean overlaps = business.overlaps(touchingEnd);              // true (boundary is inclusive)
boolean overlapsExclusive = business.overlapsExclusive(touchingEnd); // false (only touches at the boundary)
boolean overlapsCompletely = business.overlapsCompletely(contained); // true (contained is fully inside business)

DateTimeRange overlap = business.overlapRange(evening);          // 2025-01-01T16:00 to 2025-01-01T17:00
Duration overlapDuration = business.overlapDuration(evening);    // PT1H
```

**Containment:**
```java
import com.stano.datetimerange.DateTimeRange;
import java.time.LocalDateTime;

DateTimeRange business = DateTimeRange.of(
    LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 17, 0));
LocalDateTime atStart = LocalDateTime.of(2025, 1, 1, 9, 0);
LocalDateTime atEnd = LocalDateTime.of(2025, 1, 1, 17, 0);

boolean containsStart = business.containsDateTime(atStart);                          // true
boolean exclusiveContainsStart = business.containsDateTimeExclusive(atStart);        // false (excludes both ends)
boolean exclusiveOfEndContainsStart = business.containsDateTimeExclusiveOfEndDateTime(atStart); // true (includes start)

boolean containsEnd = business.containsDateTime(atEnd);                              // true
boolean exclusiveOfEndContainsEnd = business.containsDateTimeExclusiveOfEndDateTime(atEnd); // false (excludes end)
```

#### DateTimeRangeIterator

Steps through a `DateTimeRange` in fixed-length minute increments.
```java
import com.stano.datetimerange.DateTimeRange;
import com.stano.datetimerange.DateTimeRangeIterator;
import java.time.LocalDateTime;
import java.util.Iterator;

DateTimeRange range = DateTimeRange.of(
    LocalDateTime.of(2025, 1, 1, 9, 0), LocalDateTime.of(2025, 1, 1, 10, 0));
Iterator<LocalDateTime> iterator = new DateTimeRangeIterator(range, 30);

while (iterator.hasNext()) {
  LocalDateTime step = iterator.next(); // 2025-01-01T09:00, then 2025-01-01T09:30
}

// remove() is not supported and always throws UnsupportedOperationException.
```

#### DateTimeRangeWithPeriodLength

Pairs a `DateTimeRange` with a fixed period length in minutes, for period-index calculations and iteration.
```java
import com.stano.datetimerange.DateTimeRange;
import com.stano.datetimerange.DateTimeRangeWithPeriodLength;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

DateTimeRange range = DateTimeRange.of(
    LocalDateTime.of(2025, 1, 1, 8, 0), LocalDateTime.of(2025, 1, 1, 16, 0));
DateTimeRangeWithPeriodLength periods = DateTimeRangeWithPeriodLength.of(range, 60);

int startIndex = periods.getStartIndex();                  // 8 (8:00 is the 8th 60-minute period of the day)
int endIndex = periods.getEndIndex();                        // 16
int numberOfPeriods = periods.getNumberOfPeriodsInRange();   // 8

List<Integer> indexes = periods.getIndexStream().boxed().collect(Collectors.toList()); // [8, 9, ..., 15]

for (LocalDateTime step : periods) {
  // iterates the same as a DateTimeRangeIterator over range with a 60-minute period
}
```

### com.stano.timerange

#### TimeRange

**Basics:**
```java
import com.stano.timerange.TimeRange;
import java.time.Duration;
import java.time.LocalTime;

TimeRange morning = TimeRange.of(LocalTime.of(9, 0), LocalTime.of(12, 0));
LocalTime start = morning.getStartTime();  // 09:00
LocalTime end = morning.getEndTime();      // 12:00
Duration duration = morning.getDuration(); // PT3H

TimeRange lateMorning = TimeRange.of(LocalTime.of(11, 0), LocalTime.of(14, 0));
boolean overlaps = morning.overlaps(lateMorning); // true
```

**Midnight edge case** — `overlaps()` special-cases ranges ending exactly at midnight, since `LocalTime.MIDNIGHT` (`00:00`) would otherwise look like the very start of the day rather than the end of it.
```java
import com.stano.timerange.TimeRange;
import java.time.LocalTime;

// A TimeRange whose end is before its start is treated as spanning midnight.
TimeRange overnight = TimeRange.of(LocalTime.of(22, 0), LocalTime.of(1, 0)); // 10pm to 1am

TimeRange evening = TimeRange.of(LocalTime.of(20, 0), LocalTime.MIDNIGHT);     // 8pm to midnight
TimeRange lateNight = TimeRange.of(LocalTime.of(23, 0), LocalTime.MIDNIGHT);   // 11pm to midnight

// Two ranges that both end exactly at midnight are always considered overlapping,
// regardless of their start times.
boolean bothEndAtMidnight = evening.overlaps(lateNight); // true
```


## Running tests
```bash
./gradlew test
```
- JUnit 5 tests are located under src/test/java/... and run on the JUnit Platform.
- Coverage reports are produced by JaCoCo.


## SonarQube (optional)
The build includes a SonarQube plugin. To use it, provide the following as Gradle properties or environment variables:
- `sonar.host.url` (or `SONAR_HOST_URL`)
- `sonar.token` (or `SONAR_TOKEN`)

If neither is set, the plugin is skipped and the build is unaffected.

Then run:
```bash
./gradlew sonar
```


## License
This project is licensed under the Apache License, Version 2.0. See LICENSE and NOTICE for details.
