package com.example.book_my_doctor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.book_my_doctor.R
import com.example.book_my_doctor.databinding.ActivityHomeBinding
import com.example.book_my_doctor.repository.UserRepositoryImpl
import com.example.book_my_doctor.ui.fragment.AppointmentsFragment
import com.example.book_my_doctor.ui.fragment.ProfileFragment
import com.example.book_my_doctor.ui.fragment.WishlistFragment
import com.example.book_my_doctor.viewmodel.UserViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userViewModel: UserViewModel
    private val wishlistedDoctors = mutableListOf<DoctorData>()

    data class DoctorData(
        val name: String,
        val specialty: String,
        val rating: String = "4.8",
        val experience: String = "10 Years Experience",
        val imageResId: Int = R.drawable.baseline_person_24
    ) : java.io.Serializable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRepository = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepository)

        setupWelcomeText()
        setupBottomNavigation()
        setupDoctorCardListeners()
        setupNotificationBell()

        binding.scrollContent.visibility = android.view.View.VISIBLE
        binding.fragmentContainer.visibility = android.view.View.GONE

        if (intent.getBooleanExtra("NAVIGATE_TO_PROFILE", false)) {
            replaceFragment(ProfileFragment())
            binding.buttomNavigation.selectedItemId = com.example.book_my_doctor.R.id.menuProfile
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun setupWelcomeText() {
        val fullName = intent.getStringExtra("FULL_NAME") ?: "User"
        binding.welcomeText.text = "Hello, $fullName"
    }

    private fun setupBottomNavigation() {
        binding.buttomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.example.book_my_doctor.R.id.menuWishlist -> {
                    val fragment = WishlistFragment().apply {
                        arguments = Bundle().apply {
                            putSerializable("WISHLIST_DOCTORS", ArrayList(wishlistedDoctors))
                        }
                    }
                    replaceFragment(fragment)
                    true
                }
                com.example.book_my_doctor.R.id.menuAppointments -> {
                    replaceFragment(AppointmentsFragment())
                    true
                }
                com.example.book_my_doctor.R.id.menuProfile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun setupDoctorCardListeners() {
        // Doctor Card 1 - Dr. Ankit Sharma
        binding.btnBookAppointment1.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName1.text.toString(),
                specialty = binding.doctorSpeciality1.text.toString(),
                rating = "9.9",
                experience = "10 Years Experience",
                imageResId = R.drawable.doc1
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon1.setOnClickListener {
            val isWishlisted = binding.wishlistIcon1.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName1.text.toString(),
                specialty = binding.doctorSpeciality1.text.toString(),
                rating = "9.9",
                experience = "10 Years Experience",
                imageResId = R.drawable.doc1
            )
            if (!isWishlisted) {
                binding.wishlistIcon1.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon1.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon1.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon1.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Doctor Card 2 - Dr. Binnol Dahal
        binding.btnBookAppointment2.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName2.text.toString(),
                specialty = binding.doctorSpeciality2.text.toString(),
                rating = "6.6",
                experience = "6 Years Experience",
                imageResId = R.drawable.doc2
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon2.setOnClickListener {
            val isWishlisted = binding.wishlistIcon2.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName2.text.toString(),
                specialty = binding.doctorSpeciality2.text.toString(),
                rating = "6.6",
                experience = "6 Years Experience",
                imageResId = R.drawable.doc2
            )
            if (!isWishlisted) {
                binding.wishlistIcon2.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon2.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon2.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon2.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Doctor Card 3 - Dr. Dibya Sharma
        binding.btnBookAppointment3.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName3.text.toString(),
                specialty = binding.doctorSpeciality3.text.toString(),
                rating = "7.9",
                experience = "3 Years Experience",
                imageResId = R.drawable.doc3
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon3.setOnClickListener {
            val isWishlisted = binding.wishlistIcon3.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName3.text.toString(),
                specialty = binding.doctorSpeciality3.text.toString(),
                rating = "7.9",
                experience = "3 Years Experience",
                imageResId = R.drawable.doc3
            )
            if (!isWishlisted) {
                binding.wishlistIcon3.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon3.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon3.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon3.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Doctor Card 4 - Dr. Bigyan Guragain
        binding.btnBookAppointment4.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName4.text.toString(),
                specialty = binding.doctorSpeciality4.text.toString(),
                rating = "8.2",
                experience = "8 Years Experience",
                imageResId = R.drawable.doc4
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon4.setOnClickListener {
            val isWishlisted = binding.wishlistIcon4.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName4.text.toString(),
                specialty = binding.doctorSpeciality4.text.toString(),
                rating = "9.9",
                experience = "10 Years Experience",
                imageResId = R.drawable.doc4
            )
            if (!isWishlisted) {
                binding.wishlistIcon4.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon4.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon4.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon4.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Doctor Card 5 - Dr. Dua Lipa
        binding.btnBookAppointment5.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName5.text.toString(),
                specialty = binding.doctorSpeciality5.text.toString(),
                rating = "7.9",
                experience = "5 Years Experience",
                imageResId = R.drawable.doc5
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon5.setOnClickListener {
            val isWishlisted = binding.wishlistIcon5.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName5.text.toString(),
                specialty = binding.doctorSpeciality5.text.toString(),
                rating = "7.9",
                experience = "5 Years Experience",
                imageResId = R.drawable.doc5
            )
            if (!isWishlisted) {
                binding.wishlistIcon5.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon5.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon5.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon5.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Doctor Card 6 - Dr. Pratik Neupane
        binding.btnBookAppointment6.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName6.text.toString(),
                specialty = binding.doctorSpeciality6.text.toString(),
                rating = "6.7",
                experience = "6 Years Experience",
                imageResId = R.drawable.doc6
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon6.setOnClickListener {
            val isWishlisted = binding.wishlistIcon6.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName6.text.toString(),
                specialty = binding.doctorSpeciality6.text.toString(),
                rating = "6.7",
                experience = "6 Years Experience",
                imageResId = R.drawable.doc6
            )
            if (!isWishlisted) {
                binding.wishlistIcon6.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon6.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon6.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon6.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Doctor Card 7 - Dr. Nirjal Adhakari
        binding.btnBookAppointment7.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName7.text.toString(),
                specialty = binding.doctorSpeciality7.text.toString(),
                rating = "7.8",
                experience = "4 Years Experience",
                imageResId = R.drawable.doc7
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon7.setOnClickListener {
            val isWishlisted = binding.wishlistIcon7.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName7.text.toString(),
                specialty = binding.doctorSpeciality7.text.toString(),
                rating = "7.8",
                experience = "4 Years Experience",
                imageResId = R.drawable.doc7
            )
            if (!isWishlisted) {
                binding.wishlistIcon7.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon7.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon7.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon7.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Doctor Card 8 - Dr. Rojit Ale Magar
        binding.btnBookAppointment8.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName8.text.toString(),
                specialty = binding.doctorSpeciality8.text.toString(),
                rating = "6.9",
                experience = "69 Years Experience",
                imageResId = R.drawable.doc8
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon8.setOnClickListener {
            val isWishlisted = binding.wishlistIcon8.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName8.text.toString(),
                specialty = binding.doctorSpeciality8.text.toString(),
                rating = "6.9",
                experience = "69 Years Experience",
                imageResId = R.drawable.doc8
            )
            if (!isWishlisted) {
                binding.wishlistIcon8.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon8.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon8.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon8.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Doctor Card 9 - Dr. Isha Maharjan
        binding.btnBookAppointment9.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName9.text.toString(),
                specialty = binding.doctorSpeciality9.text.toString(),
                rating = "4.7",
                experience = "2 Years Experience",
                imageResId = R.drawable.doc9
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon9.setOnClickListener {
            val isWishlisted = binding.wishlistIcon9.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName9.text.toString(),
                specialty = binding.doctorSpeciality9.text.toString(),
                rating = "4.7",
                experience = "2 Years Experience",
                imageResId = R.drawable.doc9
            )
            if (!isWishlisted) {
                binding.wishlistIcon9.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon9.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon9.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon9.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Doctor Card 10 - Dr. Sandis Prajapati
        binding.btnBookAppointment10.setOnClickListener {
            val doctor = DoctorData(
                name = binding.doctorName10.text.toString(),
                specialty = binding.doctorSpeciality10.text.toString(),
                rating = "9.3",
                experience = "9 Years Experience",
                imageResId = R.drawable.doc10
            )
            val intent = Intent(this, BookAppointmentActivity::class.java).apply {
                putExtra("DOCTOR_DATA", doctor)
            }
            startActivity(intent)
        }
        binding.wishlistIcon10.setOnClickListener {
            val isWishlisted = binding.wishlistIcon10.tag == "on"
            val doctor = DoctorData(
                name = binding.doctorName10.text.toString(),
                specialty = binding.doctorSpeciality10.text.toString(),
                rating = "9.3",
                experience = "9 Years Experience",
                imageResId = R.drawable.doc10
            )
            if (!isWishlisted) {
                binding.wishlistIcon10.setImageResource(R.drawable.baseline_favorite_24)
                binding.wishlistIcon10.tag = "on"
                wishlistedDoctors.add(doctor)
                Toast.makeText(this, "Doctor added to Wishlist successfully", Toast.LENGTH_SHORT).show()
            } else {
                binding.wishlistIcon10.setImageResource(R.drawable.baseline_unfavorite_border_24)
                binding.wishlistIcon10.tag = "off"
                wishlistedDoctors.remove(doctor)
                Toast.makeText(this, "Doctor removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNotificationBell() {
        binding.notificationBellIcon.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    fun showHomeContent() {
        binding.scrollContent.visibility = android.view.View.VISIBLE
        binding.fragmentContainer.visibility = android.view.View.GONE
        binding.buttomNavigation.selectedItemId = -1
    }

    private fun replaceFragment(fragment: Fragment) {
        binding.scrollContent.visibility = android.view.View.GONE
        binding.fragmentContainer.visibility = android.view.View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(com.example.book_my_doctor.R.id.fragmentContainer, fragment)
            .commit()
    }
}