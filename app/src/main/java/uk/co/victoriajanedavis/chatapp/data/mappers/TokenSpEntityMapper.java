package uk.co.victoriajanedavis.chatapp.data.mappers;

import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;

public class TokenSpEntityMapper extends Mapper<TokenSpModel, TokenEntity> {

    @Override
    public TokenEntity mapFrom(TokenSpModel from) {
        TokenEntity entity = new TokenEntity();

        entity.setToken(from.getToken());
        entity.setUserUuid(from.getUserUuid());
        entity.setUserUsername(from.getUserUsername());
        entity.setUserEmail(from.getUserEmail());

        return entity;
    }
}
