package uk.co.victoriajanedavis.chatapp.presentation.notifications

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainActivity
import java.util.UUID

abstract class BaseNotification<T> constructor(
    private val context: Context
): Notification<T> {

    abstract val title: String
    abstract val content: String

    private lateinit var notificationTag: String

    override fun issueNotification(model: T) {
        with(NotificationManagerCompat.from(context)) {
            notificationTag = generateNotificationTag(model)
            notify(
                notificationTag, ID,  // I think each message notificationId should be unique per chat (not message) - so messages from same user will use same notification
                addOptionalItems(
                    createNotificationBuilder(
                        model = model),
                    model
                ).build()
            )
        }
    }

    private fun createNotificationBuilder(model: T): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_chat_black_72dp)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(createPendingIntent(model))
            .setAutoCancel(true)
    }

    private fun createPendingIntent(model: T): PendingIntent {
        return PendingIntent.getActivity(
            context,
            0,
            MainActivity.newIntent(context),
            PendingIntent.FLAG_CANCEL_CURRENT,
            createBundle(model)
        )
    }

    open fun addOptionalItems(
        baseBuilder: NotificationCompat.Builder,
        model: T
    ): NotificationCompat.Builder {
        return baseBuilder
    }

    internal abstract fun createBundle(model: T): Bundle

    internal abstract fun getIdentifyingUuid(model: T): UUID


    private fun generateNotificationTag(model: T): String {
        return MESSAGE_TAG_PREFIX + getIdentifyingUuid(model).toString()
    }

    companion object {
        private const val MESSAGE_TAG_PREFIX = "chatapp_new_friend"

        private const val CHANNEL_ID = "new_friend_channel_id"
    }
}