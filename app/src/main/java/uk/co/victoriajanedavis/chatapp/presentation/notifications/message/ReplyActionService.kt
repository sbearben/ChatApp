package uk.co.victoriajanedavis.chatapp.presentation.notifications.message

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import dagger.android.DaggerService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage.MessageParams
import uk.co.victoriajanedavis.chatapp.presentation.notifications.ID
import java.util.*
import javax.inject.Inject

class ReplyActionService : DaggerService() {

    @Inject lateinit var sendMessage: SendChatMessage
    private val disposables = CompositeDisposable()

    private var notificationTag = ""
    private var channelId = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationTag = intent!!.getStringExtra(EXTRA_NOTIFICATION_TAG)
        channelId = intent.getStringExtra(EXTRA_CHANNEL_ID)

        val chatUuid = UUID.fromString(intent.getStringExtra(EXTRA_CHAT_UUID))
        val message = getReplyText(intent)!!

        disposables.apply {
            clear()
            add(sendMessage.getSingle(MessageParams(chatUuid, message))
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { onReplySuccess() },
                    { e -> onReplyError(e) }
                )
            )
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun getReplyText(intent: Intent?): String? {
        return RemoteInput.getResultsFromIntent(intent)
            ?.getCharSequence(ReplyAction.KEY_TEXT_REPLY)
            .toString()
    }

    private fun onReplySuccess() {
        issueNotification("Reply sent")
        stopSelf()
    }

    private fun onReplyError(e: Throwable) {
        issueNotification(e.message ?: e.toString())
        stopSelf()
    }

    // Issue the new notification.
    private fun issueNotification(contextText: String) {
        NotificationManagerCompat.from(this).apply {
            notify(notificationTag, ID, createNotification(contextText))
        }
    }

     // Build a new notification, which informs the user that the system handled their
     // interaction with the previous notification.
    private fun createNotification(contentText: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_chat_black_72dp)
            .setContentText(contentText)
            //.setRemoteInputHistory()
            .build()
    }

    companion object {
        fun newIntent(context: Context, chatUuid: UUID, notificationTag: String, channelId: String): Intent {
            return Intent(context, ReplyActionService::class.java).apply {
                putExtra(EXTRA_CHAT_UUID, chatUuid.toString())
                putExtra(EXTRA_NOTIFICATION_TAG, notificationTag)
                putExtra(EXTRA_CHANNEL_ID, channelId)
            }
        }

        private const val EXTRA_CHAT_UUID = "uk.co.victoriajanedavis.chatapp.ReplyActionService.chat_uuid"
        private const val EXTRA_NOTIFICATION_TAG = "uk.co.victoriajanedavis.chatapp.ReplyActionService.notification_tag"
        private const val EXTRA_CHANNEL_ID = "uk.co.victoriajanedavis.chatapp.ReplyActionService.channel_id"
    }
}