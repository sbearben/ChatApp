package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.support.DaggerAppCompatActivity
import uk.co.victoriajanedavis.chatapp.BuildConfig
import uk.co.victoriajanedavis.chatapp.ChatApp
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ext.lazyAndroid
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: MainViewModel

    private val connectionReceiver: NetworkConnectionReceiver by lazyAndroid { NetworkConnectionReceiver() }
    private val navController: NavController by lazyAndroid { findNavController(R.id.nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        //navController = findNavController(R.id.nav_host)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            connectionReceiver,
            IntentFilter(ChatApp.ACTION_CONNECTIVITY_CHANGE),
            BuildConfig.APP_PRIVATE_PREMISSION,
            null
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectionReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() ||  super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        navController.currentDestination.also { destination ->
            when(destination?.id) {
                R.id.loginFragment -> {
                    Log.d("MainActivity", "loginFragment: finish()")
                }
            }
        }
        super.onBackPressed()
    }

}