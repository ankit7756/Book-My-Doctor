package com.example.book_my_doctor.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.book_my_doctor.model.AppointmentModel
import com.example.book_my_doctor.model.NotificationModel
import com.example.book_my_doctor.model.UserModel
import com.example.book_my_doctor.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, password, callback)
    }

    fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        repo.signup(email, password, callback)
    }

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgetPassword(email, callback)
    }

    fun addUserToDatabase(userId: String, userModel: UserModel, callback: (Boolean, String) -> Unit) {
        repo.addUserToDatabase(userId, userModel, callback)
    }

    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }

    private val _userData = MutableLiveData<UserModel?>()
    val userData: MutableLiveData<UserModel?> get() = _userData

    fun getUserFromDatabase(userId: String) {
        repo.getUserFromDatabase(userId) { userModel, success, _ ->
            if (success) {
                _userData.value = userModel
            } else {
                _userData.value = null
            }
        }
    }

    private val _appointments = MutableLiveData<List<AppointmentModel>>()
    val appointments: MutableLiveData<List<AppointmentModel>> get() = _appointments

    fun getAppointments(userId: String, callback: (List<AppointmentModel>) -> Unit) {
        repo.getAppointments(userId) { appointmentList ->
            _appointments.value = appointmentList
            callback(appointmentList)
        }
    }

    fun saveAppointment(appointment: AppointmentModel, callback: (Boolean, String?) -> Unit) {
        repo.saveAppointment(appointment, callback)
    }

    // New cancel method
    fun cancelAppointment(appointmentId: String, callback: (Boolean, String?) -> Unit) {
        repo.cancelAppointment(appointmentId, callback)
    }
    fun saveNotification(notification: NotificationModel, callback: (Boolean, String?) -> Unit) {
        repo.saveNotification(notification, callback)
    }

    fun getNotifications(userId: String, callback: (List<NotificationModel>) -> Unit) {
        repo.getNotifications(userId, callback)
    }

    fun deleteNotification(notificationId: String, callback: (Boolean, String?) -> Unit) {
        repo.deleteNotification(notificationId, callback)
    }

    fun clearAllNotifications(userId: String, callback: (Boolean, String?) -> Unit) {
        repo.clearAllNotifications(userId, callback)
    }
}