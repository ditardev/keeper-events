package com.micro.events.service.converter

import com.micro.events.model.dto.EventDto
import com.micro.events.model.entity.EventEntity
import com.micro.events.model.entity.UserEntity
import com.micro.events.service.utils.DaysCalculator
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class EventConverter(
    val daysCalculator: DaysCalculator
) {
    fun convertToEntities(userEntity: UserEntity, dtos: List<EventDto>): List<EventEntity> {
        return dtos.stream()
            .map { it -> convertToEntity(userEntity, it) }
            .collect(Collectors.toList())
    }

    fun convertToEntity(userEntity: UserEntity, dto: EventDto): EventEntity {
        return EventEntity(
            id = dto.id,
            name = dto.name,
            date = dto.date,
            notify = dto.notify,
            type = dto.type,
            description = dto.description,
        )
    }

    fun convertToDtos(entityList: List<EventEntity>): List<EventDto> {
        return entityList.stream()
            .map { it -> convertToDto(it) }
            .collect(Collectors.toList())
    }

    fun convertToDto(entity: EventEntity): EventDto {
        return EventDto(
            id = entity.id,
            name = entity.name,
            date = entity.date,
            notify = entity.notify,
            type = entity.type,
            description = entity.description,
            daysLeft = daysCalculator.countBetweenToday(entity.date)
        )
    }
}