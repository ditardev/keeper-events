package com.micro.events.model.dto

import com.micro.events.model.RemindType
import java.sql.Date

data class EventDto(
    val id: Long? = null,
    val name: String? = null,
    val date: Date? = null,
    val notify: Boolean? = null,
    val remindType: RemindType? = null,
    val description: String? = null,
    val daysLeft: String? = null,
)