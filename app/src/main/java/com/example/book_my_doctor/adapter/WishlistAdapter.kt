package com.example.book_my_doctor.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.book_my_doctor.R
import com.example.book_my_doctor.ui.activity.BookAppointmentActivity
import com.example.book_my_doctor.ui.activity.HomeActivity.DoctorData
import com.google.android.material.button.MaterialButton

class WishlistAdapter(private val doctorList: List<DoctorData>) :
    RecyclerView.Adapter<WishlistAdapter.DoctorViewHolder>() {

    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorImage: ImageView = itemView.findViewById(R.id.doctorImage)
        val doctorName: TextView = itemView.findViewById(R.id.doctorName)
        val doctorSpecialty: TextView = itemView.findViewById(R.id.doctorSpecialty)
        val doctorRating: TextView = itemView.findViewById(R.id.doctorRating)
        val doctorExperience: TextView = itemView.findViewById(R.id.doctorExperience)
        val btnBookAppointment: MaterialButton = itemView.findViewById(R.id.btnBookAppointment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_card, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]
        holder.doctorName.text = doctor.name
        holder.doctorSpecialty.text = doctor.specialty
        holder.doctorRating.text = doctor.rating
        holder.doctorExperience.text = doctor.experience
        holder.doctorImage.setImageResource(doctor.imageResId)

        holder.btnBookAppointment.setOnClickListener {
            val intent = Intent(holder.itemView.context, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = doctorList.size
}