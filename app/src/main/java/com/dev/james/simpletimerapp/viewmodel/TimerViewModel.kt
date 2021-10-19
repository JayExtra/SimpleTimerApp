package com.dev.james.simpletimerapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {


    //livedata for observing timer state
    private var _isTimerRunning : MutableLiveData<Boolean> = MutableLiveData()
    val isTimerRunning get() = _isTimerRunning

    //livedata for observing paused timer state
    private var _isTimerPaused : MutableLiveData<Boolean> = MutableLiveData()
    val isTimerPaused get() = _isTimerPaused




    //create our state channels for various states of the timer
    //either running , paused or stopped.
    private val stateChannel = Channel<TimerState>()
    val stateFlow = stateChannel.receiveAsFlow()


    //start timer
    fun triggerStart() = viewModelScope.launch {
        stateChannel.send(TimerState.Running(true))
        _isTimerRunning.postValue(true)
        _isTimerPaused.postValue(false)
    }

    //pause timer
    fun triggerPause() = viewModelScope.launch {
        stateChannel.send(TimerState.Paused(true))
        _isTimerPaused.postValue(true)
    }

    //stop timer
    fun triggerStop() = viewModelScope.launch {
        stateChannel.send(TimerState.Stopped(true))
        _isTimerRunning.postValue(false)
        _isTimerPaused.postValue(false)
    }

    sealed class TimerState{
        //various states of the timer
        data class Running(val state : Boolean) : TimerState()
        data class Paused(val state : Boolean) : TimerState()
        data class Stopped(val state : Boolean) : TimerState()

    }

}