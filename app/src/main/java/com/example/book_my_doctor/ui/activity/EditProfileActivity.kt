package com.example.book_my_doctor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.book_my_doctor.databinding.ActivityEditProfileBinding
import com.example.book_my_doctor.model.NotificationModel
import com.example.book_my_doctor.model.UserModel
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.utils.LoadingUtils
import com.example.book_my_doctor.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupClickListeners()
        loadCurrentUserData()
        setupWindowInsets()
    }

    private fun setupViewModel() {
        val userRepository = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepository)
        loadingUtils = LoadingUtils(this)
    }

    private fun setupClickListeners() {
        binding.btnConfirm.setOnClickListener {
            if (validateInputs()) {
                updateProfile()
            }
        }

        binding.tvBackToHome.setOnClickListener {
            (this as? HomeActivity)?.showHomeContent()
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        val fname = binding.editFirstName.text?.toString()?.trim() ?: ""
        val lname = binding.editLastName.text?.toString()?.trim() ?: ""
        val email = binding.editEmail.text?.toString()?.trim() ?: ""
        val contact = binding.editContact.text?.toString()?.trim() ?: ""

        when {
            fname.isEmpty() -> {
                showToast("Please enter first name")
                return false
            }
            lname.isEmpty() -> {
                showToast("Please enter last name")
                return false
            }
            email.isEmpty() -> {
                showToast("Please enter email")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Please enter a valid email")
                return false
            }
            contact.isEmpty() -> {
                showToast("Please enter contact number")
                return false
            }
        }
        return true
    }

    private fun loadCurrentUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            userViewModel.getUserFromDatabase(user.uid)
            userViewModel.userData.observe(this) { userModel ->
                if (userModel != null) {
                    binding.editFirstName.setText(userModel.firstName)
                    binding.editLastName.setText(userModel.lastName)
                    binding.editEmail.setText(userModel.email)
                    binding.editContact.setText(userModel.phoneNumber)
                }
            }
        }
    }

    private fun updateProfile() {
        loadingUtils.show()

        val fname = binding.editFirstName.text?.toString()?.trim() ?: ""
        val lname = binding.editLastName.text?.toString()?.trim() ?: ""
        val email = binding.editEmail.text?.toString()?.trim() ?: ""
        val contact = binding.editContact.text?.toString()?.trim() ?: ""
        val password = binding.editPassword.text?.toString()?.trim() ?: ""

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val updatedUser = UserModel(
                userId = user.uid,
                firstName = fname,
                lastName = lname,
                email = email,
                phoneNumber = contact,
                address = "" // Keep as empty, not editable here
            )

            // Update Firebase database
            userViewModel.addUserToDatabase(user.uid, updatedUser) { success, message ->
                if (success) {
                    // Update email in Firebase Auth if changed
                    if (email != currentUser.email) {
                        currentUser.updateEmail(email).addOnCompleteListener { emailTask ->
                            if (!emailTask.isSuccessful) {
                                showToast("Failed to update email: ${emailTask.exception?.message}")
                            }
                        }
                    }
                    // Update password if provided
                    if (password.isNotEmpty()) {
                        currentUser.updatePassword(password).addOnCompleteListener { passwordTask ->
                            if (!passwordTask.isSuccessful) {
                                showToast("Failed to update password: ${passwordTask.exception?.message}")
                            }
                        }
                    }
                    showToast("Profile updated successfully")
                    // Save notification to Firebase
                    userViewModel.saveNotification(
                        NotificationModel(
                            notificationId = FirebaseDatabase.getInstance().reference.child("notifications").push().key ?: "",
                            userId = user.uid,
                            message = "Your profile has been successfully updated."
                        )
                    ) { _, _ -> }
                    navigateToProfile()
                } else {
                    showToast(message ?: "Failed to update profile")
                }
                loadingUtils.dismiss()
            }
        } ?: showToast("No user logged in")
    }

    private fun navigateToProfile() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("FULL_NAME", "${binding.editFirstName.text} ${binding.editLastName.text}")
            putExtra("NAVIGATE_TO_PROFILE", true) // Flag to switch to ProfileFragment
        }
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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