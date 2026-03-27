# Microsoft JSON Date Parser

A lightweight Kotlin/JVM library for parsing Microsoft's JSON date format (`/Date(ticks+offset)/`) into `java.time.OffsetDateTime` objects.

Microsoft's JSON date format is commonly found in responses from WCF services, ASP.NET, and older Microsoft APIs. This library provides a simple, dependency-free parser to convert these date strings into standard Java Time types.

## Installation

Replace `${version}` with the latest version available on Maven Central.

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("io.github.mschout:microsoft-json-date-parser:${version}")
}
```

### Gradle (Groovy DSL)

```groovy
dependencies {
    implementation 'io.github.mschout:microsoft-json-date-parser:${version}'
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.mschout</groupId>
    <artifactId>microsoft-json-date-parser</artifactId>
    <version>${version}</version>
</dependency>
```

## Usage

### Kotlin

```kotlin
import io.github.mschout.microsoft.json.date.MicrosoftJsonDateParser

val parser = MicrosoftJsonDateParser()

// Parse a date with no offset (defaults to UTC)
val date = parser.parse("/Date(1234567890000)/")
println(date?.offsetDateTime) // 2009-02-13T23:31:30Z

// Parse a date with a positive timezone offset
val dateWithOffset = parser.parse("/Date(1234567890000+0530)/")
println(dateWithOffset?.offsetDateTime) // 2009-02-14T05:01:30+05:30

// Parse a date with a negative timezone offset
val dateNegOffset = parser.parse("/Date(1234567890000-0500)/")
println(dateNegOffset?.offsetDateTime) // 2009-02-13T18:31:30-05:00

// Get the Instant (timezone-agnostic point in time)
val instant = dateWithOffset?.instant()
println(instant) // 2009-02-13T23:31:30Z

// Null-safe: returns null for null input
val result = parser.parse(null) // null
```

### Java

```java
import io.github.mschout.microsoft.json.date.MicrosoftJsonDateParser;
import io.github.mschout.microsoft.json.date.MicrosoftJsonDate;

MicrosoftJsonDateParser parser = new MicrosoftJsonDateParser();

MicrosoftJsonDate date = parser.parse("/Date(1234567890000+0530)/");
if (date != null) {
    System.out.println(date.offsetDateTime()); // 2009-02-14T05:01:30+05:30
    System.out.println(date.instant());        // 2009-02-13T23:31:30Z
}
```

## Microsoft JSON Date Format

The format is:

```
/Date(ticks[+-]offset)/
```

| Component | Description |
|-----------|-------------|
| `ticks` | Milliseconds since the Unix epoch (January 1, 1970, 00:00:00 UTC). Can be negative for dates before the epoch. |
| `offset` | Optional timezone offset in `[+-]HHMM` format (e.g., `+0530`, `-0500`). Defaults to UTC if omitted. |

### Examples of valid date strings

- `/Date(1234567890000)/` - UTC date
- `/Date(1234567890000+0530)/` - with positive offset (IST)
- `/Date(1234567890000-0500)/` - with negative offset (EST)
- `/Date(0)/` - Unix epoch
- `/Date(-62135596800000)/` - date before epoch

## Error Handling

The parser throws `IllegalArgumentException` for invalid input strings:

```kotlin
parser.parse("not a date")       // throws IllegalArgumentException
parser.parse("Date(1234567890)") // throws IllegalArgumentException (missing slashes)
parser.parse("")                 // throws IllegalArgumentException
```

## Requirements

- Java 21 or higher

## License

This project is licensed under the [Apache License 2.0](LICENSE).
