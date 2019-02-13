package uk.co.victoriajanedavis.chatapp.presentation.notifications.friendrequest

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import dagger.android.DaggerService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.interactors.AcceptReceivedFriendRequest
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage.MessageParams
import uk.co.victoriajanedavis.chatapp.presentation.notifications.ID
import java.util.*
import javax.inject.Inject

class AcceptActionService : DaggerService() {

    @Inject lateinit var acceptFriendRequest: AcceptReceivedFriendRequest
    private val disposables = CompositeDisposable()

    private var notificationTag = ""
    private var channelId = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AcceptActionService", "onStartCommand")
        notificationTag = intent!!.getStringExtra(EXTRA_NOTIFICATION_TAG)
        channelId = intent.getStringExtra(EXTRA_CHANNEL_ID)

        val username = UUID.fromString(intent.getStringExtra(EXTRA_USERNAME))

        disposables.apply {
            clear()
            add(acceptFriendRequest.getActionCompletable(username)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { onSuccess() },
                    { e -> onError(e) }
                )
            )
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d("AcceptActionService", "onDestroy")
        super.onDestroy()
        disposables.dispose()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun onSuccess() {
        issueNotification("Friend request accepted")
        stopSelf()
    }

    private fun onError(e: Throwable) {
        issueNotification("Error accepting friend request: ${e.message ?: e.toString()}")
        stopSelf()
    }

    private fun issueNotification(contextText: String) {
        NotificationManagerCompat.from(this).apply {
            notify(notificationTag, ID, createNotification(contextText))
        }
    }

    private fun createNotification(contentText: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_chat_black_72dp)
            .setContentText(contentText)
            .setAutoCancel(true)
            .build()
    }

    companion object {
        fun newIntent(context: Context, username: String, notificationTag: String, channelId: String): Intent {
            return Intent(context, AcceptActionService::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
                putExtra(EXTRA_NOTIFICATION_TAG, notificationTag)
                putExtra(EXTRA_CHANNEL_ID, channelId)
            }
        }

        private const val EXTRA_USERNAME = "uk.co.victoriajanedavis.chatapp.AcceptActionService.username"
        private const val EXTRA_NOTIFICATION_TAG = "uk.co.victoriajanedavis.chatapp.AcceptActionService.notification_tag"
        private const val EXTRA_CHANNEL_ID = "uk.co.victoriajanedavis.chatapp.AcceptActionService.channel_id"
    }
}