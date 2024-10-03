package com.shamardn.podcasttime.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.models.Resource
import com.shamardn.podcasttime.databinding.FragmentRegisterBinding
import com.shamardn.podcasttime.ui.auth.viewmodel.RegisterViewModel
import com.shamardn.podcasttime.ui.auth.viewmodel.RegisterViewModelFactory
import com.shamardn.podcasttime.ui.common.custom_views.ProgressDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(contextValue = requireContext())
    }

    val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

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

    private fun handleObservers() {
        registerViewModel.isGoogleBtnClicked.observe(viewLifecycleOwner) {
//            if (it) {
//                signupWithGoogleRequest()
//            }
        }

        registerViewModel.isFacebookBtnClicked.observe(viewLifecycleOwner) { isClicked ->
//            if (isClicked) {
//                if (isLoggedIn()) {
//                    signOut()
//                } else {
//                    signupWithFacebook()
//                }
//            }
        }
        binding.textLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            registerViewModel.registerState.collect { resources ->
                when(resources){
                    is Resource.Loading -> {
                        progressDialog.show()
                    }
                    is Resource.Success -> {
                        progressDialog.dismiss()
                        showLoginSuccessDialog()
                    }
                    is Resource.Error -> {
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun showLoginSuccessDialog() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Register Success")
            .setMessage("We have sent you an email verification link. Please verify your email to login.")
            .setPositiveButton(
                "OK"
            ) { dialog, which ->
                dialog?.dismiss()
                findNavController().popBackStack()
            }.create().show()
    }
}