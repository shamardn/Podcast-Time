package com.shamardn.podcasttime.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.shamardn.podcasttime.BuildConfig
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.datasource.datastore.UserPreferenceDataSource
import com.shamardn.podcasttime.data.models.Resource
import com.shamardn.podcasttime.data.repo.auth.FirebaseAuthRepositoryImpl
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepositoryImpl
import com.shamardn.podcasttime.databinding.FragmentLoginBinding
import com.shamardn.podcasttime.ui.auth.viewmodel.LoginViewModel
import com.shamardn.podcasttime.ui.auth.viewmodel.LoginViewModelFactory
import com.shamardn.podcasttime.ui.common.custom_views.ProgressDialog
import com.shamardn.podcasttime.ui.showRetrySnakeBar
import com.shamardn.podcasttime.ui.showSnakeBarError
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.LoginException
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    private val loginManager: LoginManager by lazy { LoginManager.getInstance() }

    val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            userPrefs = UserPreferenceRepositoryImpl(UserPreferenceDataSource(requireActivity())),
            authRepository = FirebaseAuthRepositoryImpl()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = loginViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        handleObservers()
    }

    private fun handleObservers() {
        loginViewModel.isGoogleBtnClicked.observe(viewLifecycleOwner) {
            if (it) {
                loginWithGoogleRequest()
            }
        }

        loginViewModel.isFacebookBtnClicked.observe(viewLifecycleOwner) {
            if (it) {
                if (isLoggedIn()) {
                    signOut()
                } else {
                    loginWithFacebook()
                }
            }
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            loginViewModel.loginState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        progressDialog.dismiss()
                        findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                        logAuthIssuesToCrashlytics(msg, "Login Error")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun signOut() {
        loginManager.logOut()
    }

    private fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    private fun loginWithFacebook() {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val token = result.accessToken.token
                firebaseAuthWithFacebook(token)
            }

            override fun onCancel() {
                // Handle login cancel
            }

            override fun onError(error: FacebookException) {
                val msg = error.message ?: getString(R.string.generic_err_msg)
                view?.showSnakeBarError(msg)
                logAuthIssuesToCrashlytics(msg, "Facebook")
            }
        })

        loginManager.logInWithReadPermissions(
            this, callbackManager, listOf("email", "public_profile")
        )
    }

    private fun firebaseAuthWithFacebook(token: String) {
        loginViewModel.loginWithFacebook(token)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            } else {
                view?.showRetrySnakeBar(getString(R.string.sign_in_failed)) {
                    loginWithGoogleRequest()
                }
            }
        }

    private fun loginWithGoogleRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.clientServerId)
            .requestEmail()
            .requestProfile()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        googleSignInClient.signOut()

        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            val msg = e.message ?: getString(R.string.generic_err_msg)
            logAuthIssuesToCrashlytics(msg, "Google")
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_err_msg))
        }
    }

    private fun logAuthIssuesToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUtils.LOGIN_KEY to  msg,
            CrashlyticsUtils.AUTH_PROVIDER to provider,
            )
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
       loginViewModel.loginWithGoogle(idToken)
    }


    companion object {
        private const val TAG = "LoginFragment"
    }
}