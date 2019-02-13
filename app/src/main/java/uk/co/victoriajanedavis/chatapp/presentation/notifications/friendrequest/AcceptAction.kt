package uk.co.victoriajanedavis.chatapp.presentation.notifications.friendrequest

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class AcceptAction @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun createAcceptActionPendingIntent(
        username: String,
        notificationTag: String,
        channelId: String
    ): PendingIntent {
        return PendingIntent.getService(
            context,
            0,  // notificationId
            AcceptActionService.newIntent(
                context,
                username,
                notificationTag,
                channelId
            ),
             PendingIntent.FLAG_ONE_SHOT)
    }
}