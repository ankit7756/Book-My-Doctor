package com.example.book_my_doctor.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.book_my_doctor.adapter.NotificationAdapter
import com.example.book_my_doctor.databinding.ActivityNotificationBinding
import com.example.book_my_doctor.model.NotificationModel
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var userViewModel: UserViewModel
    private val notificationList = mutableListOf<NotificationModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        setupRecyclerView()
        setupClearAllButton() // Updated method
        loadNotifications()
        setupWindowInsets()
    }

    private fun setupRecyclerView() {
        val adapter = NotificationAdapter(notificationList) { notification ->
            // Delete individual notification
            userViewModel.deleteNotification(notification.notificationId) { success, message ->
                if (success) {
                    notificationList.remove(notification)
                    binding.notificationsRecyclerView.adapter?.notifyDataSetChanged()
                    updateEmptyState()
                    Toast.makeText(this, "Notification deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to delete: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notificationsRecyclerView.adapter = adapter
    }

    private fun setupClearAllButton() {
        binding.tvClearAll.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                userViewModel.clearAllNotifications(it.uid) { success, message ->
                    if (success) {
                        notificationList.clear()
                        binding.notificationsRecyclerView.adapter?.notifyDataSetChanged()
                        updateEmptyState()
                        Toast.makeText(this, "All notifications cleared", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to clear: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loadNotifications() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            userViewModel.getNotifications(it.uid) { notifications ->
                notificationList.clear()
                val sortedNotifications = notifications.sortedByDescending { it.timestamp }
                notificationList.addAll(sortedNotifications)
                binding.notificationsRecyclerView.adapter?.notifyDataSetChanged()
                updateEmptyState()
            }
        } ?: run {
            binding.emptyNotificationsText.visibility = android.view.View.VISIBLE
            binding.notificationsRecyclerView.visibility = android.view.View.GONE
        }
    }

    private fun updateEmptyState() {
        if (notificationList.isEmpty()) {
            binding.notificationsRecyclerView.visibility = android.view.View.GONE
            binding.emptyNotificationsText.visibility = android.view.View.VISIBLE
        } else {
            binding.notificationsRecyclerView.visibility = android.view.View.VISIBLE
            binding.emptyNotificationsText.visibility = android.view.View.GONE
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}