package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ext.observe
import javax.inject.Inject

class MainFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MainFragment", "MAIN FRAGMENT CALLED 1")
        viewModel.getIsUserLoggedInLiveData().observe(viewLifecycleOwner) {
            Log.d("MainFragment", "MAIN FRAGMENT CALLED 2: $it")
            when(it) {
                false -> {
                    findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
                    Log.d("MainFragment", "Current destination: ${findNavController().currentDestination}")
                }
                true -> {
                    findNavController().navigate(R.id.action_mainFragment_to_friendsFragment)
                    Log.d("MainFragment", "Current destination: ${findNavController().currentDestination}")
                }
            }
        }
    }


}