package com.example.store.productReview.domain

interface ReviewService {
    fun findByProductId(productId: Long): List<Review>
    fun findByUserId(userId: Long): List<Review>
    fun findById(reviewId: Long): Review?
    fun save(review: Review): Review
    fun delete(reviewId: Long)
    fun update(review: Review): Review
}