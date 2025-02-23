package com.example.book_my_doctor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.book_my_doctor.R
import com.example.book_my_doctor.model.AppointmentModel
import com.google.android.material.button.MaterialButton

class AppointmentsAdapter(
    private val appointmentList: List<AppointmentModel>,
    private val onCancelClick: (AppointmentModel) -> Unit
) : RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorImage: ImageView = itemView.findViewById(R.id.appointmentDoctorImage)
        val doctorName: TextView = itemView.findViewById(R.id.appointmentDoctorName)
        val doctorSpecialty: TextView = itemView.findViewById(R.id.appointmentSpecialty)
        val dateAndDay: TextView = itemView.findViewById(R.id.appointmentDateAndDay)
        val timeSlot: TextView = itemView.findViewById(R.id.appointmentTimeSlot)
        val btnCancel: MaterialButton = itemView.findViewById(R.id.btnCancelAppointment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment_card, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointmentList[position]
        holder.doctorName.text = appointment.doctorName
        holder.doctorSpecialty.text = appointment.doctorSpecialty
        holder.dateAndDay.text = appointment.dateAndDay
        holder.timeSlot.text = appointment.timeSlot

        // Map doctor name to image
        when (appointment.doctorName) {
            "Dr. Ankit Sharma" -> holder.doctorImage.setImageResource(R.drawable.doc1)
            "Dr. Binnol Dahal" -> holder.doctorImage.setImageResource(R.drawable.doc2)
            "Dr. Dibya Sharma" -> holder.doctorImage.setImageResource(R.drawable.doc3)
            "Dr. Bigyan Guragain" -> holder.doctorImage.setImageResource(R.drawable.doc4)
            "Dr. Dua Lipa" -> holder.doctorImage.setImageResource(R.drawable.doc5)
            "Dr. Pratik Neupane" -> holder.doctorImage.setImageResource(R.drawable.doc6)
            "Dr. Nirjal Adhakari" -> holder.doctorImage.setImageResource(R.drawable.doc7)
            "Dr. Rojit Ale Magar" -> holder.doctorImage.setImageResource(R.drawable.doc8)
            "Dr. Isha Maharjan" -> holder.doctorImage.setImageResource(R.drawable.doc9)
            "Dr. Sandis Prajapati" -> holder.doctorImage.setImageResource(R.drawable.doc10)

            else -> holder.doctorImage.setImageResource(R.drawable.baseline_person_24)
        }

        holder.btnCancel.setOnClickListener {
            onCancelClick(appointment)
        }
    }

    override fun getItemCount(): Int = appointmentList.size
}