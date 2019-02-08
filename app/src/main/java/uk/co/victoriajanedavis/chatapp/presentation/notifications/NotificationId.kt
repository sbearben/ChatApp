package uk.co.victoriajanedavis.chatapp.presentation.notifications

import android.os.SystemClock

const val ID = 0

private fun generateId(): Int {
    return SystemClock.uptimeMillis().toInt()
}