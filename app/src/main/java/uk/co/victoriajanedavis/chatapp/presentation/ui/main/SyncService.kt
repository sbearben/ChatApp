package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.os.IBinder
import dagger.android.DaggerService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.interactors.FullSync
import javax.inject.Inject

class SyncService : DaggerService() {

    @Inject lateinit var fullSync: FullSync
    private val disposables = CompositeDisposable()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        disposables.apply {
            clear()
            add(fullSync.getActionCompletable(0)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { stopSelf() },
                    { _ -> stopSelf() }
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

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SyncService::class.java)
        }
    }
}