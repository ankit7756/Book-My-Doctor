package com.example.book_my_doctor.repository

import com.example.book_my_doctor.model.AppointmentModel
import com.example.book_my_doctor.model.NotificationModel
import com.example.book_my_doctor.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit)
    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit)
    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit)
    fun getCurrentUser(): FirebaseUser?
    fun getUserFromDatabase(userId: String, callback: (UserModel?, Boolean, String) -> Unit)
    fun getAppointments(userId: String, callback: (List<AppointmentModel>) -> Unit)
    fun saveAppointment(appointment: AppointmentModel, callback: (Boolean, String?) -> Unit)
    fun cancelAppointment(appointmentId: String, callback: (Boolean, String?) -> Unit)
    fun saveNotification(notification: NotificationModel, callback: (Boolean, String?) -> Unit)
    fun getNotifications(userId: String, callback: (List<NotificationModel>) -> Unit)
    fun deleteNotification(notificationId: String, callback: (Boolean, String?) -> Unit)
    fun clearAllNotifications(userId: String, callback: (Boolean, String?) -> Unit)
}