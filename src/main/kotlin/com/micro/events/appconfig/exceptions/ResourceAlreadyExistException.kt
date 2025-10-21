package com.micro.events.appconfig.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
data class ResourceAlreadyExistException(
    val msg: String?,
    val httpStatus: HttpStatus = HttpStatus.CONFLICT
) : RuntimeException(msg)