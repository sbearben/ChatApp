package uk.co.victoriajanedavis.chatapp.presentation.common

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment

class FactoryNavHostFragment : NavHostFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Get the FragmentFactory set on the Activity's fragmentManager and set it on the child FragmentManager
        // because in navigation, our fragments are children of the NavHostFragment
        fragmentManager?.let {
            childFragmentManager.fragmentFactory = it.fragmentFactory
        }
        super.onCreate(savedInstanceState)
    }
}