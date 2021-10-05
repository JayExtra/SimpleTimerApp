package com.dev.james.simpletimerapp.features.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dev.james.simpletimerapp.R
import com.dev.james.simpletimerapp.databinding.FragmentShowTimerBinding

class ShowTimerFragment : Fragment(R.layout.fragment_show_timer) {

    private lateinit var  binding : FragmentShowTimerBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShowTimerBinding.bind(view)
    }
}