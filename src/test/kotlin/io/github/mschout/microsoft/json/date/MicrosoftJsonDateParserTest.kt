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

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class MicrosoftJsonDateParserTest :
    FunSpec({
      val parser = MicrosoftJsonDateParser()

      test("parse returns null for null input") { parser.parse(null).shouldBeNull() }

      test("parse date with no offset defaults to UTC") {
        val result = parser.parse("/Date(1234567890000)/")!!

        result.offsetDateTime.offset shouldBe ZoneOffset.UTC
        result.instant() shouldBe Instant.ofEpochMilli(1234567890000L)
      }

      test("parse date with positive offset") {
        val result = parser.parse("/Date(1234567890000+0530)/")!!

        val expectedOffset = ZoneOffset.ofHoursMinutes(5, 30)
        result.offsetDateTime.offset shouldBe expectedOffset
        result.instant() shouldBe Instant.ofEpochMilli(1234567890000L)
      }

      test("parse date with negative offset") {
        val result = parser.parse("/Date(1234567890000-0500)/")!!

        val expectedOffset = ZoneOffset.ofHoursMinutes(-5, 0)
        result.offsetDateTime.offset shouldBe expectedOffset
        result.instant() shouldBe Instant.ofEpochMilli(1234567890000L)
      }

      test("parse epoch zero") {
        val result = parser.parse("/Date(0)/")!!

        result.offsetDateTime shouldBe OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        result.instant() shouldBe Instant.EPOCH
      }

      test("parse negative epoch (before 1970)") {
        val result = parser.parse("/Date(-62135596800000)/")!!

        result.instant() shouldBe Instant.ofEpochMilli(-62135596800000L)
      }

      test("parse throws on invalid format") {
        shouldThrow<IllegalArgumentException> { parser.parse("not a date") }
      }

      test("parse throws on missing wrapping slashes") {
        shouldThrow<IllegalArgumentException> { parser.parse("Date(1234567890000)") }
      }

      test("parse throws on empty string") {
        shouldThrow<IllegalArgumentException> { parser.parse("") }
      }

      test("parse date with +0000 offset") {
        val result = parser.parse("/Date(1234567890000+0000)/")!!

        result.offsetDateTime.offset shouldBe ZoneOffset.UTC
        result.instant() shouldBe Instant.ofEpochMilli(1234567890000L)
      }

      test("instant() returns correct Instant regardless of offset") {
        val utcResult = parser.parse("/Date(1000000000000)/")!!
        val offsetResult = parser.parse("/Date(1000000000000+0900)/")!!

        utcResult.instant() shouldBe offsetResult.instant()
      }
    })
