package uk.co.victoriajanedavis.chatapp.presentation.common

import androidx.fragment.app.Fragment
import uk.co.victoriajanedavis.chatapp.ChatApp

open class BaseFragment : Fragment() { //DaggerFragment() {

    override fun onDestroy() {
        super.onDestroy()
        (activity?.application as ChatApp).mustDie(this)
    }
}