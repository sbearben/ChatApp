package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.victoriajanedavis.chatapp.domain.interactors.IsUserLoggedIn
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val isUserLoggedIn: IsUserLoggedIn
) : ViewModel() {

    private val isUserLoggedInLiveData = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(bindToUseCase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getIsUserLoggedInLiveData(): LiveData<Boolean> = isUserLoggedInLiveData

    private fun bindToUseCase() : Disposable {
        return isUserLoggedIn.getBehaviorStream(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isUserLoggedIn -> isUserLoggedInLiveData.value = isUserLoggedIn },
                    { e -> Log.e("MainViewModel", "Error getting isUserLoggedIn boolean", e)})
    }
}