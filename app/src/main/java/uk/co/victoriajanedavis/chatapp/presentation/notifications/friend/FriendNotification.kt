package uk.co.victoriajanedavis.chatapp.presentation.notifications.friend

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.data.model.websocket.AcceptedFriendRequestWsModel
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ApplicationContext
import uk.co.victoriajanedavis.chatapp.presentation.notifications.ID
import uk.co.victoriajanedavis.chatapp.presentation.notifications.Notification
import uk.co.victoriajanedavis.chatapp.presentation.notifications.registerNotificationChannel
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainActivity
import java.util.UUID
import javax.inject.Inject

class FriendNotification @Inject constructor(
    @ApplicationContext private val context: Context
): Notification<AcceptedFriendRequestWsModel> {

    override fun issueNotification(model: AcceptedFriendRequestWsModel) {
        with(NotificationManagerCompat.from(context)) {
            val notificationTag = generateNotificationTag(model.acceptorUuid)
            //notificationId is a unique int for each notification that you must define
            notify(
                notificationTag, ID,  // I think each message notificationId should be unique per chat (not message) - so messages from same user will use same notification
                createNotificationBuilder(
                    smallIconResId = R.drawable.ic_chat_black_72dp,
                    title = "New friend",
                    content = "'${model.acceptorUsername}' accepted your friend request",
                    relevantUuid = model.acceptorUuid
                ).build()
            )
        }
    }

    private fun createNotificationBuilder(
        smallIconResId: Int,
        title: String,
        content: String,
        relevantUuid: UUID
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(createPendingIntent(relevantUuid))
            .setAutoCancel(true)
    }

    private fun createPendingIntent(userUuid: UUID): PendingIntent {
        return PendingIntent.getActivity(
            context,
            0,
            MainActivity.newIntent(context),
            PendingIntent.FLAG_CANCEL_CURRENT,
            createBundle(userUuid)
        )
    }

    private fun createBundle(userUuid: UUID): Bundle {
        return Bundle().apply {
            putString(EXTRA_NOTIFICATION_TYPE, "new_friend")
            putString(EXTRA_USER_UUID, userUuid.toString())
        }
    }

    private fun generateNotificationTag(chatUuid: UUID): String {
        return MESSAGE_TAG_PREFIX + chatUuid.toString()
    }

    companion object {
        private const val MESSAGE_TAG_PREFIX = "chatapp_new_friend"

        private const val CHANNEL_ID = "new_friend_channel_id"
        private const val CHANNEL_NAME = "New Friends"
        private const val CHANNEL_DESCRIPTION = "Accepted friend requests"

        const val EXTRA_NOTIFICATION_TYPE = "uk.co.victoriajanedavis.chatapp.MainActivity.notification_type"
        const val EXTRA_USER_UUID = "uk.co.victoriajanedavis.chatapp.MainActivity.user_uuid"

        fun registerNotificationChannel(context: Context) {
            return registerNotificationChannel(context, CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESCRIPTION)
        }
    }
}