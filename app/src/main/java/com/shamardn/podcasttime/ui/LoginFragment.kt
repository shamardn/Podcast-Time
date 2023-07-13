package com.shamardn.podcasttime.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.ui.main.MainActivity

class LoginFragment : Fragment() {

    private var bottomNavigationViewVisibility = View.GONE

    private fun setBottomNavigationVisibility() {
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setBottomNavigationVisibility()
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

}