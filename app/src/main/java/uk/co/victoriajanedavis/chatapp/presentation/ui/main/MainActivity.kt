package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.support.DaggerAppCompatActivity
import uk.co.victoriajanedavis.chatapp.BuildConfig
import uk.co.victoriajanedavis.chatapp.ChatApp
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.common.InjectingFragmentFactory
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var injectingFragmentFactory: InjectingFragmentFactory

    private lateinit var viewModel: MainViewModel

    private val connectionReceiver: NetworkConnectionReceiver by lazy { NetworkConnectionReceiver() }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.fragmentFactory = injectingFragmentFactory

        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        navController = findNavController(R.id.nav_host)
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

    /*
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
    */

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }
}