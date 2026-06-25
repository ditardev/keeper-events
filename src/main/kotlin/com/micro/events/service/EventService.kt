package com.micro.events.service

import com.micro.events.model.ImportType
import com.micro.events.model.dto.EventDto
import com.micro.events.model.entity.EventEntity
import com.micro.events.model.entity.UserEntity

interface EventService {

    fun selectAll(userUUID: String?): List<EventDto>?
    fun create(userUUID: String, eventDto: EventDto): EventDto
    fun update(userUUID: String, eventDto: EventDto): EventDto
    fun delete(userUUID: String, idList: List<Long>): Int
    fun upload(userUUID: String, uploadType: ImportType, eventDtoList: List<EventDto>): Int
    fun import(inputList: List<EventEntity>)
    fun replace(userEntity: UserEntity, inputList: List<EventEntity>)

}