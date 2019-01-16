package uk.co.victoriajanedavis.chatapp.data.repositories.cache

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

    override fun putSingular(value: FirebaseTokenSpModel) {
        sharedPref.edit()
            .putString(FirebaseTokenSpModel.PREF_TOKEN_KEY, value.token)
            .apply()
    }

    override fun clear() {
        sharedPref.edit().remove(FirebaseTokenSpModel.PREF_TOKEN_KEY).apply()
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