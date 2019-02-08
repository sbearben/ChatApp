package uk.co.victoriajanedavis.chatapp.presentation.notifications.message

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ApplicationContext
import uk.co.victoriajanedavis.chatapp.presentation.notifications.ID
import uk.co.victoriajanedavis.chatapp.presentation.notifications.registerNotificationChannel
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainActivity
import java.util.*
import javax.inject.Inject

class MessageNotification @Inject constructor(
    @ApplicationContext private val context: Context,
    private val replyAction: ReplyAction
) {

    fun issueNotification(message: MessageEntity) {
        with(NotificationManagerCompat.from(context)) {
            val notificationTag = generateNotificationTag(message.chatUuid)
            //notificationId is a unique int for each notification that you must define
            notify(
                notificationTag, ID,  // I think each message notificationId should be unique per chat (not message) - so messages from same user will use same notification
                createNotificationBuilder(message, notificationTag).build()
            )
        }
    }

    private fun createNotificationBuilder(
        message: MessageEntity,
        notificationTag: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_chat_black_72dp)
            .setContentTitle(message.userUsername)
            .setContentText(message.text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(createPendingIntent(message.chatUuid))
            // Set the reply action
            .addAction(replyAction.createReplyAction(
                message.chatUuid,
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
            0,
            createBundle(chatUuid)
        )
    }

    private fun createBundle(chatUuid: UUID): Bundle {
        return Bundle().apply {
            putString("notification_type", "message")
            putString("chat_uuid", chatUuid.toString())
        }
    }

    private fun generateNotificationTag(chatUuid: UUID): String {
        return MESSAGE_TAG_PREFIX + chatUuid.toString()
    }

    /***** End of Reply Action stuff *****/

    companion object {
        private const val MESSAGE_TAG_PREFIX = "chatapp_message"

        private const val CHANNEL_ID = "message_channel_id"
        private const val CHANNEL_NAME = "Messages"
        private const val CHANNEL_DESCRIPTION = "Received messages"

        fun registerNotificationChannel(context: Context) {
            return registerNotificationChannel(context, CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESCRIPTION)
        }
    }
}