package uk.co.victoriajanedavis.chatapp.presentation.notifications.message

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.injection.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class ReplyAction @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Create the reply action and add the remote input.
    fun createReplyAction(chatUuid: UUID, notificationTag: String, channelId: String): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            R.drawable.ic_send_blue_transparent_36dp,
            ACTION_REPLY_LABEL, createReplyActionPendingIntent(chatUuid, notificationTag, channelId))
            .addRemoteInput(createRemoteInput())
            .build()
    }

    // Build a PendingIntent for the reply action to trigger.
    // - https://developer.android.com/training/notify-user/build-notification#add-reply-action
    private fun createReplyActionPendingIntent(
        chatUuid: UUID,
        notificationTag: String,
        channelId: String
    ): PendingIntent {
        return PendingIntent.getService(
            context,
            0,  // notificationId
            ReplyActionService.newIntent(
                context,
                chatUuid,
                notificationTag,
                channelId
            ),
             PendingIntent.FLAG_ONE_SHOT)
    }

    private fun createRemoteInput(): RemoteInput {
        return RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(ACTION_REPLY_LABEL)
            build()
        }
    }

    companion object {
        const val KEY_TEXT_REPLY = "chatapp_message_key_text_reply"
        private const val ACTION_REPLY_LABEL = "Reply"
    }
}