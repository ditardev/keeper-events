package com.micro.events.service

import com.micro.events.model.entity.UserEntity
import com.micro.events.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun findOrCreate(userUUID: String?): UserEntity {
        return userRepository.findUserEntityByUuid(UUID.fromString(userUUID))
            ?: return userRepository.save(UserEntity(null, UUID.fromString(userUUID)))
    }
}