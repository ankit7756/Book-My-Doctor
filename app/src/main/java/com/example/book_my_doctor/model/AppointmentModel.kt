package com.example.book_my_doctor.model

import java.io.Serializable

data class AppointmentModel(
    val appointmentId: String = "",
    val userId: String = "",
    val doctorName: String = "",
    val doctorSpecialty: String = "",
    val dateAndDay: String = "", // e.g., "2025-02-23 (Sunday)"
    val timeSlot: String = "",
    val status: String = "Pending"
) : Serializable