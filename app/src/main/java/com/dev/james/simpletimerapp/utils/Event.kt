package com.dev.james.simpletimerapp.utils

open class Event<out T>(private val content : T) {
    var hasBeenHandled = false
    private set //allow external read but not write

    /**
     * Returns the content and prevents its us again*/
    fun getContentIfNotHandled() : T? {
        return if(hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled*/
    fun peekContent() : T = content
}