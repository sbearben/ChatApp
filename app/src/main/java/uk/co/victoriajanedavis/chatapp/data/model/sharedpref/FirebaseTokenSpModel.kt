package uk.co.victoriajanedavis.chatapp.data.model.sharedpref

data class FirebaseTokenSpModel(
    val token: String
) {

    companion object {
        const val PREF_TOKEN_KEY = "firebaseTokenKey"
    }
}