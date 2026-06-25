package com.micro.events.service.extention

import com.micro.events.model.dto.EventDto
import com.micro.events.model.entity.EventEntity
import com.micro.events.model.entity.UserEntity
import com.micro.events.service.utils.DaysCalculator

fun EventEntity.toDto(): EventDto = EventDto(
    id = id,
    name = name,
    date = date,
    notify = notify,
    remindType = remindType,
    description = description,
    daysLeft = DaysCalculator.countBetweenToday(date)
)

// Конвертация одного DTO в сущность
fun EventDto.toEntity(userEntity: UserEntity): EventEntity = EventEntity(
    id = id,
    name = name,
    date = date,
    notify = notify,
    remindType = remindType,
    description = description,
    userEntity = userEntity
)

fun Iterable<EventEntity>.toDtoList(): List<EventDto> {
    return this.map { it.toDto() }
}

fun Iterable<EventDto>.toEntityList(userEntity: UserEntity): List<EventEntity> {
    return this.map { it.toEntity(userEntity) }
}

