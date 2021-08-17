package com.movie

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class MovieHallTest : StringSpec() {
    init {
        "should reserve ticket for a show" {
            val showsForToday = listOf(
                Show(100,100, ShowTime.MORNING, LocalDate.of(2021, 8, 19),"ABCD"),
                Show(100,100, ShowTime.AFTERNOON, LocalDate.of(2021, 8, 19),"ABCD"),
                Show(100,100, ShowTime.EVENING, LocalDate.of(2021, 8, 19),"ABCD")
            )

            val movieHall = MovieHall(showsForToday)
            val (newBookMyShowState, ticket) = movieHall.reserve(ShowTime.MORNING, LocalDate.of(2021, 8, 19))

            ticket shouldBe Ticket(100, ShowTime.MORNING, LocalDate.of(2021, 8, 19))
            newBookMyShowState.totalReservedTicketsFor(ShowTime.MORNING, LocalDate.of(2021, 8, 19)) shouldBe 99
            newBookMyShowState.totalReservedTicketsFor(ShowTime.AFTERNOON, LocalDate.of(2021, 8, 19)) shouldBe 100
            newBookMyShowState.totalReservedTicketsFor(ShowTime.EVENING, LocalDate.of(2021, 8, 19)) shouldBe 100
        }

        "should reserve two tickets for a show" {
            val showsForToday = listOf(
                Show(100,100, ShowTime.MORNING, LocalDate.of(2021, 8, 19),"ABCD"),
                Show(100,100, ShowTime.AFTERNOON, LocalDate.of(2021, 8, 19),"ABCD"),
                Show(100,100, ShowTime.EVENING, LocalDate.of(2021, 8, 19),"ABCD")
            )

            val movieHall = MovieHall(showsForToday)
            val (newBookMyShowState, ticket) = movieHall.reserve(ShowTime.MORNING, LocalDate.of(2021, 8, 19))
            val (newBookMyShowState1, ticket1) = newBookMyShowState.reserve(ShowTime.MORNING, LocalDate.of(2021, 8, 19))

            ticket shouldBe Ticket(100, ShowTime.MORNING, LocalDate.of(2021, 8, 19))
            ticket1 shouldBe Ticket(99, ShowTime.MORNING, LocalDate.of(2021, 8, 19))
            newBookMyShowState1.totalReservedTicketsFor(ShowTime.MORNING, LocalDate.of(2021, 8, 19)) shouldBe 98
            newBookMyShowState1.totalReservedTicketsFor(ShowTime.AFTERNOON, LocalDate.of(2021, 8, 19)) shouldBe 100
            newBookMyShowState1.totalReservedTicketsFor(ShowTime.EVENING, LocalDate.of(2021, 8, 19)) shouldBe 100
        }

        "should return error if all the tickets are sold out for a show" {
            val showsForToday = listOf(
                Show(100,1, ShowTime.MORNING, LocalDate.of(2021, 8, 21),"ABCD"),
                Show(100,100, ShowTime.AFTERNOON, LocalDate.of(2021, 8, 21),"ABCD"),
                Show(100,100, ShowTime.EVENING, LocalDate.of(2021, 8, 21),"ABCD")
            )
            val exception = shouldThrow<IllegalStateException> {
                val movieHall = MovieHall(showsForToday)
                val (newBookMyShowState, _) = movieHall.reserve(ShowTime.MORNING, LocalDate.of(2021, 8, 21))
                newBookMyShowState.reserve(ShowTime.MORNING, LocalDate.of(2021, 8, 21))
            }

            exception.message shouldBe "Seats are filled"
        }

        "should return error if a show is not defined and it's information is accessed" {
            val showsForToday = listOf(
                Show(100,100, ShowTime.MORNING, LocalDate.of(2021, 8, 21),"ABCD"),
            )
            val movieHall = MovieHall(showsForToday)
            val (newBookMyShowState, ticket) = movieHall.reserve(ShowTime.MORNING, LocalDate.of(2021, 8, 21))
            ticket shouldBe Ticket(100, ShowTime.MORNING, LocalDate.of(2021, 8, 21))

            val exception = shouldThrow<NoSuchElementException> {
                newBookMyShowState.totalReservedTicketsFor(ShowTime.MORNING, LocalDate.of(2021, 8, 19)) shouldBe 100
            }

            exception.message shouldBe "Collection contains no element matching the predicate."
        }

        "should return error if a show is defined for after 7 days from the current date" {
            val exception = shouldThrow<IllegalStateException> {
                val showsForToday = listOf(
                    Show(100,1, ShowTime.MORNING, LocalDate.of(2021, 8, 29),"ABCD"),
                    Show(100,100, ShowTime.AFTERNOON, LocalDate.of(2021, 8, 21),"ABCD"),
                    Show(100,100, ShowTime.EVENING, LocalDate.of(2021, 8, 21),"ABCD")
                )
                MovieHall(showsForToday)
            }

            exception.message shouldBe "Show not available for date: 2021-08-29"
        }

        "should return error if a show is defined before the current date" {
            val exception = shouldThrow<IllegalStateException> {
                val showsForToday = listOf(
                    Show(100,1, ShowTime.MORNING, LocalDate.of(2021, 8, 16),"ABCD"),
                    Show(100,100, ShowTime.AFTERNOON, LocalDate.of(2021, 8, 21),"ABCD"),
                    Show(100,100, ShowTime.EVENING, LocalDate.of(2021, 8, 21),"ABCD")
                )
                MovieHall(showsForToday)
            }

            exception.message shouldBe "Show not available for date: 2021-08-16"
        }

        "should return name of the movie for a particular show" {
            val showsForToday = listOf(
                Show(100,100, ShowTime.MORNING, LocalDate.of(2021, 8, 19),"ABCD"),
                Show(100,100, ShowTime.AFTERNOON, LocalDate.of(2021, 8, 19),"ABCD2"),
                Show(100,100, ShowTime.EVENING, LocalDate.of(2021, 8, 19),"ABCD3")
            )

            val movieHall = MovieHall(showsForToday)
            val (newBookMyShowState,_) = movieHall.reserve(ShowTime.MORNING, LocalDate.of(2021, 8, 19))

            newBookMyShowState.getMovieNameForAShow(ShowTime.MORNING,LocalDate.of(2021, 8, 19)) shouldBe "ABCD"
            newBookMyShowState.getMovieNameForAShow(ShowTime.AFTERNOON,LocalDate.of(2021, 8, 19)) shouldBe "ABCD2"
            newBookMyShowState.getMovieNameForAShow(ShowTime.EVENING,LocalDate.of(2021, 8, 19)) shouldBe "ABCD3"
        }
    }
}