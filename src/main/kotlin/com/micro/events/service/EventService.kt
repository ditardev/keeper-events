package com.micro.events.service

import com.micro.events.appconfig.exceptions.ResourceNotFoundException
import com.micro.events.model.dto.EventDto
import com.micro.events.repository.EventRepository
import com.micro.events.service.converter.EventConverter
import org.springframework.stereotype.Service

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val converter: EventConverter,
    private val userService: UserService,
) {
    fun selectAll(userUUID: String?): List<EventDto>? {
        val userEntity = userService.findOrCreate(userUUID)
        val events = eventRepository.findAllByUserEntity(userEntity)
        return events?.let { converter.convertToDtos(events) }
    }

    fun create(userUUID: String, eventDto: EventDto): EventDto {
        val userEntity = userService.findOrCreate(userUUID)
        val eventEntity = eventRepository.save(converter.convertToEntity(userEntity, eventDto))
        return converter.convertToDto(eventEntity)
    }

    fun update(userUUID: String, eventDto: EventDto): EventDto {
        val userEntity = userService.findOrCreate(userUUID)
        if (eventRepository.existsByUserEntityAndId(userEntity, eventDto.id!!)) {
            throw ResourceNotFoundException("Event with name ${eventDto.fullName} not found")
        }
        val eventEntity = eventRepository.save(converter.convertToEntity(userEntity, eventDto))
        return converter.convertToDto(eventEntity)
    }

    fun delete(userUUID: String, idList: List<Long>): Int {
        val userEntity = userService.findOrCreate(userUUID)
        val existedIdList = mutableListOf<Long>()
        for (id in idList) {
            if (!eventRepository.existsByUserEntityAndId(userEntity, id)) {
                throw ResourceNotFoundException("Event with id $id not found")
            }
            existedIdList.add(id)
        }
        existedIdList.forEach { id -> eventRepository.deleteById(id) }
        return existedIdList.size
    }
}