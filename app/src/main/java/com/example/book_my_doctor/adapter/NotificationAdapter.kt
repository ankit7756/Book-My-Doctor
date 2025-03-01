package com.example.book_my_doctor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.book_my_doctor.R
import com.example.book_my_doctor.model.NotificationModel
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter(
    private val notificationList: List<NotificationModel>,
    private val onDeleteClick: (NotificationModel) -> Unit // Added callback
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.notificationMessage)
        val timestamp: TextView = itemView.findViewById(R.id.notificationTimestamp)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDeleteNotification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.message.text = notification.message
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        holder.timestamp.text = dateFormat.format(notification.timestamp)

        holder.btnDelete.setOnClickListener {
            onDeleteClick(notification)
        }
    }

    override fun getItemCount(): Int = notificationList.size
}