package com.example.book_my_doctor.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.book_my_doctor.adapter.WishlistAdapter
import com.example.book_my_doctor.databinding.FragmentWishlistBinding
import com.example.book_my_doctor.ui.activity.HomeActivity.DoctorData

class WishlistFragment : Fragment() {
    private lateinit var binding: FragmentWishlistBinding
    private val doctorList = mutableListOf<DoctorData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getSerializable("WISHLIST_DOCTORS")?.let {
            doctorList.clear()
            doctorList.addAll(it as ArrayList<DoctorData>)
        }

        val adapter = WishlistAdapter(doctorList)
        binding.wishlistRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.wishlistRecyclerView.adapter = adapter

        if (doctorList.isEmpty()) {
            binding.wishlistRecyclerView.visibility = View.GONE
            binding.emptyWishlistText.visibility = View.VISIBLE
        } else {
            binding.wishlistRecyclerView.visibility = View.VISIBLE
            binding.emptyWishlistText.visibility = View.GONE
        }
    }
}