package ru.ytken.libraryapp

class ActionDebounceFilter {
    private var lastClickedTime = 0L
    private val currentTimeMs: Long
        get() = System.currentTimeMillis()

    fun filterAction(): Boolean {
        return if (currentTimeMs - lastClickedTime >= 400L) {
            lastClickedTime = currentTimeMs
            true
        } else {
            false
        }
    }
}