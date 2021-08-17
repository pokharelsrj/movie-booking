package com.movie

import java.time.LocalDate

data class Show(val numberOfSeats: Int, val showTime: ShowTime, val showDate: LocalDate) {
    fun decrementSeats(): Show {
        if (numberOfSeats < 1)
            throw error("Seats are filled")

        return Show(numberOfSeats - 1, showTime, showDate)
    }
}