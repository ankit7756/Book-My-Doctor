package com.example.book_my_doctor.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.book_my_doctor.databinding.FragmentProfileBinding
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.viewmodel.UserViewModel


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,
                                container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var repo =UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        var currentUser = userViewModel.getCurrentUser()

        currentUser.let {
            userViewModel.getUserFromDatabase(it?.uid.toString())
        }

        userViewModel.userData.observe(requireActivity()){users->
            binding.profileEmail.text = users?.email
            binding.profileName.text = users?.firstName + " " +users?.lastName
        }

    }

}