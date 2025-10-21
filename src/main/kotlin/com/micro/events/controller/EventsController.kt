package com.micro.events.controller

import com.micro.events.appconfig.model.ApiRequest
import com.micro.events.appconfig.model.ApiResponse
import com.micro.events.appconfig.model.RequestInfoDto
import com.micro.events.model.dto.EventDto
import com.micro.events.service.EventService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${server.endpoint.main}/events")
class EventsController(
    private val eventService: EventService
) {
    @PostMapping("/all")
    fun selectAll(@RequestBody request: ApiRequest<RequestInfoDto>): ResponseEntity<*>? {
        val data = eventService.selectAll(request.userUUID)
        return ResponseEntity.ok(ApiResponse.Success(true, data))
    }

    @PostMapping("/create")
    fun create(@RequestBody request: ApiRequest<EventDto>): ResponseEntity<*>? {
        val data = eventService.create(request.userUUID, request.data)
        return ResponseEntity.ok(ApiResponse.Success(true, data))
    }

    @PutMapping("/update")
    fun update(@RequestBody request: ApiRequest<EventDto>): ResponseEntity<*>? {
        val data = eventService.update(request.userUUID, request.data)
        return ResponseEntity.ok(ApiResponse.Success(true, data))
    }

    @PostMapping("/delete")
    fun delete(@RequestBody request: ApiRequest<List<Long>>): ResponseEntity<*>? {
        val data = eventService.delete(request.userUUID, request.data)
        return ResponseEntity.ok(ApiResponse.Success(true, data))
    }
}