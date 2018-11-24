package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.support.DaggerAppCompatActivity
import uk.co.victoriajanedavis.chatapp.R

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() ||  super.onSupportNavigateUp()
    }

    /*
    override fun onBackPressed() {
        val currentDestination=navController.currentDestination
        when(currentDestination?.id) {
            R.id.loginFragment -> {
                finish()
            }
        }
        super.onBackPressed()
    }
    */

}