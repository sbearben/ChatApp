package uk.co.victoriajanedavis.chatapp.presentation.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import uk.co.victoriajanedavis.chatapp.presentation.notifications.message.MessageNotification

internal fun registerNotificationChannel(context: Context, id: String, name: String, description: String) {
    // Create the required NotificationChannel for API 26+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH).apply {
            this.description = description
        }
        // Register the channel with the system
        with(context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) {
            createNotificationChannel(channel)
        }
    }
}

fun registerAllNotificationChannels(context: Context) {
    MessageNotification.registerNotificationChannel(context)
}