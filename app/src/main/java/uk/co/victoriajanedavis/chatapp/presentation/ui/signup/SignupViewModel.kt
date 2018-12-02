package uk.co.victoriajanedavis.chatapp.presentation.ui.signup

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.RegisterUser
import uk.co.victoriajanedavis.chatapp.domain.interactors.RegisterUser.RegisterParams
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.common.State.*
import javax.inject.Inject

class SignupViewModel @Inject constructor(
        private val registerUser: RegisterUser
) : ViewModel() {

    private val registerUserLiveData = MutableLiveData<State<TokenEntity>>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getRegisterUserLiveData(): LiveData<State<TokenEntity>> = registerUserLiveData

    fun registerUser(username: String, email: String, password1: String, password2: String) {
        registerUserLiveData.value = ShowLoading
        compositeDisposable.add(bindToUseCase(RegisterParams(username, email, password1, password2)))
    }

    private fun bindToUseCase(loginParams: RegisterParams) : Disposable {
        return registerUser.getSingle(loginParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ tokenEntity -> registerUserLiveData.value = ShowContent(tokenEntity) },
                        { e -> registerUserLiveData.value = ShowError(e.toString()) })
    }
}