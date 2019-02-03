package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.support.DaggerAppCompatActivity
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: MainActivityViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
        navController = findNavController(R.id.nav_host)

        //addNavControllerDestinationChangedListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() ||  super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val currentDestination = navController.currentDestination
        when(currentDestination?.id) {
            R.id.loginFragment, R.id.friendsFragment -> {
                Log.d("MainActivity", "loginFragment: finish()")
                //finish()
            }
        }
        super.onBackPressed()
    }

    /*
    private fun addNavControllerDestinationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
        }
    }
    */

}