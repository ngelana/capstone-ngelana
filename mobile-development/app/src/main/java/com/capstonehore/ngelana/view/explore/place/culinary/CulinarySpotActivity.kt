package com.capstonehore.ngelana.view.explore.place.culinary

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.PlaceAdapter
import com.capstonehore.ngelana.data.Result
import com.capstonehore.ngelana.data.preferences.UserPreferences
import com.capstonehore.ngelana.data.remote.response.PlaceItem
import com.capstonehore.ngelana.databinding.ActivityCulinarySpotBinding
import com.capstonehore.ngelana.view.ViewModelFactory
import com.capstonehore.ngelana.view.detail.DetailPlaceFragment
import com.capstonehore.ngelana.view.explore.place.PlaceViewModel

class CulinarySpotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCulinarySpotBinding

    private lateinit var placeAdapter: PlaceAdapter

    private lateinit var placeViewModel: PlaceViewModel

    private val Context.sessionDataStore by preferencesDataStore(USER_SESSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCulinarySpotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        placeViewModel = obtainViewModel(this@CulinarySpotActivity)

        setupToolbar()
        setupAdapter()
        setupView("culinary_spot")
        setupSearchView()
    }

    private fun setupToolbar() {
        with(binding) {
            setSupportActionBar(topAppBar)
            topAppBar.setNavigationIcon(R.drawable.ic_arrow_back)
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupAdapter() {
        placeAdapter = PlaceAdapter(placeViewModel)

        binding.rvPlaces.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CulinarySpotActivity)
            adapter = placeAdapter
        }

        placeAdapter.setOnItemClickCallback(object : PlaceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaceItem?) {
                data?.let {
                    val dialogFragment = DetailPlaceFragment.newInstance(it)
                    dialogFragment.show(supportFragmentManager, "DetailPlaceFragment")
                }
            }
        })
    }

    private fun setupView(@Suppress("SameParameterValue") type: String) {
        placeViewModel.getPrimaryTypePlace(type)
            .observe(this@CulinarySpotActivity) {
                if (it != null) {
                    when (it) {
                        is Result.Success -> {
                            showLoading(false)

                            val response = it.data
                            placeAdapter.submitList(response)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showToast(it.error)
                        }
                        is Result.Loading -> showLoading(true)
                    }
                }
            }
    }

    private fun searchPlace(query: String) {
        placeViewModel.searchPlaceByQuery(query).observe(this@CulinarySpotActivity) {
            if (it != null) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)

                        val response = it.data
                        placeAdapter.submitList(response)
                        Log.d(TAG, "Successfully Show Places: $response")
                    }
                    is Result.Error -> {
                        showLoading(false)

                        showToast(it.error)
                        Log.d(TAG, "Failed to Show Places: ${it.error}")
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
        }
    }

    private fun setupSearchView() {
        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val query = s.toString().trim()
                    if (query.isNotEmpty()) {
                        searchPlace(query)
                    } else {
                        setupView("culinary_spot")
                    }
                }
            })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): PlaceViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            UserPreferences.getInstance(sessionDataStore)
        )
        return ViewModelProvider(activity, factory)[PlaceViewModel::class.java]
    }

    companion object {
        private const val TAG = "TouristAttractionsActivity"
        const val USER_SESSION = "user_session"
    }

}