package uk.co.victoriajanedavis.chatapp.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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