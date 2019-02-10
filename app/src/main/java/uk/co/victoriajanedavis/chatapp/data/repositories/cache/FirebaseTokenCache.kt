package uk.co.victoriajanedavis.chatapp.data.repositories.cache

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.FirebaseTokenSpModel
import uk.co.victoriajanedavis.chatapp.domain.SingularCache.SingularDiskCache
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class FirebaseTokenCache @Inject constructor(
    private val sharedPref: SharedPreferences
) : SingularDiskCache<FirebaseTokenSpModel> {

    @SuppressLint("ApplySharedPref")
    override fun putSingular(value: FirebaseTokenSpModel) {
        sharedPref.edit()
            .putString(FirebaseTokenSpModel.PREF_TOKEN_KEY, value.token)
            // Using commit instead of apply since this is done on a background thread anyways so should be synchronous
            .commit()
    }

    @SuppressLint("ApplySharedPref")
    override fun clear() {
        sharedPref.edit()
            .remove(FirebaseTokenSpModel.PREF_TOKEN_KEY)
            .commit()
    }

    override fun getSingular(): Observable<FirebaseTokenSpModel> {
        return Observable.fromCallable {
            FirebaseTokenSpModel(sharedPref.getString(FirebaseTokenSpModel.PREF_TOKEN_KEY, EMPTY_FIELD)!!)
        }
    }

    companion object {
        private const val EMPTY_FIELD = ""
    }
}