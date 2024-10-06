package com.shamardn.podcasttime.ui.auth.fragments

import android.os.Bundle
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
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.models.Resource
import com.shamardn.podcasttime.databinding.FragmentRegisterBinding
import com.shamardn.podcasttime.ui.auth.getGoogleRequestIntent
import com.shamardn.podcasttime.ui.auth.viewmodel.RegisterViewModel
import com.shamardn.podcasttime.ui.auth.viewmodel.RegisterViewModelFactory
import com.shamardn.podcasttime.ui.common.custom_views.ProgressDialog
import com.shamardn.podcasttime.ui.showRetrySnakeBar
import com.shamardn.podcasttime.ui.showSnakeBarError
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.RegisterException
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    private val loginManager: LoginManager by lazy { LoginManager.getInstance() }

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(contextValue = requireContext())
    }

    val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignupResult(task)
            } else {
                view?.showRetrySnakeBar(getString(R.string.sign_in_failed)) {
                    registerWithGoogleRequest()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = registerViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        handleObservers()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            registerViewModel.registerState.collect { resource ->
                when(resource){
                    is Resource.Loading -> {
                        progressDialog.show()
                    }
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        showRegisterSuccessDialog()
                    }
                    is Resource.Error -> {
                        progressDialog.dismiss()
                        val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                        logAuthIssuesToCrashlytics(msg, "Register Error")
                    }
                }
            }
        }
    }

    private fun handleObservers() {
        binding.btnRegisterSignupGoogle.setOnClickListener{
            registerWithGoogleRequest()
        }

        binding.btnRegisterSignupFace.setOnClickListener {
            if (!isLoggedIn()) {
                loginWithFacebook()
            } else {
                signOut()
            }
        }
        binding.btnRegisterSignup.setOnClickListener {
            registerViewModel.registerWithEmailAndPassword()
        }

        binding.textRegisterLogin.setOnClickListener {
            findNavController().popBackStack()
        }
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
        registerViewModel.registerWithFacebook(token)
    }

    private fun handleSignupResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            val msg = e.message ?: getString(R.string.generic_err_msg)
            logAuthIssuesToCrashlytics(msg, "Google")
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_err_msg))
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        registerViewModel.registerWithGoogle(idToken)
    }

    private fun registerWithGoogleRequest() {
        val signupIntent = getGoogleRequestIntent(requireActivity())
        launcher.launch(signupIntent)
    }

    private fun logAuthIssuesToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<RegisterException>(
            msg,
            CrashlyticsUtils.REGISTER_KEY to msg,
            CrashlyticsUtils.AUTH_PROVIDER to provider,
        )
    }

    private fun showRegisterSuccessDialog() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Register Success")
            .setMessage("We have sent you an email verification link. Please verify your email to login.")
            .setPositiveButton(
                "OK"
            ) { dialog, which ->
                dialog?.dismiss()
                findNavController().popBackStack()
            }.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}