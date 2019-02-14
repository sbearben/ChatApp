package uk.co.victoriajanedavis.chatapp.presentation.ui.signup

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_signup.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.common.ViewModelFactory
import uk.co.victoriajanedavis.chatapp.presentation.ext.*
import javax.inject.Inject

class SignupFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: SignupViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignupViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelStateObserver()
        setupCreateAccountButtonClickListener()
        setupSwitchToLoginPageClickListener()
    }

    private fun setupViewModelStateObserver() {
        viewModel.getRegisterUserLiveData().observe(viewLifecycleOwner) {
            it?.let(::onStateChanged)
        }
    }

    private fun setupCreateAccountButtonClickListener() {
        createAccountButton.setOnClickListener {
            hideKeyboard()
            viewModel.registerUser(usernameEditText.text.toString(), emailEditText.text.toString(),
                passwordOneEditText.text.toString(), passwordTwoEditText.text.toString())
        }
    }

    private fun setupSwitchToLoginPageClickListener() {
        loginLinkTextview.setOnClickListener {
            hideKeyboard()
            findNavController().popBackStack()
        }
    }

    private fun onStateChanged(state: State<Void>) = when(state) {
        is ShowContent -> registrationSuccessful()
        is ShowLoading -> showLoading()
        is ShowError -> showError(state.message)
    }

    private fun registrationSuccessful() {
        //findNavController().popBackStack(R.id.mainFragment, false)
        findNavController().navigate(R.id.action_signupFragment_to_chatFlowGraph)
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
        emailEditText.isEnabled = areEnabled
        passwordOneEditText.isEnabled = areEnabled
        passwordTwoEditText.isEnabled = areEnabled
        createAccountButton.isEnabled = areEnabled
        loginLinkTextview.isEnabled = areEnabled
    }
}