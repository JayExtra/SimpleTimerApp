package com.dev.james.simpletimerapp.features.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dev.james.simpletimerapp.R
import com.dev.james.simpletimerapp.databinding.FragmentSavedTimersBinding

class SavedTimersList : Fragment(R.layout.fragment_saved_timers) {

    private lateinit var binding : FragmentSavedTimersBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedTimersBinding.bind(view)
    }
}