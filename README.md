# date-range (Java)

A small, focused Java library that provides immutable range types and helpers for:

- Date ranges (weekly, bi‑weekly, semi‑monthly, monthly, quarterly, semi‑annual, annual)
- Time ranges during a day
- DateTime ranges (combining a date and a time range)

The library emphasizes simple, composable value objects with predictable navigation (prior/next), containment, overlap, and iteration helpers.


## Status
- Java: 21
- Build: Gradle (Kotlin DSL)
- Tests: JUnit 5 and Spock
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
  implementation("com.stano:date-range:1.0.0-SNAPSHOT")
}
```


## Key packages and types
- com.stano.daterange
  - DateRange: immutable date range with inclusive start and end, iterable over days.
  - WeeklyDateRange, BiWeeklyDateRange, SemiMonthlyDateRange, MonthlyDateRange, QuarterlyDateRange, SemiAnnualDateRange, AnnualDateRange: helpers to construct DateRange instances with appropriate navigation.
  - DateUtils: month/day helpers.
- com.stano.timerange
  - TimeRange: immutable time-of-day range with overlap logic (handles midnight edge cases).
- com.stano.datetimerange
  - DateTimeRange: immutable date-time range with containment/overlap and helper factories (e.g., from a TimeRange on a given date, allDay).


## Usage examples

### Weekly date range
```java
import com.stano.daterange.*;
import java.time.*;

DateRange week = WeeklyDateRange.withStartDate(LocalDate.of(2025, 1, 1));
LocalDate first = week.startDate(); // 2025-01-01
LocalDate last  = week.endDate();   // 2025-01-07
DateRange nextWeek = week.next();   // shifts by 7 days
boolean overlaps = week.overlaps(nextWeek); // false
```

### Monthly date range (calendar months)
```java
import com.stano.daterange.*;
import java.time.*;

DateRange jan = MonthlyDateRange.withEndDateOnFirst(LocalDate.of(2025, 1, 31));
// jan.startDate() == 2025-01-01, jan.endDate() == 2025-01-31
DateRange feb = jan.next();
```

### Creating a DateTimeRange from a TimeRange on a date
```java
import com.stano.timerange.*;
import com.stano.datetimerange.*;
import java.time.*;

TimeRange tr = TimeRange.of(LocalTime.of(22, 0), LocalTime.of(1, 0)); // spans midnight
LocalDate date = LocalDate.of(2025, 3, 10);
DateTimeRange dtr = DateTimeRange.fromTimeRangeOnDate(tr, date);
// dtr covers 2025-03-10T22:00 to 2025-03-11T01:00
```

## Running tests
```bash
./gradlew test
```
- Spock (Groovy) and JUnit 5 tests are located under src/test/groovy/... and run on the JUnit Platform.
- Coverage reports are produced by JaCoCo.


## SonarQube (optional)
The build includes a SonarQube plugin. To use it, provide the following properties (for example via gradle.properties or environment variables resolved into `com.stano.sonar.host` and `com.stano.sonar.token`):
- sonar.host.url
- sonar.token

Then run:
```bash
./gradlew sonar
```


## License
This project is licensed under the Apache License, Version 2.0. See LICENSE and NOTICE for details.
