package uk.co.victoriajanedavis.chatapp.presentation;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import uk.co.victoriajanedavis.chatapp.data.mappers.MessageNwDbMapper;
import uk.co.victoriajanedavis.chatapp.data.model.network.MessageNwModel;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;

import java.util.Map;

import javax.inject.Inject;

public class MyFirebaseService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseService";

    @Inject ChatAppDatabase database;
    @Inject Gson gson;

    private MessageNwDbMapper messageMapper = new MessageNwDbMapper();


    @Override
    public void onCreate() {
        super.onCreate();
        // For when we implement dagger.android
        // AndroidInjection.inject(this);
    }

    @Override
    public void onNewToken(String s) {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        Map<String, String> messageData = remoteMessage.getData();

        String messageType = messageData.get("type");
        if (messageType == null) {  // Any messages received must contain "type" element
            return;
        }

        if (messageData.size() > 0) {
            switch(messageType) {
                case "MESSAGE":
                    MessageNwModel messageNwModel = gson.fromJson(messageData.get("json"), MessageNwModel.class);
                    database.messageDao().insertMessage(messageMapper.mapFrom(messageNwModel));
                    break;
            }

            /*
            if (// check if we need long running process) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
            */
        }
        // Send notification
    }

    @Override
    public void onDeletedMessages() {
        /* If this is called it means there were too many messages (>100) pending for your this
           particular device at the time it reconnected or if this device hasn't connected to FCM
           in more than one month:
           - this means we should now initiate a FULL SYNC with the app server
         */
    }






    // The following is from the "firebase/quickstart-android" repo on Github:
    // - https://github.com/firebase/quickstart-android/tree/master/messaging


    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
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
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
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
