package com.shamardn.podcasttime.ui.main

import android.Manifest
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.shamardn.podcasttime.PodcastTimeApplication
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.datasource.datastore.UserPreferenceDataSource
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepositoryImpl
import com.shamardn.podcasttime.databinding.ActivityMainBinding
import com.shamardn.podcasttime.ui.bottomplayer.BottomPlayerFragment
import com.shamardn.podcasttime.ui.common.viewmodel.UserViewModel
import com.shamardn.podcasttime.ui.common.viewmodel.UserViewModelFactory
import com.shamardn.podcasttime.ui.login.AuthActivity
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.NotificationException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserPreferenceRepositoryImpl(UserPreferenceDataSource(this)))
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(
                this,
                getString(R.string.notifications_permission_granted),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            Toast.makeText(
                this,
                getString(R.string.fcm_can_t_post_notifications_without_post_notifications_permission),
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Base_Theme_PodcastTime)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        initSplashScreen()

        val isLoggedIn = runBlocking { userViewModel.isUserLoggedIn().first() }
        if (!isLoggedIn) {
            goToAuthActivity()
            return
        }

        setStartDestination()

        setContentView(binding.root)

        volumeControlStream = AudioManager.STREAM_MUSIC

        askNotificationPermission()
    }

    private fun goToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val options = ActivityOptions.makeCustomAnimation(
            this, android.R.anim.fade_in, android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
        finish()
    }

    private fun initSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
            // Add a callback that's called when the splash screen is animating to the
            // app content.
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 1000L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        } else {
            setTheme(R.style.Theme_MainActivity)
        }
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

    fun setBottomPlayerVisibility(visibility: Int) {
        binding.bottomPlayerContainer.visibility = visibility
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setStartDestination() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container_view) as NavHostFragment
        val graph = navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph)
        if (PodcastTimeApplication.isFirstTimeLaunch) {
            graph.setStartDestination(R.id.loginFragment)
        } else {
            graph.setStartDestination(R.id.homeFragment)
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(
            R.id.bottom_player_container,
            BottomPlayerFragment(),
            "BottomPlayerFragment"
        ).commit()

        navHostFragment.navController.setGraph(graph, intent.extras)
    }

    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
                CrashlyticsUtils.sendCustomLogToCrashlytics<NotificationException>(
                    "Notifications PERMISSION_GRANTED",
                    Pair(CrashlyticsUtils.NOTIFICATION_KEY, "granted")
                )
            } else {
                // Directly ask for the permission
                CrashlyticsUtils.sendCustomLogToCrashlytics<NotificationException>(
                    "Notifications PERMISSION_NOT_GRANTED",
                    Pair(CrashlyticsUtils.NOTIFICATION_KEY, "not granted")
                )
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        CrashlyticsUtils.sendCustomLogToCrashlytics<NotificationException>(
            "Notifications",
            Pair(CrashlyticsUtils.NOTIFICATION_KEY, "build version is less than 33")
        )

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}