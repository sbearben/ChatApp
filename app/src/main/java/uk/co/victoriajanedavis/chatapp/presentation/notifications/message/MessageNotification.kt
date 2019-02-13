package uk.co.victoriajanedavis.chatapp.presentation.notifications.message

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.data.model.websocket.MessageWsModel
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ApplicationContext
import uk.co.victoriajanedavis.chatapp.presentation.notifications.ID
import uk.co.victoriajanedavis.chatapp.presentation.notifications.Notification
import uk.co.victoriajanedavis.chatapp.presentation.notifications.registerNotificationChannel
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainActivity
import java.util.UUID
import javax.inject.Inject

class MessageNotification @Inject constructor(
    @ApplicationContext private val context: Context,
    private val replyAction: ReplyAction
): Notification<MessageWsModel> {

    override fun issueNotification(model: MessageWsModel) {
        with(NotificationManagerCompat.from(context)) {
            val notificationTag = generateNotificationTag(model.chatUuid)
            //notificationId is a unique int for each notification that you must define
            notify(
                notificationTag, ID,  // I think each message notificationId should be unique per chat (not message) - so messages from same user will use same notification
                createNotificationBuilder(
                    smallIconResId = R.drawable.ic_chat_black_72dp,
                    title = model.senderUsername,
                    content = model.message,
                    relevantUuid = model.chatUuid,
                    notificationTag = notificationTag
                ).build()
            )
        }
    }

    private fun createNotificationBuilder(
        smallIconResId: Int,
        title: String,
        content: String,
        relevantUuid: UUID,
        notificationTag: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIconResId)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(createPendingIntent(relevantUuid))
            // Set the reply action
            .addAction(replyAction.createReplyAction(
                relevantUuid,
                notificationTag,
                CHANNEL_ID)
            )
            .setAutoCancel(true)
    }

    private fun createPendingIntent(chatUuid: UUID): PendingIntent {
        return PendingIntent.getActivity(
            context,
            0,
            MainActivity.newIntent(context),
            PendingIntent.FLAG_CANCEL_CURRENT,
            createBundle(chatUuid)
        )
    }

    private fun createBundle(chatUuid: UUID): Bundle {
        return Bundle().apply {
            putString(EXTRA_NOTIFICATION_TYPE, "message")
            putString(EXTRA_CHAT_UUID, chatUuid.toString())
        }
    }

    private fun generateNotificationTag(chatUuid: UUID): String {
        return MESSAGE_TAG_PREFIX + chatUuid.toString()
    }

    companion object {
        private const val MESSAGE_TAG_PREFIX = "chatapp_message"

        private const val CHANNEL_ID = "message_channel_id"
        private const val CHANNEL_NAME = "Messages"
        private const val CHANNEL_DESCRIPTION = "Received messages"

        const val EXTRA_NOTIFICATION_TYPE = "uk.co.victoriajanedavis.chatapp.MainActivity.notification_type"
        const val EXTRA_CHAT_UUID = "uk.co.victoriajanedavis.chatapp.MainActivity.chat_uuid"

        fun registerNotificationChannel(context: Context) {
            return registerNotificationChannel(context, CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESCRIPTION)
        }
    }
}