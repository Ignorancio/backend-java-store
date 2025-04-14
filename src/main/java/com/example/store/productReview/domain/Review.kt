package com.example.store.productReview.domain

data class Review(
    val id:Long,
    val productId: Long,
    val userId: Long,
    val rating: Int,
    val comment: String?,
)
