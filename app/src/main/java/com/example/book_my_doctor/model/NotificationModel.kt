package com.example.book_my_doctor.model

import java.io.Serializable

data class NotificationModel(
    val notificationId: String = "",
    val userId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Serializable