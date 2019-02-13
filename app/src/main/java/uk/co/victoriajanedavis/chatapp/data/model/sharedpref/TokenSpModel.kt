package uk.co.victoriajanedavis.chatapp.data.model.sharedpref

import java.util.UUID


data class TokenSpModel(
    val token: String,
    val userUuid: UUID? = null,
    val userUsername: String,
    val userEmail: String
) {

    companion object {
        const val PREF_TOKEN_KEY = "tokenKey"
        const val PREF_USER_UUID_KEY = "userUuidKey"
        const val PREF_USER_USERNAME_KEY = "userUsernameKey"
        const val PREF_USER_EMAIL_KEY = "userEmailKey"
    }
}
