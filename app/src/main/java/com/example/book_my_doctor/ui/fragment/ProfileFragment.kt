package com.example.book_my_doctor.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.book_my_doctor.databinding.FragmentProfileBinding
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.ui.activity.EditProfileActivity
import com.example.book_my_doctor.ui.activity.HomeActivity
import com.example.book_my_doctor.ui.activity.MainActivity
import com.example.book_my_doctor.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        val currentUser = userViewModel.getCurrentUser()

        currentUser?.let {
            userViewModel.getUserFromDatabase(it.uid)
        }

        userViewModel.userData.observe(viewLifecycleOwner) { users ->
            if (users != null) {
                binding.profileName.text = "${users.firstName} ${users.lastName}"
                binding.profileEmail.text = users.email
                binding.profileContact.text = users.phoneNumber
            } else {
                binding.profileName.text = "Guest"
                binding.profileEmail.text = "Not logged in"
                binding.profileContact.text = "N/A"
            }
        }

        // Logout button
        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().finish()
        }

        binding.editProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Back to Home button
        binding.backToHome.setOnClickListener {
            (activity as? HomeActivity)?.showHomeContent()
        }
    }
}