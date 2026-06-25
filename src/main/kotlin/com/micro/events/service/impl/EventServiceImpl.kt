package com.micro.events.service.impl

import com.micro.events.appconfig.exceptions.ResourceNotFoundException
import com.micro.events.model.ImportType
import com.micro.events.model.dto.EventDto
import com.micro.events.model.entity.EventEntity
import com.micro.events.model.entity.UserEntity
import com.micro.events.repository.EventRepository
import com.micro.events.service.EventService
import com.micro.events.service.UserService
import com.micro.events.service.extention.toDto
import com.micro.events.service.extention.toDtoList
import com.micro.events.service.extention.toEntity
import com.micro.events.service.extention.toEntityList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventServiceImpl(
    private val eventRepository: EventRepository,
    private val userService: UserService,
) : EventService {

    @Transactional(readOnly = true)
    override fun selectAll(userUUID: String?): List<EventDto>? {
        val userEntity = userService.findOrCreate(userUUID)
        val events = eventRepository.findAllByUserEntity(userEntity)
        return events?.let { events.toDtoList() }
    }

    @Transactional
    override fun create(userUUID: String, eventDto: EventDto): EventDto {
        val userEntity = userService.findOrCreate(userUUID)
        return eventRepository.save(eventDto.toEntity(userEntity)).toDto()
    }

    @Transactional
    override fun update(userUUID: String, eventDto: EventDto): EventDto {
        val userEntity = userService.findOrCreate(userUUID)
        if (eventRepository.existsByUserEntityAndId(userEntity, eventDto.id!!).not()) {
            throw ResourceNotFoundException("Event with name ${eventDto.name} and id ${eventDto.id} not found")
        }
        val eventEntity = eventRepository.save(eventDto.toEntity(userEntity))
        return eventEntity.toDto()
    }

    @Transactional
    override fun delete(userUUID: String, idList: List<Long>): Int {
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

    @Transactional
    override fun upload(userUUID: String, uploadType: ImportType, eventDtoList: List<EventDto>): Int {
        val userEntity = userService.findOrCreate(userUUID)
        val inputEntities = eventDtoList.toEntityList(userEntity)
        when (uploadType) {
            ImportType.IMPORT -> import(inputEntities)
            ImportType.REPLACE -> replace(userEntity, inputEntities)
        }
        return inputEntities.size
    }

    @Transactional
    override fun import(inputList: List<EventEntity>) {
        eventRepository.saveAll(inputList)
    }

    @Transactional
    override fun replace(userEntity: UserEntity, inputList: List<EventEntity>) {
        val entityList = eventRepository.findAllByUserEntity(userEntity)
        entityList.forEach { entity -> eventRepository.deleteById(entity.id!!) }
        eventRepository.saveAll(inputList)
    }
}