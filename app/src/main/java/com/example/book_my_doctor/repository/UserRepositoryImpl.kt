package com.example.book_my_doctor.repository

import com.example.book_my_doctor.model.AppointmentModel
import com.example.book_my_doctor.model.NotificationModel
import com.example.book_my_doctor.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryImpl : UserRepository {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("users")
    val appointmentsReference: DatabaseReference = database.reference.child("appointments")
    val notificationsReference: DatabaseReference = database.reference.child("notifications") // Added

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Login successful")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun signup(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Register Success", auth.currentUser?.uid.toString())
            } else {
                callback(false, it.exception?.message.toString(), "")
            }
        }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Password Reset link sent to $email")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(userId).setValue(userModel)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Registered successfully")
                } else {
                    callback(false, it.exception?.message.toString())
                }
            }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getUserFromDatabase(
        userId: String,
        callback: (UserModel?, Boolean, String) -> Unit
    ) {
        reference.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val model = snapshot.getValue(UserModel::class.java)
                    callback(model, true, "Details fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun getAppointments(userId: String, callback: (List<AppointmentModel>) -> Unit) {
        appointmentsReference.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val appointmentList = mutableListOf<AppointmentModel>()
                    for (child in snapshot.children) {
                        val appointment = child.getValue(AppointmentModel::class.java)
                        appointment?.let { appointmentList.add(it) }
                    }
                    callback(appointmentList)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
    }

    override fun saveAppointment(appointment: AppointmentModel, callback: (Boolean, String?) -> Unit) {
        appointmentsReference.child(appointment.appointmentId).setValue(appointment)
            .addOnSuccessListener { callback(true, "Appointment booked successfully") }
            .addOnFailureListener { callback(false, it.message) }
    }

    override fun cancelAppointment(appointmentId: String, callback: (Boolean, String?) -> Unit) {
        appointmentsReference.child(appointmentId).removeValue()
            .addOnSuccessListener { callback(true, "Appointment cancelled successfully") }
            .addOnFailureListener { callback(false, it.message) }
    }

    // Added implementations for notifications
    override fun saveNotification(notification: NotificationModel, callback: (Boolean, String?) -> Unit) {
        notificationsReference.child(notification.notificationId).setValue(notification)
            .addOnSuccessListener { callback(true, "Notification saved") }
            .addOnFailureListener { callback(false, it.message) }
    }

    override fun getNotifications(userId: String, callback: (List<NotificationModel>) -> Unit) {
        notificationsReference.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val notificationList = mutableListOf<NotificationModel>()
                    for (child in snapshot.children) {
                        val notification = child.getValue(NotificationModel::class.java)
                        notification?.let { notificationList.add(it) }
                    }
                    callback(notificationList)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
    }

    override fun deleteNotification(notificationId: String, callback: (Boolean, String?) -> Unit) {
        notificationsReference.child(notificationId).removeValue()
            .addOnSuccessListener { callback(true, "Notification deleted") }
            .addOnFailureListener { callback(false, it.message) }
    }

    override fun clearAllNotifications(userId: String, callback: (Boolean, String?) -> Unit) {
        notificationsReference.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        child.ref.removeValue()
                    }
                    callback(true, "All notifications cleared")
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message)
                }
            })
    }
}