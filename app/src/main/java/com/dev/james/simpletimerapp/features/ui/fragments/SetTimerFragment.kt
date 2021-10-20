package com.dev.james.simpletimerapp.features.ui.fragments

import android.animation.Animator
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.dev.james.simpletimerapp.R
import com.dev.james.simpletimerapp.databinding.FragmentSetTimerBinding
import com.dev.james.simpletimerapp.viewmodel.TimerViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest

class SetTimerFragment : Fragment(R.layout.fragment_set_timer) {

    private lateinit var binding: FragmentSetTimerBinding
    private var time = ""

    private val timerViewModel : TimerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetTimerBinding.bind(view)


        //on creation of the application we update the time text to its initial
        //value without any time added in this case 00:00:00
        updateTimeText()

        //we listen to the event channels for any one time events e.g to show toast that the
        //timer has been started
        listenToChannels()

        //we observe the various timer state livedata. We do this so that we can preserve
        //the timer states even on configuration changes. In our case we use the bottom nav
        //navigating between fragments make the fragments on the back stacks lose their state when destroyed and
        //recreated.
        observeLiveData()

        binding.apply {

            //these are the individual buttons for adding the time digits
            listOf(button0 , button1 , button2 , button3 , button4 , button5 ,button6,
            button7 , button8 , button9).forEach { button ->
                button.setOnClickListener {
                    button0.isClickable = true
                    addDigit(button.text as String)
                }
            }


            //on creation we prevent the button zero from being clickable to stop
            //the addition of a zero as the first timer digit.
            button0.isClickable = false

            backSpaceimage.setOnClickListener {
                //this will call the deleteDigit() function which will delete
                //text one by one
                deleteDigit()
            }

            startTimerFab.setOnClickListener {
                val output = "000000$time"
                //when we trigger start timer we will pass the time string
                //to the ViewModel where we will calculate the hours minutes and seconds
                Log.i("SetTimer", "onViewCreated: time now $output ")
                timerViewModel.triggerStart(output)


                //on starting the timer trigger the layout animations
                addTimerLayout.isInvisible = true
                showTimerLayout.slideUp()

            }
            deleteTimeButton.setOnClickListener {
                //trigger the timer reset and reset everything back to zero
                //also including stoping the timer
                timerViewModel.triggerStop()
                //also do the layout animations
                addTimerLayout.slideUp()
                showTimerLayout.isInvisible = true
            }

            editLabelImage.setOnClickListener {
                //show dialog
                showDialog()
            }
        }
    }

    private fun observeLiveData() {
        timerViewModel.isTimerRunning.observe(viewLifecycleOwner , { hasBeenStarted ->

            //if timer has been started we will show the required layout. Then change the functionality
            //of the pause and start button to pause the timer on click. If the timer is not running , we will show
            //the required layouts.
                if (hasBeenStarted){
                    binding.apply {
                        showTimerLayout.isVisible = true
                        addTimerLayout.isInvisible = true
                        pauseStartFab.setOnClickListener {
                            timerViewModel.triggerPause()
                        }
                    }
                }else{
                    binding.apply {
                        addTimerLayout.isVisible = true
                        showTimerLayout.isInvisible = true
                    }
                }

        })

        //if the timer is paused , the timer text will show a blinking animation ,  then change the functionality
        //of the pause and start button to play/resume the timer. Otherwise if not paused the timer will continue running
        //and the blinking animation will stop.
        timerViewModel.isTimerPaused.observe(viewLifecycleOwner , { isPaused ->
                if (isPaused){
                    binding.apply {
                        startBlinking()
                        pauseStartFab.setImageResource(R.drawable.ic_baseline_play)
                        pauseStartFab.setOnClickListener {
                            timerViewModel.triggerStart(time)
                        }
                    }
                }else{
                    stopBlinking()
                    binding.pauseStartFab.setImageResource(R.drawable.ic_baseline_pause)
                }

        })

        //time calculated and received which will be passed to the timer service

        timerViewModel.timeMillis.observe(viewLifecycleOwner , { timeMillis ->

            Log.i("SetTimer", "received time in millis : $timeMillis")

        })


    }


    private fun updateTimeText() {
        val output = "000000$time"

            val h = output.takeLast(6).take(2)
            val m = output.takeLast(4).take(2)
            val s = output.takeLast(2)
            //Log.d("SetTimer", "updated text: $h $m $s ")

        val firstValue = output.takeLast(6).take(6).takeLast(1)
        Log.i("FirstValue", "updateTimeText: first value is : $firstValue ")
            binding.timerTextView.text = "${h}h ${m}m ${s}s"
            binding.timerText.text = "${h}h ${m}m ${s}s"



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


        binding.button0.isClickable = false

        updateTimeText()
    }



    private fun listenToChannels() {

        //here we listen to all the one time events during running , pausing and stopping the timer. They
        //are collected as flows.
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
                        resetTime()
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

    //layout animations
    private fun View.slideUp(duration : Int = 300){
        val animate = TranslateAnimation(0f , 0f ,this.height.toFloat() , 0f)
        animate.duration = duration.toLong()
        this.startAnimation(animate)
        visibility = View.VISIBLE

    }


    //show dialog
    private fun showDialog(){
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.fragment_label_dialog)

        //get label from
        val labelTextField = dialog.findViewById<EditText>(R.id.label_text_input)
        val labelEditTextLayout = dialog.findViewById<TextInputLayout>(R.id.textFieldLayout)
        val saveBtn = dialog.findViewById<Button>(R.id.buttonSave)
        val cancelBtn = dialog.findViewById<Button>(R.id.buttonCancel)
        val label = labelTextField.text

        saveBtn.setOnClickListener {

            if (label.isEmpty()){
                labelEditTextLayout.error = "label cannot be empty"
            }
            binding.timerLabel.text = label
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()

    }

}