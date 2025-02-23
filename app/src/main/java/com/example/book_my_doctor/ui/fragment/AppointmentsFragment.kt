package com.example.book_my_doctor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.book_my_doctor.adapter.AppointmentsAdapter
import com.example.book_my_doctor.databinding.FragmentAppointmentsBinding
import com.example.book_my_doctor.model.AppointmentModel
import com.example.book_my_doctor.model.NotificationModel
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AppointmentsFragment : Fragment() {
    private lateinit var binding: FragmentAppointmentsBinding
    private lateinit var userViewModel: UserViewModel
    private val appointmentList = mutableListOf<AppointmentModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        setupRecyclerView()
        loadAppointments()
    }

    private fun setupRecyclerView() {
        val adapter = AppointmentsAdapter(appointmentList) { appointment ->
            userViewModel.cancelAppointment(appointment.appointmentId) { success, message ->
                if (success) {
                    Toast.makeText(context, "Appointment cancelled successfully", Toast.LENGTH_SHORT).show()
                    userViewModel.saveNotification(
                        NotificationModel(
                            notificationId = FirebaseDatabase.getInstance().reference.child("notifications").push().key ?: "",
                            userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                            message = "Your appointment with ${appointment.doctorName} on ${appointment.dateAndDay} at ${appointment.timeSlot} has been successfully cancelled."
                        )
                    ) { _, _ -> }
                    appointmentList.remove(appointment)
                    binding.appointmentsRecyclerView.adapter?.notifyDataSetChanged()
                    updateEmptyState()
                } else {
                    Toast.makeText(context, "Failed to cancel: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.appointmentsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.appointmentsRecyclerView.adapter = adapter
    }

    private fun loadAppointments() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            userViewModel.getAppointments(it.uid) { appointments ->
                appointmentList.clear()
                appointmentList.addAll(appointments)
                binding.appointmentsRecyclerView.adapter?.notifyDataSetChanged()
                updateEmptyState()
            }
        } ?: run {
            binding.emptyAppointmentsText.visibility = View.VISIBLE
            binding.appointmentsRecyclerView.visibility = View.GONE
        }
    }

    private fun updateEmptyState() {
        if (appointmentList.isEmpty()) {
            binding.appointmentsRecyclerView.visibility = View.GONE
            binding.emptyAppointmentsText.visibility = View.VISIBLE
        } else {
            binding.appointmentsRecyclerView.visibility = View.VISIBLE
            binding.emptyAppointmentsText.visibility = View.GONE
        }
    }
}