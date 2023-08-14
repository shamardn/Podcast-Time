package com.shamardn.podcasttime.ui.main

import android.media.AudioManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.shamardn.podcasttime.PodcastTimeApplication
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ActivityMainBinding
import com.shamardn.podcasttime.ui.bottomplayer.BottomPlayerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.PodcastTimeTheme)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(binding.root)

        setStartDestination()

        installSplashScreen()

        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onResume() {
        super.onResume()
        this.findNavController(R.id.main_fragment_container_view).run {
            binding.bottomNavigationView.setupWithNavController(this)
        }
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigationView.visibility = visibility
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setStartDestination() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container_view) as NavHostFragment
        val graph = navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph)
        if (PodcastTimeApplication.isFirstTimeLaunch) {
            graph.setStartDestination(R.id.loginFragment)
        }else {
            graph.setStartDestination(R.id.homeFragment)
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.bottom_player_container, BottomPlayerFragment(),"BottomPlayerFragment").commit()

        navHostFragment.navController.setGraph(graph, intent.extras)
    }


}