package com.micro.events.service.utils

import org.joda.time.Days
import org.joda.time.LocalDate
import org.springframework.stereotype.Component
import java.sql.Date

object DaysCalculator { // Меняем class на object, убираем @Component
    fun countBetweenToday(date: Date?): String {
        if (date == null) return ""
        val today = LocalDate.now()
        val dayAndMonth = LocalDate(date)
        var nextOccurrence = dayAndMonth.withYear(today.year)
        if (nextOccurrence.isBefore(today)) {
            nextOccurrence = nextOccurrence.plusYears(1)
        }
        val days = Days.daysBetween(today, nextOccurrence).days

        return when(days) {
            0 -> "Today"
            1 -> "Tomorrow"
            else -> days.toString()
        }
    }
}