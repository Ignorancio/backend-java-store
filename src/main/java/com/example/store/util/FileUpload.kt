package com.example.store.util

import org.springframework.web.multipart.MultipartFile

interface FileUpload {
    fun uploadFile(directory: String, file: MultipartFile): String
}