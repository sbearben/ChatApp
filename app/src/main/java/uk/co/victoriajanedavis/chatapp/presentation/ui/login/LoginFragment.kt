package uk.co.victoriajanedavis.chatapp.presentation.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_login.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.common.ext.*
import javax.inject.Inject

class LoginFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelStateObserver()
        setupLoginButtonClickListener()
        setupSwitchToSignupPageClickListener()
    }

    private fun setupViewModelStateObserver() {
        viewModel.getLoginUserLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun setupLoginButtonClickListener() {
        loginButton.setOnClickListener {
            hideKeyboard()
            viewModel.loginUser(usernameEditText.text.toString(), passwordEditText.text.toString())
        }
    }

    private fun setupSwitchToSignupPageClickListener() {
        signupLinkTextView.setOnClickListener {
            hideKeyboard()
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun onStateChanged(state: State<Void>) = when(state) {
        is ShowContent -> onLoginSuccessful()
        is ShowLoading -> showLoading()
        is ShowError -> showError(state.message)
    }

    private fun onLoginSuccessful() {
        //findNavController().navigateUp()
        findNavController().navigate(R.id.action_loginFragment_to_chatFlowGraph)
    }

    private fun showLoading() {
        disableViews()
        progressBar.visible()
    }

    private fun showError(message: String) {
        enableViews()
        progressBar.invisible()

        showSnackbar(message, Snackbar.LENGTH_LONG)
    }

    private fun disableViews() {
        setViewsEnabled(false)
    }

    private fun enableViews() {
        setViewsEnabled(true)
    }

    private fun setViewsEnabled(areEnabled: Boolean) {
        usernameEditText.isEnabled = areEnabled
        passwordEditText.isEnabled = areEnabled
        loginButton.isEnabled = areEnabled
        signupLinkTextView.isEnabled = areEnabled
    }
}