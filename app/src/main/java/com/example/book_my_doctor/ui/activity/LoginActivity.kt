package com.example.book_my_doctor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.book_my_doctor.R
import com.example.book_my_doctor.databinding.ActivityLoginBinding
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.utils.LoadingUtils
import com.example.book_my_doctor.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupClickListeners()
        setupWindowInsets()
    }

    private fun setupViewModel() {
        val userRepository = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepository)
        loadingUtils = LoadingUtils(this)
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            if (validateInputs()) {
                performLogin()
            }
        }

        binding.btnSignupnavigate.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.btnForgot.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java))
        }
    }

    private fun validateInputs(): Boolean {
        val email = binding.email.text?.toString()?.trim() ?: ""
        val password = binding.passwordInput.text?.toString()?.trim() ?: ""

        when {
            email.isEmpty() -> {
                showToast("Please enter email")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Please enter a valid email")
                return false
            }
            password.isEmpty() -> {
                showToast("Please enter password")
                return false
            }
        }
        return true
    }

    private fun performLogin() {
        try {
            loadingUtils.show()

            val email = binding.email.text?.toString()?.trim() ?: ""
            val password = binding.passwordInput.text?.toString()?.trim() ?: ""

            userViewModel.login(email, password) { success, message ->
                try {
                    loadingUtils.dismiss()
                    showToast(message ?: "Login failed")

                    if (success) {
                        val currentUser = userViewModel.getCurrentUser()
                        currentUser?.let { user ->
                            // Fetch user data without callback
                            userViewModel.getUserFromDatabase(user.uid)
                            // Observe LiveData to get the full name
                            userViewModel.userData.observe(this) { userModel ->
                                if (userModel != null) {
                                    val fullName = "${userModel.firstName} ${userModel.lastName}"
                                    navigateToHome(fullName)
                                } else {
                                    showToast("Failed to fetch user data")
                                    navigateToHome() // Fallback to "User"
                                }
                                // Remove observer after navigation to avoid multiple triggers
                                userViewModel.userData.removeObservers(this)
                            }
                        } ?: run {
                            showToast("No current user found")
                            navigateToHome() // Fallback if no user
                        }
                    }
                } catch (e: Exception) {
                    loadingUtils.dismiss()
                    showToast("Error during login: ${e.message}")
                }
            }
        } catch (e: Exception) {
            loadingUtils.dismiss()
            showToast("Error: ${e.message}")
        }
    }

    private fun navigateToHome(fullName: String = "User") {
        try {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
                putExtra("FULL_NAME", fullName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            showToast("Error navigating to main screen: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupWindowInsets() {
        try {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        } catch (e: Exception) {
            // Silently handle insets error as it's not critical
        }
    }
}