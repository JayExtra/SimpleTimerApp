package com.dev.james.simpletimerapp.features.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dev.james.simpletimerapp.R
import com.dev.james.simpletimerapp.databinding.FragmentSetTimerBinding

class SetTimerFragment : Fragment(R.layout.fragment_set_timer) {

    private lateinit var binding: FragmentSetTimerBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetTimerBinding.bind(view)
    }
}