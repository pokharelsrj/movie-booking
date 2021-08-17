package com.movie

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class ShowTest : StringSpec() {
    init {
        "should decrement the number of available seats if the number of available seats is more than 1" {
            val initialShowDetails = Show(100, 65, ShowTime.MORNING, LocalDate.of(2021, 8, 29), "ABCD")

            initialShowDetails.decrementSeats() shouldBe Show(100, 64, ShowTime.MORNING, LocalDate.of(2021, 8, 29), "ABCD")
        }

        "should return error if a user tries to book a seat when the number of seats available is less than 1" {
            val exception = shouldThrow<IllegalStateException> {
                Show(100, 0, ShowTime.MORNING, LocalDate.of(2021, 8, 29), "ABCD").decrementSeats()
            }

            exception.message shouldBe "Seats are filled"
        }

        "should return name of the movie for a particular show" {
            val initialShowDetails = Show(100, 65, ShowTime.MORNING, LocalDate.of(2021, 8, 29), "DDLJ")

            initialShowDetails.movieName shouldBe "DDLJ"
        }

        "should not decrement the number of seats if no movie is assigned to a slot" {
            val initialShowDetails = Show(100, 100, ShowTime.MORNING, LocalDate.of(2021, 8, 29), null)

            val exception = shouldThrowAny {
                initialShowDetails.decrementSeats()
            }

            exception.message shouldBe "Cannot issue ticket unless a movie is assigned to the show"
        }

        "should add name of the movie if a slot is not assigned any movie name" {
            val initialShowDetails = Show(100, 100, ShowTime.MORNING, LocalDate.of(2021, 8, 29), null)

            val showWithNewMovieName = initialShowDetails.addMovieName("Interstellar")

            showWithNewMovieName shouldBe Show(100, 100, ShowTime.MORNING, LocalDate.of(2021, 8, 29), "Interstellar")
        }

        "should return error if a new movie name is added after some tickets have already been sold for the show" {
            val initialShowDetails = Show(100, 65, ShowTime.MORNING, LocalDate.of(2021, 8, 29), "Memento")

            val exception = shouldThrowAny {
                initialShowDetails.addMovieName("Ghajini")
            }

            exception.message shouldBe "Movie name cannot be renamed"
        }

        "should be able to rename the movie name if no tickets have been sold for the show" {
            val initialShowDetails = Show(100, 100, ShowTime.MORNING, LocalDate.of(2021, 8, 29), "Memento")

            val showWithNewMovieName = initialShowDetails.addMovieName("Interstellar")

            showWithNewMovieName shouldBe Show(100, 100, ShowTime.MORNING, LocalDate.of(2021, 8, 29), "Interstellar")
        }
    }
}