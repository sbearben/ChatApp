package uk.co.victoriajanedavis.chatapp.common

import org.mockito.Mockito

// Kotlin's null-safety creates an issue here which is why we have this elvis operator
// solution: https://stackoverflow.com/a/48805160/7648952
object MockitoUtil {

    @JvmStatic
    fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}