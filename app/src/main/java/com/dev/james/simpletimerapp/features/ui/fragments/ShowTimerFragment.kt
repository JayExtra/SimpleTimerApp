package com.dev.james.simpletimerapp.features.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dev.james.simpletimerapp.R
import com.dev.james.simpletimerapp.databinding.FragmentShowTimerBinding
import com.dev.james.simpletimerapp.viewmodel.TimerViewModel
import kotlinx.coroutines.flow.collectLatest

class ShowTimerFragment : Fragment(R.layout.fragment_show_timer) {

    private lateinit var  binding : FragmentShowTimerBinding
    private val timerViewModel : TimerViewModel by activityViewModels()

    val time = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShowTimerBinding.bind(view)

        listenToChannels()

        binding.apply {
            pauseStartFab.setOnClickListener {
                timerViewModel.triggerPause()
            }
        }
    }


    private fun listenToChannels() {
        lifecycleScope.launchWhenStarted {
            timerViewModel.stateFlow.collectLatest { timerState ->
                when(timerState){
                    is TimerViewModel.TimerState.Running -> {
                        Toast.makeText(requireContext(), "timer started...", Toast.LENGTH_SHORT).show()
                        stopBlinking()
                        binding.apply {
                            pauseStartFab.setImageResource(R.drawable.ic_baseline_pause)
                            pauseStartFab.setOnClickListener {
                                timerViewModel.triggerPause()
                            }
                        }
                    }
                    is TimerViewModel.TimerState.Paused -> {
                        Toast.makeText(requireContext(), "timer paused.", Toast.LENGTH_SHORT).show()
                        startBlinking()
                        binding.apply {
                            pauseStartFab.setImageResource(R.drawable.ic_baseline_play)
                            pauseStartFab.setOnClickListener {
                                timerViewModel.triggerStart(time)
                            }
                        }
                    }

                    is TimerViewModel.TimerState.Stopped -> {
                        stopBlinking()
                        Toast.makeText(requireContext(), "timer stopped.", Toast.LENGTH_SHORT).show()
                        binding.apply {
                            pauseStartFab.setImageResource(R.drawable.ic_baseline_play)
                        }

                    }
                }

            }
        }
    }

    private fun startBlinking(){
        val blinkingAnimation = AnimationUtils.loadAnimation(requireContext() , R.anim.blink)
        binding.apply {
            timerText.startAnimation(blinkingAnimation)
        }
    }

    private fun stopBlinking(){
        val blinkingAnimation = AnimationUtils.loadAnimation(requireContext() , R.anim.blink)
        binding.apply {
            timerText.clearAnimation()
        }
    }

}