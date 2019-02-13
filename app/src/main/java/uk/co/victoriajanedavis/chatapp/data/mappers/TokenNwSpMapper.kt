package uk.co.victoriajanedavis.chatapp.data.mappers

import io.reactivex.annotations.NonNull
import uk.co.victoriajanedavis.chatapp.data.model.network.TokenNwModel
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper

class TokenNwSpMapper : Mapper<TokenNwModel, TokenSpModel>() {

    override fun mapFrom(@NonNull from: TokenNwModel): TokenSpModel {
        return TokenSpModel(
            token = from.token,
            userUuid = from.user.uuid,
            userUsername = from.user.username,
            userEmail = from.user.email
        )
    }
}
