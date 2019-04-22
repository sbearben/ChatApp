package uk.co.victoriajanedavis.chatapp.data.cache

import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.FirebaseTokenSpModel
import javax.inject.Inject

class FakeFirebaseTokenCache @Inject constructor() : BaseSingularMemoryCache<FirebaseTokenSpModel>() {

    override fun createDefaultValue(): FirebaseTokenSpModel {
        return FirebaseTokenSpModel("")
    }
}