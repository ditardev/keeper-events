package com.micro.events.service

import com.micro.events.model.entity.UserEntity

interface UserService {

    fun findOrCreate(userUUID: String?): UserEntity

}