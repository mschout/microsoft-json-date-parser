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
}
