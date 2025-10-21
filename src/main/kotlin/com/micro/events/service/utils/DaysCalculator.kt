package com.micro.events.service.utils

import org.springframework.stereotype.Component
import java.sql.Date
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Component
class DaysCalculator(
) {
    fun countBetweenToday(date: Date?): String{
        val today = LocalDate.now()
        val date2 = date?.toLocalDate()

        val daysBetween = today.until(date2, ChronoUnit.DAYS)
        return daysBetween.toString()
    }
}