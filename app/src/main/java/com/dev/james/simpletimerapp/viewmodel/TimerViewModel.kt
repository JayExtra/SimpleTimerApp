package com.dev.james.simpletimerapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    val time = ""
    private val _timeLiveData : MutableLiveData<String> = MutableLiveData()
    val timeLivedata get() = _timeLiveData



}