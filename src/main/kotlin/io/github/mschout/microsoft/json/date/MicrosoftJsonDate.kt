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
import java.time.OffsetDateTime
import kotlin.math.abs

/**
 * Represents a date and time parsed from Microsoft's JSON date format.
 *
 * Microsoft JSON date format is typically represented as `/Date(ticks[+-]offset)/`, where `ticks`
 * are the number of milliseconds since the Unix epoch, and `offset` specifies the timezone offset
 * from UTC.
 *
 * @property offsetDateTime The date and time value as an [OffsetDateTime] object.
 */
@JvmRecord
data class MicrosoftJsonDate(val offsetDateTime: OffsetDateTime) {
  fun instant(): Instant = offsetDateTime.toInstant()

  /**
   * Returns the string representation of the date and time in Microsoft's JSON date format.
   *
   * The format is `/Date(ticks[+-]offset)/`, where:
   * - `ticks` is the number of milliseconds since the Unix epoch (January 1, 1970, 00:00:00 UTC).
   * - `offset` is the timezone offset from UTC in the format `[+-]HHMM`.
   *
   * @return The string representation of the date and time in Microsoft's JSON date format.
   */
  override fun toString(): String {
    val epochMillis = offsetDateTime.toInstant().toEpochMilli()
    return "/Date($epochMillis${getOffsetString()})/"
  }

  private fun getOffsetString(): String {
    val offset = offsetDateTime.offset
    val sign = if (offset.totalSeconds >= 0) "+" else "-"
    val offsetSeconds = abs(offset.totalSeconds)
    val hours = offsetSeconds / 3600
    val minutes = (offsetSeconds % 3600) / 60

    return "${sign}${"%02d%02d".format(hours, minutes)}"
  }
}
