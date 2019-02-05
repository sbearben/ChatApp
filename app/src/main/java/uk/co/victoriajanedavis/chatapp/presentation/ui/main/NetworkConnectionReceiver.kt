package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import javax.inject.Inject

class NetworkConnectionReceiver @Inject constructor() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val isNetworkAvailable = !(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true))

        if (isNetworkAvailable) {
            context.startService(SyncService.newIntent(context))
            Toast.makeText(context, "Network is available", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(context, "Network is not available", Toast.LENGTH_LONG).show()
        }
    }
}