package com.micro.events.model.dto

data class UploadFileDto(
    val type: ImportType = ImportType.IMPORT,
    val json: List<EventDto>,
)

enum class ImportType() {
    IMPORT,
    REPLACE;
}

