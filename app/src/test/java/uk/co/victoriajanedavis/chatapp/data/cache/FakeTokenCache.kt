package uk.co.victoriajanedavis.chatapp.data.cache

import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel
import javax.inject.Inject

class FakeTokenCache @Inject constructor() : BaseSingularMemoryCache<TokenSpModel>() {

    override fun createDefaultValue(): TokenSpModel {
        return TokenSpModel(
            token = "",
            userUsername = "",
            userEmail = ""
        )
    }
}