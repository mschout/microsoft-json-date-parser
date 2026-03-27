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

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class MicrosoftJsonDateTest :
    FunSpec({
      test("toString with UTC offset") {
        val odt = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1234567890000L), ZoneOffset.UTC)
        val date = MicrosoftJsonDate(odt)

        date.toString() shouldBe "/Date(1234567890000+0000)/"
      }

      test("toString with positive offset") {
        val offset = ZoneOffset.ofHoursMinutes(5, 30)
        val odt = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1234567890000L), offset)
        val date = MicrosoftJsonDate(odt)

        date.toString() shouldBe "/Date(1234567890000+0530)/"
      }

      test("toString with negative offset") {
        val offset = ZoneOffset.ofHoursMinutes(-5, 0)
        val odt = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1234567890000L), offset)
        val date = MicrosoftJsonDate(odt)

        date.toString() shouldBe "/Date(1234567890000-0500)/"
      }

      test("toString at epoch zero") {
        val odt = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        val date = MicrosoftJsonDate(odt)

        date.toString() shouldBe "/Date(0+0000)/"
      }

      test("toString with negative epoch") {
        val odt = OffsetDateTime.ofInstant(Instant.ofEpochMilli(-62135596800000L), ZoneOffset.UTC)
        val date = MicrosoftJsonDate(odt)

        date.toString() shouldBe "/Date(-62135596800000+0000)/"
      }

      test("toString roundtrips through parser") {
        val parser = MicrosoftJsonDateParser()
        val original = parser.parse("/Date(1234567890000+0530)/")!!

        val roundTripped = parser.parse(original.toString())!!

        roundTripped.instant() shouldBe original.instant()
        roundTripped.offsetDateTime.offset shouldBe original.offsetDateTime.offset
      }

      test("toString roundtrips with negative offset") {
        val parser = MicrosoftJsonDateParser()
        val original = parser.parse("/Date(1000000000000-0800)/")!!

        original.toString() shouldBe "/Date(1000000000000-0800)/"
      }
    })
