package com.capstonehore.ngelana.view.home.plan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.FragmentPlanBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel
import com.capstonehore.ngelana.view.home.plan.recommendation.RecommendationPlanActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PlanFragment : Fragment() {

    private var _binding: FragmentPlanBinding? = null

    private val binding get() = _binding!!

    private lateinit var planViewModel: PlanViewModel

    private val navController by lazy { findNavController() }

    private var selectedDate: Date? = null
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

//    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupAction()
        setupImage()
        setupAnimation()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            navController.navigate(R.id.action_navigation_plan_to_navigation_home)
        }

        binding.nextButton.setOnClickListener {
//            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
//                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                getUserLastLocation()
//            } else {
//                requestLocationPermission()
//            }
            moveToRecommendationPlan()
        }

        binding.edDate.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }
    }

    private fun setupImage() {
        val image = "https://storage.googleapis.com/ngelana-bucket/ngelana-assets/img_ngelana6.png"
        Glide.with(requireActivity())
            .load(image)
            .into(binding.imageView)
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val tvDescription = ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(300)
        val tvDate = ObjectAnimator.ofFloat(binding.tvDate, View.ALPHA, 1f).setDuration(300)
        val nextButton =
            ObjectAnimator.ofFloat(binding.nextButton, View.ALPHA, 1f).setDuration(500)
        val backButton = ObjectAnimator.ofFloat(binding.backButton, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(nextButton, backButton)
        }

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDescription,
                tvDate,
                together
            )
            start()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                selectedDate = selectedCalendar.time
                binding.edDate.setText(dateFormat.format(selectedCalendar.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            when {
//                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
//                    getUserLastLocation()
//                }
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
//                    getUserLastLocation()
//                }
//                else -> {
//                    showToast(getString(R.string.permission_request_denied))
//                }
//            }
//        }

//    private fun checkPermission(permission: String): Boolean {
//        return ContextCompat.checkSelfPermission(
//            requireContext(),
//            permission
//        ) == PackageManager.PERMISSION_GRANTED
//    }

//    private fun requestLocationPermission() {
//        requestPermissionLauncher.launch(
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        )
//    }

//    private fun getUserLastLocation() {
//        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
//            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//        ) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    moveToRecommendationPlan()
//                    updateLocationUI(location)
//                } else {
//                    showToast(getString(R.string.enable_location_first))
//                }
//            }
//        } else {
//            requestLocationPermission()
//        }
//    }

    private fun moveToRecommendationPlan() {
        val dateStr = selectedDate?.let { dateFormat.format(it) } ?: ""

        if (dateStr.isEmpty()) {
            showToast(getString(R.string.empty_date))
        } else {
            startActivity(
                Intent(requireActivity(), RecommendationPlanActivity::class.java)
                    .putExtra(RecommendationPlanActivity.EXTRA_DATE, dateStr)
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlaceViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[PlaceViewModel::class.java]
    }
}