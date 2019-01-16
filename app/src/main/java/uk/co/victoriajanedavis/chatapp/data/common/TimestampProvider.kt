package uk.co.victoriajanedavis.chatapp.data.common

class TimestampProvider {
    companion object {
        fun currentTimeMillis() : Long = System.currentTimeMillis()
    }
}