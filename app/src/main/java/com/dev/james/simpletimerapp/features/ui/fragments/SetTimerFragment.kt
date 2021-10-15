package com.dev.james.simpletimerapp.features.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dev.james.simpletimerapp.R
import com.dev.james.simpletimerapp.databinding.FragmentSetTimerBinding
import kotlin.math.min

class SetTimerFragment : Fragment(R.layout.fragment_set_timer) {

    private lateinit var binding: FragmentSetTimerBinding
    private val secondsString : MutableList<String> = mutableListOf()
    private val minutesString : MutableList<String> = mutableListOf()
    private val hoursString : MutableList<String> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetTimerBinding.bind(view)

        binding.apply {

            button0.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("0")
                }
            }
            button1.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("1")
                }            }
            button2.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("2")
                }            }
            button3.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("3")
                }            }
            button4.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("4")
                }            }
            button5.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("5")
                }            }
            button6.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("6")
                }            }
            button7.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("7")
                }            }
            button8.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("8")
                }            }
            button9.setOnClickListener {
                if(hoursString.size < 2){
                    updateTimeText("9")
                }            }


            backSpaceimage.setOnClickListener {
                deleteAndUpdateUi()
            }

        }
    }

    private fun deleteAndUpdateUi() {


        binding.timerTextView.text = requireContext().getString(
            R.string.updated_timer , hoursString.joinToString(separator = "")
            , minutesString.joinToString(separator = "") ,secondsString.joinToString(separator = "")
        )


    }

    private fun updateTimeText(s: String) {
        secondsString.add(s)
            if (secondsString.size > 2){
                val extraDig = secondsString[0]
                minutesString.add(extraDig)
                secondsString.removeAt(0)
            }

            if(minutesString.size > 2 ){
                val extraDig2 = minutesString[0]
                hoursString.add(extraDig2)
                minutesString.removeAt(0)
                Toast.makeText(requireContext(), "minutes size has passed 2", Toast.LENGTH_SHORT).show()
            }

            if(hoursString.size > 2 ){
                hoursString.removeAt(1)
                //disableButtons()

            }

            binding.timerTextView.text = requireContext().getString(
                R.string.updated_timer , hoursString.joinToString(separator = "")
                , minutesString.joinToString(separator = "") ,secondsString.joinToString(separator = "")
            )
    }

    private fun disableButtons(){
        binding.apply {
            button0.isEnabled = false
            button1.isEnabled = false
            button2.isEnabled = false
            button3.isEnabled = false
            button4.isEnabled = false
            button5.isEnabled = false
            button6.isEnabled = false
            button7.isEnabled = false
            button8.isEnabled = false
            button9.isEnabled = false
        }
    }

    private fun enableButtons(){
        binding.apply {
            button0.isEnabled = true
            button1.isEnabled = true
            button2.isEnabled = true
            button3.isEnabled = true
            button4.isEnabled = true
            button5.isEnabled = true
            button6.isEnabled = true
            button7.isEnabled = true
            button8.isEnabled = true
            button9.isEnabled = true
        }
    }
}