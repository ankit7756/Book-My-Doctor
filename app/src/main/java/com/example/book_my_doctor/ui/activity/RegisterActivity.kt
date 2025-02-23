package com.example.book_my_doctor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.book_my_doctor.databinding.ActivityRegisterBinding
import com.example.book_my_doctor.model.UserModel
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.utils.LoadingUtils
import com.example.book_my_doctor.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupClickListeners()
    }

    private fun setupViewModel() {
        val userRepository = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepository)
        loadingUtils = LoadingUtils(this)
    }

    private fun setupClickListeners() {
        binding.submitTask.setOnClickListener {
            if (validateInputs()) {
                performRegistration()
            }
        }

        binding.tvLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun validateInputs(): Boolean {
        val fname = binding.fname.text?.toString()?.trim() ?: ""
        val lname = binding.Lname.text?.toString()?.trim() ?: ""
        val email = binding.email.text?.toString()?.trim() ?: ""
        val contact = binding.contact.text?.toString()?.trim() ?: ""
        val password = binding.passwordInput.text?.toString()?.trim() ?: ""
        val confirmPassword = binding.confirmPasswordInput.text?.toString()?.trim() ?: ""

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
            contact.isEmpty() -> {
                showToast("Please enter contact number")
                return false
            }
            password.isEmpty() -> {
                showToast("Please enter password")
                return false
            }
            confirmPassword.isEmpty() -> {
                showToast("Please confirm your password")
                return false
            }
            password != confirmPassword -> {
                showToast("Passwords do not match")
                return false
            }
        }
        return true
    }

    private fun performRegistration() {
        try {
            loadingUtils.show()

            val email = binding.email.text?.toString()?.trim() ?: ""
            val password = binding.passwordInput.text?.toString()?.trim() ?: ""
            val fname = binding.fname.text?.toString()?.trim() ?: ""
            val lname = binding.Lname.text?.toString()?.trim() ?: ""
            val contact = binding.contact.text?.toString()?.trim() ?: ""

            userViewModel.signup(email, password) { success, message, userId ->
                try {
                    if (success && userId != null) {
                        val userModel = UserModel(
                            userId = userId,
                            firstName = fname,
                            lastName = lname,
                            email = email,
                            phoneNumber = contact,
                            address = "" // Empty string for address
                        )
                        addUser(userModel)
                    } else {
                        loadingUtils.dismiss()
                        showToast(message ?: "Registration failed")
                    }
                } catch (e: Exception) {
                    loadingUtils.dismiss()
                    showToast("Error creating user: ${e.message}")
                }
            }
        } catch (e: Exception) {
            loadingUtils.dismiss()
            showToast("Error: ${e.message}")
        }
    }

    private fun addUser(userModel: UserModel) {
        try {
            userViewModel.addUserToDatabase(userModel.userId, userModel) { success, message ->
                loadingUtils.dismiss()
                showToast(message)
                if (success) {
                    navigateToLogin(userModel.firstName, userModel.lastName)
                }
            }
        } catch (e: Exception) {
            loadingUtils.dismiss()
            showToast("Error adding user: ${e.message}")
        }
    }

    private fun navigateToLogin(firstName: String = "", lastName: String = "") {
        val intent = Intent(this, LoginActivity::class.java).apply {
            putExtra("FULL_NAME", "$firstName $lastName")
        }
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}