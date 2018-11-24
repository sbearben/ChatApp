package uk.co.victoriajanedavis.chatapp.data.mappers;

import io.reactivex.annotations.NonNull;
import uk.co.victoriajanedavis.chatapp.data.model.network.TokenNwModel;
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel;
import uk.co.victoriajanedavis.chatapp.domain.common.Mapper;

public class TokenNwSpMapper extends Mapper<TokenNwModel, TokenSpModel> {

    @Override
    public TokenSpModel mapFrom(@NonNull TokenNwModel from) {
        TokenSpModel spModel = new TokenSpModel();
        spModel.setToken(from.getToken());
        spModel.setUserUuid(from.getUser().getUuid());
        spModel.setUserUsername(from.getUser().getUsername());
        spModel.setUserEmail(from.getUser().getEmail());

        return spModel;
    }
}
