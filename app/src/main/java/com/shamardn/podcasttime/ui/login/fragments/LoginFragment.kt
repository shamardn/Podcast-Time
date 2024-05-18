package com.shamardn.podcasttime.ui.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shamardn.podcasttime.data.datasource.datastore.UserPreferenceDataSource
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepositoryImpl
import com.shamardn.podcasttime.databinding.FragmentLoginBinding
import com.shamardn.podcasttime.ui.login.viewmodel.LoginViewModel
import com.shamardn.podcasttime.ui.login.viewmodel.LoginViewModelFactory

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(userPreferenceRepository = UserPreferenceRepositoryImpl(
            UserPreferenceDataSource(requireActivity())
        ))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        private const val TAG = "LoginFragment"
    }
}