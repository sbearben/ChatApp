package uk.co.victoriajanedavis.chatapp.data.repositories.cache

import android.annotation.SuppressLint
import android.content.SharedPreferences
import java.util.UUID

import javax.inject.Inject

import io.reactivex.Observable
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel
import uk.co.victoriajanedavis.chatapp.domain.SingularCache

@ApplicationScope
class TokenCache @Inject constructor(
    private val sharedPref: SharedPreferences
) : SingularCache.SingularDiskCache<TokenSpModel> {

    @SuppressLint("ApplySharedPref")
    override fun putSingular(value: TokenSpModel) {
        sharedPref.edit()
            .putString(TokenSpModel.PREF_TOKEN_KEY, value.token)
            .putString(TokenSpModel.PREF_USER_UUID_KEY, value.userUuid.toString())
            .putString(TokenSpModel.PREF_USER_USERNAME_KEY, value.userUsername)
            .putString(TokenSpModel.PREF_USER_EMAIL_KEY, value.userEmail)
            // Using commit instead of apply since this is done on a background thread anyways so should be synchronous
            .commit()
    }

    @SuppressLint("ApplySharedPref")
    override fun clear() {
        sharedPref.edit()
            .remove(TokenSpModel.PREF_TOKEN_KEY)
            .remove(TokenSpModel.PREF_USER_UUID_KEY)
            .remove(TokenSpModel.PREF_USER_USERNAME_KEY)
            .remove(TokenSpModel.PREF_USER_EMAIL_KEY)
            .commit()
    }

    override fun getSingular(): Observable<TokenSpModel> {
        return Observable.fromCallable {
            val token = TokenSpModel()

            token.token = sharedPref.getString(TokenSpModel.PREF_TOKEN_KEY, EMPTY_FIELD)
            token.userUuid = validUuidOrNullIfEmptyString(
                sharedPref.getString(TokenSpModel.PREF_USER_UUID_KEY, EMPTY_FIELD)
            )
            token.userUsername = sharedPref.getString(TokenSpModel.PREF_USER_USERNAME_KEY, EMPTY_FIELD)
            token.userEmail = sharedPref.getString(TokenSpModel.PREF_USER_EMAIL_KEY, EMPTY_FIELD)

            return@fromCallable token
        }
    }

    private fun validUuidOrNullIfEmptyString(uuidStr: String?): UUID? {
        return if (uuidStr == null || uuidStr == EMPTY_FIELD) null else UUID.fromString(uuidStr)
    }

    companion object {
        private const val EMPTY_FIELD = ""
    }
}
