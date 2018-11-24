package uk.co.victoriajanedavis.chatapp.presentation.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_signup.*
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import uk.co.victoriajanedavis.chatapp.presentation.ext.invisible
import uk.co.victoriajanedavis.chatapp.presentation.ext.observe
import uk.co.victoriajanedavis.chatapp.presentation.ext.visible
import javax.inject.Inject

class SignupFragment : DaggerFragment() {

    @Inject lateinit var viewModel: SignupViewModel

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
            viewModel.registerUser(usernameEditText.text.toString(), emailEditText.text.toString(),
                passwordOneEditText.text.toString(), passwordTwoEditText.text.toString())
        }
    }

    private fun setupSwitchToLoginPageClickListener() {
        loginLinkTextview.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onStateChanged(state: State<TokenEntity>) = when(state) {
        is ShowContent -> registrationSuccessful()
        is ShowLoading -> showLoading()
        is ShowError -> showError(state.message)
    }

    private fun registrationSuccessful() {
        findNavController().popBackStack(R.id.mainFragment, false)
    }

    private fun showLoading() {
        disableViews()
        progressBar.visible()
    }

    private fun showError(message: String) {
        enableViews()
        progressBar.invisible()
        Log.e("SignupFragment", message)
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