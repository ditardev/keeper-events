package com.micro.events.repository

import com.micro.events.model.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findUserEntityByUuid(uuid: UUID): UserEntity?
}