package com.example.book_my_doctor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.book_my_doctor.databinding.ActivityForgetPasswordBinding
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.utils.LoadingUtils
import com.example.book_my_doctor.viewmodel.UserViewModel

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()
        setupClickListeners()
    }

    private fun initializeComponents() {
        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)
        loadingUtils = LoadingUtils(this)
    }

    private fun setupClickListeners() {
        // Reset Password Button Click
        binding.btnForget.setOnClickListener {
            if (validateEmail()) {
                sendResetLink()
            }
        }

        // Back to Login Click
        binding.tvBackToLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.editEmailForget.text.toString().trim()

        return when {
            email.isEmpty() -> {
                binding.tilEmail.error = "Email is required"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = "Please enter a valid email"
                false
            }
            else -> {
                binding.tilEmail.error = null
                true
            }
        }
    }

    private fun sendResetLink() {
        loadingUtils.show()
        val email = binding.editEmailForget.text.toString().trim()

        userViewModel.forgetPassword(email) { success, message ->
            loadingUtils.dismiss()
            showToast(message)

            if (success) {
                // Wait a moment before finishing to let user read the success message
                binding.root.postDelayed({
                    navigateToLogin()
                }, 2000)
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}