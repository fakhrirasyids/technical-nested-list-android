package com.fakhrirasyids.technicalnestedlist.utils.helpers

open class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            content
        }
    }

    fun handleContent() {
        hasBeenHandled = true
    }
}