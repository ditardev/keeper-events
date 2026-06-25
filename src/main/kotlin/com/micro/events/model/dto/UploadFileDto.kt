package com.micro.events.model.dto

import com.micro.events.model.ImportType

data class UploadFileDto(
    val type: ImportType = ImportType.IMPORT,
    val dtoList: List<EventDto>,
)


