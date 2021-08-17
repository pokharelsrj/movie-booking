package com.movie

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class ShowTest : StringSpec() {
    init {
        "should decrement the number of available seats if the number of available seats is more than 1" {
            val initialShowDetails = Show(65, ShowTime.MORNING, LocalDate.of(2021, 8, 29))

            initialShowDetails.decrementSeats() shouldBe Show(64, ShowTime.MORNING, LocalDate.of(2021, 8, 29))
        }

        "should return error if a user tries to book a seat when the number of seats available is less than 1" {
            val exception = shouldThrow<IllegalStateException> {
                Show(0, ShowTime.MORNING, LocalDate.of(2021, 8, 29)).decrementSeats()
            }
            exception.message shouldBe "Seats are filled"
        }
    }
}