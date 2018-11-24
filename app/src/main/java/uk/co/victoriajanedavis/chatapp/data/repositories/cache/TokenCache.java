package uk.co.victoriajanedavis.chatapp.data.repositories.cache;

import android.content.SharedPreferences;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope;
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.TokenSpModel;
import uk.co.victoriajanedavis.chatapp.domain.Cache.DiskCache;

@ApplicationScope
public class TokenCache implements DiskCache<Void, TokenSpModel> {

    private static final String PREF_FILE_NAME = "android_chat_app_pref_file";

    private static final String EMPTY_FIELD = "";

    private final SharedPreferences sharedPref;


    @Inject
    public TokenCache(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
    }

    @Override
    public void putSingular(@NonNull TokenSpModel tokenSpModel) {
        sharedPref.edit()
                .putString(TokenSpModel.PREF_TOKEN_KEY, tokenSpModel.getToken())
                .putString(TokenSpModel.PREF_USER_UUID, tokenSpModel.getUserUuid().toString())
                .putString(TokenSpModel.PREF_USER_USERNAME, tokenSpModel.getUserUsername())
                .putString(TokenSpModel.PREF_USER_EMAIL, tokenSpModel.getUserEmail())
                .apply();
    }

    @Override
    public void putAll(@NonNull List<TokenSpModel> tokenSpModels) { ;
    }

    @Override
    public void replaceAll(@Nullable Void aVoid, @NonNull List<TokenSpModel> tokenSpModels) {
    }

    @Override
    public void delete(@NonNull TokenSpModel tokenSpModel) {
    }

    @Override
    public void clear() {
        sharedPref.edit().clear().apply();
    }

    @NonNull
    @Override
    public Observable<TokenSpModel> getSingular(@NonNull Void aVoid) {
        return Observable.fromCallable(() -> {
            TokenSpModel token = new TokenSpModel();

            token.setToken(sharedPref.getString(TokenSpModel.PREF_TOKEN_KEY, EMPTY_FIELD));
            token.setUserUuid(validUuidOrNullIfEmptyString(sharedPref.getString(TokenSpModel.PREF_USER_UUID, EMPTY_FIELD)));
            token.setUserUsername(sharedPref.getString(TokenSpModel.PREF_USER_USERNAME, EMPTY_FIELD));
            token.setUserEmail(sharedPref.getString(TokenSpModel.PREF_USER_EMAIL, EMPTY_FIELD));

            return token;
        });
    }

    @NonNull
    @Override
    public Observable<List<TokenSpModel>> getAll(@Nullable Void aVoid) {
        return Observable.empty();
    }

    private UUID validUuidOrNullIfEmptyString(String uuidStr) {
        return (uuidStr == null || uuidStr.equals(EMPTY_FIELD)) ? null : UUID.fromString(uuidStr);
    }
}
