package com.micro.events.service.impl

import com.micro.events.model.entity.UserEntity
import com.micro.events.repository.UserRepository
import com.micro.events.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService {

    @Transactional
    override fun findOrCreate(userUUID: String?): UserEntity {
        return userRepository.findUserEntityByUuid(UUID.fromString(userUUID))
            ?: return userRepository.save(UserEntity(null, UUID.fromString(userUUID)))
    }

}