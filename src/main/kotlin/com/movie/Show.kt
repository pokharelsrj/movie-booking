package com.movie

import java.time.LocalDate

data class Show(val hallCapacity:Int, val numberOfSeats: Int, val showTime: ShowTime, val showDate: LocalDate, val movieName:String?) {
    fun decrementSeats(): Show {
        if (numberOfSeats < 1)
            throw error("Seats are filled")
        else if(movieName == null)
            throw error("Cannot issue ticket unless a movie is assigned to the show")

        return Show(hallCapacity,numberOfSeats - 1, showTime, showDate,movieName)
    }

    fun addMovieName(newMovieName:String):Show{
        if(hallCapacity != numberOfSeats)
            throw error("Movie name cannot be renamed")

      return Show(hallCapacity,numberOfSeats, showTime, showDate,newMovieName)
    }

}