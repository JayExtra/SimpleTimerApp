package com.dev.james.simpletimerapp.features.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dev.james.simpletimerapp.R
import com.dev.james.simpletimerapp.databinding.FragmentSetTimerBinding
import com.dev.james.simpletimerapp.viewmodel.TimerViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.min

class SetTimerFragment : Fragment(R.layout.fragment_set_timer) {

    private lateinit var binding: FragmentSetTimerBinding
    private var time = ""

    private val timerViewModel : TimerViewModel by activityViewModels()

    private var hasTimerBeenSet : Boolean? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetTimerBinding.bind(view)


        updateTimeText()
        listenToChannels()
        observeLiveData()

        binding.apply {
           // addTimerLayout.isVisible = true
            listOf(button0 , button1 , button2 , button3 , button4 , button5 ,button6,
            button7 , button8 , button9).forEach { button ->
                button.setOnClickListener {
                    addDigit(button.text as String)
                }
            }

            backSpaceimage.setOnClickListener {
                deleteDigit()
            }

            startTimerFab.setOnClickListener {
                //findNavController().navigate(R.id.action_setTimerFragment_to_showTimerFragment)
                timerViewModel.triggerStart()
                hasTimerBeenSet = true
            }
            deleteTimeButton.setOnClickListener {
                timerViewModel.triggerStop()
                hasTimerBeenSet = false
            }
        }
    }

    private fun observeLiveData() {
        timerViewModel.hasTimerStarted.observe(viewLifecycleOwner , { hasBeenStarted ->

            if (hasBeenStarted){
                binding.apply {
                    addTimerLayout.isInvisible = true
                    showTimerLayout.isVisible = true
                }
            }else{
                binding.apply {
                    addTimerLayout.isVisible = true
                    showTimerLayout.isInvisible = true
                }
            }

        })

        timerViewModel.isTimerPaused.observe(viewLifecycleOwner , { isPaused ->

            if (isPaused){
                binding.apply {
                   startBlinking()
                        pauseStartFab.setImageResource(R.drawable.ic_baseline_play)
                        pauseStartFab.setOnClickListener {
                            timerViewModel.triggerStart()
                        }
                }
            }else{
                stopBlinking()
            }

        })
    }


    private fun updateTimeText() {
        val output = "000000$time"
        val h = output.takeLast(6).take(2)
        val m = output.takeLast(4).take(2)
        val s = output.takeLast(2)

        //Log.d("SetTimer", "updated text: $h $m $s ")
        binding.timerTextView.text = "${h}h ${m}m ${s}s"

    }

    private fun addDigit(s : String){
        if (time.length < 6)
            time += s

        binding.startTimerFab.isVisible = time.isNotEmpty()
        binding.timerTextView.setTextColor(resources.getColor(R.color.haven_purple))

        Log.d("SetTimer", "addDigit: $s ")

        updateTimeText()
    }


    private fun deleteDigit(){
        time = time.dropLast(1)
        binding.startTimerFab.isInvisible = time.isEmpty()

        if(time.isEmpty())
            binding.timerTextView.setTextColor(Color.GRAY)

        updateTimeText()
    }

    private fun listenToChannels() {
        lifecycleScope.launchWhenStarted {
            timerViewModel.stateFlow.collectLatest { timerState ->
                when(timerState){
                    is TimerViewModel.TimerState.Running -> {
                        Toast.makeText(requireContext(), "timer started...", Toast.LENGTH_SHORT).show()
                        stopBlinking()
                        binding.apply {

                            showTimerLayout.isVisible = true
                            addTimerLayout.isInvisible = true
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
                                timerViewModel.triggerStart()
                            }
                        }
                    }

                    is TimerViewModel.TimerState.Stopped -> {
                        stopBlinking()
                        resetTime()
                        Toast.makeText(requireContext(), "timer stopped.", Toast.LENGTH_SHORT).show()
                        binding.apply {
                            pauseStartFab.setImageResource(R.drawable.ic_baseline_play)
                            addTimerLayout.isVisible = true
                            showTimerLayout.isInvisible = true
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
        binding.apply {
            timerText.clearAnimation()
        }
    }

    private fun resetTime(){
        time = time.dropLast(6)
        binding.startTimerFab.isInvisible = time.isEmpty()
        if(time.isEmpty())
            binding.timerTextView.setTextColor(Color.GRAY)

        updateTimeText()
    }

}