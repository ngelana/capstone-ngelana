package com.capstonehore.ngelana.view.main

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.databinding.ActivityMainBinding

/**
 * MainActivity is the entry point of the application. It sets up the navigation for the app.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.navView
        bottomNavigationView.setupWithNavController(navController)

        // Set up navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (navController.currentDestination?.id != R.id.navigation_home) {
                        navController.navigate(R.id.navigation_home)
                    }
                    true
                }
                R.id.navigation_explore -> {
                    if (navController.currentDestination?.id != R.id.navigation_explore) {
                        navController.navigate(R.id.navigation_explore)
                    }
                    true
                }
                R.id.navigation_trip -> {
                    if (navController.currentDestination?.id != R.id.navigation_trip) {
                        navController.navigate(R.id.navigation_trip)
                    }
                    true
                }
                R.id.navigation_profile -> {
                    if (navController.currentDestination?.id != R.id.navigation_profile) {
                        navController.navigate(R.id.navigation_profile)
                    }
                    true
                }
                else -> false
            }
        }
    }

    /**
     * This method is called whenever the user chooses to navigate Up within your application's activity hierarchy from the action bar.
     *
     * @return boolean Return true if Up navigation completed successfully and this Activity was finished, false otherwise.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}