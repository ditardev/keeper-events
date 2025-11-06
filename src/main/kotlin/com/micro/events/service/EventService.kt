package com.micro.events.service

import com.micro.events.appconfig.exceptions.ResourceAlreadyExistException
import com.micro.events.appconfig.exceptions.ResourceNotFoundException
import com.micro.events.model.dto.EventDto
import com.micro.events.model.dto.ImportType
import com.micro.events.model.dto.UploadFileDto
import com.micro.events.model.entity.EventEntity
import com.micro.events.model.entity.UserEntity
import com.micro.events.repository.EventRepository
import com.micro.events.service.converter.EventConverter
import org.springframework.dao.DataIntegrityViolationException
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
        return try {
            val eventEntity = eventRepository.save(converter.convertToEntity(userEntity, eventDto))
            converter.convertToDto(eventEntity)
        } catch (e: DataIntegrityViolationException) {
            throw ResourceAlreadyExistException("Resource already exists: ${e.message}")
        }
    }

    fun update(userUUID: String, eventDto: EventDto): EventDto {
        val userEntity = userService.findOrCreate(userUUID)
        if (eventRepository.existsByUserEntityAndId(userEntity, eventDto.id!!).not()) {
            throw ResourceNotFoundException("Event with name ${eventDto.name} not found")
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

    fun upload(userUUID: String, uploadFileDto: UploadFileDto): Int {
        val userEntity = userService.findOrCreate(userUUID)
        val inputEntities = converter.convertToEntities(userEntity, uploadFileDto.json)
        when (uploadFileDto.type) {
            ImportType.IMPORT -> import(inputEntities)
            ImportType.REPLACE -> replace(userEntity, inputEntities)
        }
        return inputEntities.size
    }
    fun import(inputList: List<EventEntity>) {
        eventRepository.saveAll(inputList)
    }

    fun replace(userEntity: UserEntity, inputList: List<EventEntity>) {
        val entityList = eventRepository.findAllByUserEntity(userEntity)
        entityList.forEach { entity -> eventRepository.deleteById(entity.id!!) }
        eventRepository.saveAll(inputList)
    }
}