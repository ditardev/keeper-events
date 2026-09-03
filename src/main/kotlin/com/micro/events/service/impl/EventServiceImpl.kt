package com.micro.events.service.impl

import com.micro.events.appconfig.exceptions.ResourceNotFoundException
import com.micro.events.appconfig.utility.Messages
import com.micro.events.model.ImportType
import com.micro.events.model.dto.EventDto
import com.micro.events.model.entity.EventEntity
import com.micro.events.model.entity.UserEntity
import com.micro.events.repository.EventRepository
import com.micro.events.service.EventService
import com.micro.events.service.UserService
import com.micro.events.service.extension.toDto
import com.micro.events.service.extension.toDtoList
import com.micro.events.service.extension.toEntity
import com.micro.events.service.extension.toEntityList
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
            throw ResourceNotFoundException(" ${Messages.EVENT_NOT_FOUND_NAME + eventDto.name}")
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
                throw ResourceNotFoundException("${Messages.EVENT_NOT_FOUND_ID}$id")
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