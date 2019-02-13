package uk.co.victoriajanedavis.chatapp.data.mappers

import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity

class TokenSpEntityMapper : Mapper<TokenSpModel, TokenEntity>() {

    override fun mapFrom(from: TokenSpModel): TokenEntity {
        return TokenEntity(
            token = from.token,
            userUuid = from.userUuid,
            userUsername = from.userUsername,
            userEmail = from.userEmail
        )
    }
}
