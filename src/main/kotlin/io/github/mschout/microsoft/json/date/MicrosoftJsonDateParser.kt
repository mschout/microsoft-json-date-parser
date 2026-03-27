/*
 * Copyright 2026 Michael Schout
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.mschout.microsoft.json.date

import java.time.Instant
import java.time.ZoneOffset

private val MICROSOFT_DATE_PATTERN = Regex("""/Date\((-?\d+)([+-]\d{4})?\)/""")
private const val PARSE_ERROR_MSG = "Expected Microsoft date format: /Date(ticks[+-]offset)/"

/**
 * A parser for Microsoft's JSON date format.
 *
 * Microsoft JSON date format is represented as `/Date(ticks[+-]offset)/`, where:
 * - `ticks` are the number of milliseconds since the Unix epoch (January 1, 1970, 00:00:00 UTC).
 * - `offset` is the timezone offset from UTC in the format `[+-]HHMM`.
 *
 * This class provides functionality to parse such date strings into a [MicrosoftJsonDate] object.
 */
class MicrosoftJsonDateParser {

  /**
   * Parses a Microsoft JSON date string into a [MicrosoftJsonDate] object.
   *
   * Microsoft JSON date format is represented as `/Date(ticks[+-]offset)/`, where:
   * - `ticks` are the number of milliseconds since the Unix epoch (January 1, 1970, 00:00:00 UTC).
   * - `offset` specifies the timezone offset from UTC in the format `[+-]HHMM`.
   *
   * @param date The Microsoft JSON date string to parse. Can be null.
   * @return A [MicrosoftJsonDate] object representing the parsed date and time, or null if the
   *   input string is null.
   * @throws IllegalArgumentException If the input string does not match the expected format.
   */
  fun parse(date: String?): MicrosoftJsonDate? {
    if (date == null) return null

    val match =
        MICROSOFT_DATE_PATTERN.matchEntire(date)
            ?: throw IllegalArgumentException("$PARSE_ERROR_MSG: '$date'")

    val epochMillis = match.groupValues[1].toLong()
    val instant = Instant.ofEpochMilli(epochMillis)

    val offset = parseOffset(match.groupValues[2])

    return MicrosoftJsonDate(instant.atOffset(offset))
  }

  private fun parseOffset(offsetStr: String): ZoneOffset {
    if (offsetStr.isEmpty()) return ZoneOffset.UTC

    val sign = if (offsetStr[0] == '+') 1 else -1
    val hours = offsetStr.substring(1, 3).toInt()
    val minutes = offsetStr.substring(3, 5).toInt()

    return ZoneOffset.ofHoursMinutes(sign * hours, sign * minutes)
  }
}
