package com.shamardn.podcasttime.ui.main

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.PodcastTimeTheme)

        binding = ActivityMainBinding.inflate(layoutInflater)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(binding.root)

        installSplashScreen()
    }

    override fun onResume() {
        super.onResume()
        this.findNavController(R.id.main_fragment_container_view).run {
            binding.bottomNavigationView.setupWithNavController(this)
        }
    }
}