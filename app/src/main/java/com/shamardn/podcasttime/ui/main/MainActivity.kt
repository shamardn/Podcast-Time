package com.shamardn.podcasttime.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.shamardn.podcasttime.PodcastTimeApplication
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ActivityMainBinding
import com.shamardn.podcasttime.util.Constants.ACTION_SHOW_EPISODE_DETAILS_FRAGMENT
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

        navigateToEpisodeDetailsFragmentIfNeeded(intent)

        installSplashScreen()

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
        navHostFragment.navController.setGraph(graph, intent.extras)
    }

    private fun navigateToEpisodeDetailsFragmentIfNeeded(intent: Intent?){
        if(intent?.action == ACTION_SHOW_EPISODE_DETAILS_FRAGMENT) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container_view) as NavHostFragment
            navHostFragment.findNavController().navigate(R.id.action_global_episode_details_fragment)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToEpisodeDetailsFragmentIfNeeded(intent)
    }
}