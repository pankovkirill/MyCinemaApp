package com.example.mycinemaapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mycinemaapp.R
import com.example.mycinemaapp.databinding.MainActivityBinding
import com.example.mycinemaapp.view.contacts.ContactFragment
import com.example.mycinemaapp.view.maps.MapsFragment
import com.example.mycinemaapp.view.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    @RequiresApi(Build.VERSION_CODES.M)
    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when {
                it -> getLocation()
                !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    Toast.makeText(
                        this,
                        "Разрешите доступ к геолокации в настройках",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> Toast.makeText(this, "T_T", Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        supportFragmentManager.apply {
            beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, MapsFragment())
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_favorite
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_setting -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, SettingsFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
            }

            R.id.navigation_contact -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, ContactFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
            }

            R.id.navigation_map -> {
                permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}