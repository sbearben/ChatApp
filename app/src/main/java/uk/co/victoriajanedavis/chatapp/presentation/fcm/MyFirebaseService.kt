package uk.co.victoriajanedavis.chatapp.presentation.fcm

import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.realtime.RealtimeStreamsLifeManager
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessageDataStream
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessagingStreams
import uk.co.victoriajanedavis.chatapp.domain.interactors.FirebaseTokenRefresher
import uk.co.victoriajanedavis.chatapp.domain.interactors.FullSync
import uk.co.victoriajanedavis.chatapp.presentation.notifications.ChatAppNotificationManager

import javax.inject.Inject

class MyFirebaseService : FirebaseMessagingService() {

    @Inject lateinit var realtimeStreamsLifeManager: RealtimeStreamsLifeManager
    @Inject lateinit var messageDataStream: FirebaseMessageDataStream
    @Inject lateinit var tokenRefresher: FirebaseTokenRefresher

    @Inject lateinit var notificationManager: ChatAppNotificationManager
    @Inject lateinit var fullSync: FullSync

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)

        notificationManager.initializeStreams()
        realtimeStreamsLifeManager.initializeStreams()

        disposables.add(tokenRefresher.getRefreshSingle(null)
            .subscribeOn(Schedulers.io())  // do we need this since MyFirebaseService is on its own thread?
            .subscribe({}, { e -> Log.d(TAG, "Posting token to backend failed: " + e.message) })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.clearStreams()
        realtimeStreamsLifeManager.clearStreams()
        disposables.clear()
    }

    override fun onNewToken(s: String?) {
        Log.d(TAG, "New token: $s")
        tokenRefresher.storeToken(s!!).subscribe()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "Message Received")
        messageDataStream.push(remoteMessage!!.data)
    }

    override fun onDeletedMessages() {
        Log.d(TAG, "Deleted Messages")
        disposables.add(fullSync.getActionCompletable(0)
            .subscribeOn(Schedulers.io())
            .subscribe({}, { e -> Log.d(TAG, "Full sync failed: " + e.message) }
        ))
    }

    companion object {
        private const val TAG = "FirebaseService"
    }
}
