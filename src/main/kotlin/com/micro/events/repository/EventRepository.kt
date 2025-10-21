package com.micro.events.repository

import com.micro.events.model.entity.EventEntity
import com.micro.events.model.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<EventEntity, Long> {

    fun findAllByUserEntity(userEntity: UserEntity): List<EventEntity>
    fun deleteAllByUserEntity(userEntity: UserEntity)

    fun existsByUserEntityAndId(userEntity: UserEntity, id: Long): Boolean
}