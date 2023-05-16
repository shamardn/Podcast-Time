package com.shamardn.podcasttime.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.PodcastTimeTheme)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        installSplashScreen()
    }

    override fun onResume() {
        super.onResume()
        val navController = Navigation.findNavController(this, R.id.main_fragment_container_view)
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}