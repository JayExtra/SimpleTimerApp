package com.dev.james.simpletimerapp.features.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.dev.james.simpletimerapp.R
import com.dev.james.simpletimerapp.databinding.FragmentSetTimerBinding
import kotlin.math.min

class SetTimerFragment : Fragment(R.layout.fragment_set_timer) {

    private lateinit var binding: FragmentSetTimerBinding
    private var time = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetTimerBinding.bind(view)


        updateTimeText()

        binding.apply {

            listOf(button0 , button1 , button2 , button3 , button4 , button5 ,button6,
            button7 , button8 , button9).forEach { button ->
                button.setOnClickListener {
                    addDigit(button.text as String)
                }
            }

            backSpaceimage.setOnClickListener {
                deleteDigit()
            }

        }
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

}