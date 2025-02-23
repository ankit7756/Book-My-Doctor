package com.example.book_my_doctor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.book_my_doctor.databinding.ActivityBookAppointmentBinding
import com.example.book_my_doctor.model.AppointmentModel
import com.example.book_my_doctor.model.NotificationModel
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.viewmodel.UserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.app.DatePickerDialog
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookAppointmentBinding
    private var selectedDate: String? = null
    private var selectedDay: String? = null
    private var selectedTimeSlot: String? = null
    private lateinit var doctor: HomeActivity.DoctorData
    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val calendar = Calendar.getInstance()
    private lateinit var userViewModel: UserViewModel // Added declaration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = UserRepositoryImpl() // Initialize UserViewModel
        userViewModel = UserViewModel(repo)

        loadDoctorData()
        setupDatePicker()
        setupTimeSlotDropdown()
        setupClickListeners()
        setupWindowInsets()
    }

    private fun loadDoctorData() {
        doctor = intent.getSerializableExtra("DOCTOR_DATA") as HomeActivity.DoctorData
        binding.doctorName.text = doctor.name
        binding.doctorSpecialty.text = doctor.specialty
        binding.doctorImage.setImageResource(doctor.imageResId)
    }

    private fun setupDatePicker() {
        binding.dateInput.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    val dayFormat = SimpleDateFormat("EEEE", Locale.US)
                    selectedDate = dateFormat.format(calendar.time)
                    selectedDay = dayFormat.format(calendar.time)
                    binding.dateInput.setText("$selectedDate ($selectedDay)")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }
    }

    private fun setupTimeSlotDropdown() {
        val timeSlots = arrayOf("Morning (7:00 AM - 11:00 AM)", "Afternoon (12:00 PM - 4:00 PM)", "Evening (5:00 PM - 8:00 PM)")
        val timeSlotAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, timeSlots)
        binding.timeSlotDropdown.setAdapter(timeSlotAdapter)
        binding.timeSlotDropdown.setOnItemClickListener { _, _, position, _ -> selectedTimeSlot = timeSlots[position] }
    }

    private fun setupClickListeners() {
        binding.btnBookAppointment.setOnClickListener {
            if (validateInputs()) {
                showBookingConfirmationDialog()
            }
        }

        binding.tvBackToHome.setOnClickListener {
            (this as? HomeActivity)?.showHomeContent()
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        if (selectedDate == null || selectedDay == null) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedTimeSlot == null) {
            Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showBookingConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Booking Confirmation")
            .setMessage("Doctor: ${doctor.name}\nDate: $selectedDate ($selectedDay)\nTime: $selectedTimeSlot\n\nWould you like to confirm this booking?")
            .setPositiveButton("Confirm") { _, _ ->
                saveAppointment()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveAppointment() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val appointment = AppointmentModel(
                appointmentId = databaseReference.child("appointments").push().key ?: "",
                userId = it.uid,
                doctorName = doctor.name,
                doctorSpecialty = doctor.specialty,
                dateAndDay = "$selectedDate ($selectedDay)",
                timeSlot = selectedTimeSlot ?: ""
            )
            databaseReference.child("appointments").child(appointment.appointmentId).setValue(appointment)
                .addOnSuccessListener {
                    userViewModel.saveNotification(
                        NotificationModel(
                            notificationId = databaseReference.child("notifications").push().key ?: "",
                            userId = user.uid, // Fixed: Use user.uid, not it.uid
                            message = "Your appointment with ${doctor.name} has been successfully booked for $selectedDate ($selectedDay) at $selectedTimeSlot."
                        )
                    ) { _: Boolean, _: String? -> } // Fixed: Explicit callback type
                    showBookingSuccessDialog()
                }
                .addOnFailureListener { Toast.makeText(this, "Failed to book: ${it.message}", Toast.LENGTH_SHORT).show() }
        } ?: Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
    }

    private fun showBookingSuccessDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Success!")
            .setMessage("Your appointment with ${doctor.name} has been booked successfully.")
            .setPositiveButton("OK") { _, _ ->
                navigateToHome()
            }
            .setCancelable(false)
            .show()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}