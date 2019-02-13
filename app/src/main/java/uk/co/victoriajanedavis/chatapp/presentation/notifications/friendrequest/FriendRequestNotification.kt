package uk.co.victoriajanedavis.chatapp.presentation.notifications.friendrequest

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.data.model.websocket.CreatedFriendRequestWsModel
import uk.co.victoriajanedavis.chatapp.data.model.websocket.MessageWsModel
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ApplicationContext
import uk.co.victoriajanedavis.chatapp.presentation.notifications.ID
import uk.co.victoriajanedavis.chatapp.presentation.notifications.Notification
import uk.co.victoriajanedavis.chatapp.presentation.notifications.registerNotificationChannel
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainActivity
import java.util.*
import javax.inject.Inject

class FriendRequestNotification @Inject constructor(
    @ApplicationContext private val context: Context,
    private val acceptAction: AcceptAction
): Notification<CreatedFriendRequestWsModel> {

    override fun issueNotification(model: CreatedFriendRequestWsModel) {
        with(NotificationManagerCompat.from(context)) {
            val notificationTag = generateNotificationTag(model.senderUuid)
            //notificationId is a unique int for each notification that you must define
            notify(
                notificationTag, ID,  // I think each message notificationId should be unique per chat (not message) - so messages from same user will use same notification
                createNotificationBuilder(
                    content = "\'${model.senderUsername}\' sent you a friend request",
                    relevantUuid = model.senderUuid,
                    username = model.senderUsername,
                    notificationTag = notificationTag
                ).build()
            )
        }
    }

    private fun createNotificationBuilder(
        content: String,
        relevantUuid: UUID,
        username: String,
        notificationTag: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_chat_black_72dp)
            .setContentTitle("New Friend Request")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(createPendingIntent(relevantUuid))
            // Set the accept action
            .addAction(
                R.drawable.ic_add_white_24dp,
                "Accept",
                acceptAction.createAcceptActionPendingIntent(username, CHANNEL_ID, notificationTag))
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
            putString(EXTRA_NOTIFICATION_TYPE, "friend_request")
            putString(EXTRA_USER_UUID, userUuid.toString())
        }
    }

    private fun generateNotificationTag(userUuid: UUID): String {
        return FRIEND_REQUESTS_TAG_PREFIX + userUuid.toString()
    }

    companion object {
        private const val FRIEND_REQUESTS_TAG_PREFIX = "chatapp_friendrequests"

        private const val CHANNEL_ID = "friendrequests_channel_id"
        private const val CHANNEL_NAME = "Friend Requests"
        private const val CHANNEL_DESCRIPTION = "Received friend requests"

        const val EXTRA_NOTIFICATION_TYPE = "uk.co.victoriajanedavis.chatapp.MainActivity.notification_type"
        const val EXTRA_USER_UUID = "uk.co.victoriajanedavis.chatapp.MainActivity.user_uuid"

        fun registerNotificationChannel(context: Context) {
            return registerNotificationChannel(context, CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESCRIPTION)
        }
    }
}