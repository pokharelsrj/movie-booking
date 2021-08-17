package com.movie

import java.time.LocalDate
import java.time.ZoneId

class MovieHall(private val shows: Collection<Show>) {

    private val showsForToday = HashSet<Show>()

    init {
        val today = LocalDate.now(ZoneId.of("Asia/Kolkata"))
        val dateLimit = today.plusDays(7)
        shows.map {
            if (!(it.showDate.isBefore(dateLimit) && it.showDate.isAfter(today)))
                throw error("Show not available for date: ${it.showDate}")
        }
        showsForToday.addAll(shows)
    }

    fun reserve(showTime: ShowTime, showDate: LocalDate): Pair<MovieHall, Ticket> {
        val showToBook = shows.first { it.showTime == showTime && it.showDate == showDate }
        val decrementedSeats = showToBook.decrementSeats()
        showsForToday.remove(showToBook)
        return Pair(MovieHall(showsForToday + decrementedSeats), Ticket(showToBook.numberOfSeats, showTime, showDate))
    }

    fun totalReservedTicketsFor(showTime: ShowTime, showDate: LocalDate): Int {
        return showsForToday.first { it.showTime == showTime && it.showDate == showDate }.numberOfSeats
    }
}


