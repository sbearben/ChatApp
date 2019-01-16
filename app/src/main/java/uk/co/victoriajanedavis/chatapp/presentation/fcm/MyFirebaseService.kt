package uk.co.victoriajanedavis.chatapp.presentation.fcm

import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.data.realtime.fcm.FirebaseMessageResolver
import uk.co.victoriajanedavis.chatapp.data.realtime.websocket.RealtimeStreamsLifeManager
import uk.co.victoriajanedavis.chatapp.domain.interactors.FirebaseTokenRefresher
import uk.co.victoriajanedavis.chatapp.domain.interactors.FullSync

import javax.inject.Inject

class MyFirebaseService : FirebaseMessagingService() {

    @Inject lateinit var realtimeStreamsLifeManager: RealtimeStreamsLifeManager
    @Inject lateinit var messageResolver: FirebaseMessageResolver
    @Inject lateinit var tokenRefresher: FirebaseTokenRefresher
    @Inject lateinit var fullSync: FullSync

    private val disposables: CompositeDisposable = CompositeDisposable()


    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this);

        realtimeStreamsLifeManager.initializeStreams()

        disposables.add(tokenRefresher.getRefreshSingle(null)
            .subscribeOn(Schedulers.io())  // TODO: do we need this since MyFirebaseService is on its own thread?
            .subscribe({ Log.d(TAG, "Posting token to backend succeeded") }, { e -> Log.d(TAG, "Posting token to backend failed: " + e.message) }))

        Log.d(TAG, " onCreate() Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        realtimeStreamsLifeManager.clearStreams()
        disposables.clear()
        Log.d(TAG, " onDestroy() Called")
    }

    override fun onNewToken(s: String?) {
        Log.d(TAG, "New token: $s")
        tokenRefresher.storeToken(s!!).subscribe()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "Message Received")
        messageResolver.resolveMessage(remoteMessage!!.data)
        /*
        if (// check if we need long running process) {
            // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            scheduleJob();
        } else {
            // Handle message within 10 seconds
            handleNow();
        }
        */
        // Send notification
    }

    override fun onDeletedMessages() {
        Log.d(TAG, "Deleted Messages")
        disposables.add(fullSync.initiateSync()
            .subscribe({}, { e -> Log.d(TAG, "Full sync failed: " + e.message) }
        ))
    }


    // The following is from the "firebase/quickstart-android" repo on Github:
    // - https://github.com/firebase/quickstart-android/tree/master/messaging


    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private fun scheduleJob() {
        /*
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        */
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    companion object {

        private val TAG = "FirebaseService"
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    /*
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Below, 0 is the Request Code
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Below, 0 is ID of notification
        notificationManager.notify(0, notificationBuilder.build());
    }
    */
}
