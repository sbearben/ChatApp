package uk.co.victoriajanedavis.chatapp.domain;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import uk.co.victoriajanedavis.chatapp.data.model.network.TokenNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.TokenCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.TokenReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.LoginUser;
import uk.co.victoriajanedavis.chatapp.domain.interactors.LoginUser.LoginParams;
import uk.co.victoriajanedavis.chatapp.common.BaseTest;
import uk.co.victoriajanedavis.chatapp.common.ModelGenerationUtil;

public class LoginUserTest extends BaseTest {

    private TokenReactiveStore tokenStore;
    private TokenRepository repository;
    private LoginUser interactor;

    @Mock
    private ChatAppService service;


    /*
    @Before
    public void setUp() {
        SharedPreferences sharedPref = InstrumentationRegistry.getContext()
                .getSharedPreferences("chat_test_pref", Context.MODE_PRIVATE);

        TokenCache tokenCache = new TokenCache(sharedPref);
        tokenStore = new TokenReactiveStore(tokenCache);

        repository = new TokenRepository(tokenStore, service);

        interactor = new LoginUser(repository);
    }


    @Test
    public void getTriggersNetworkLoginAndEmissionOfFetchedToken() {
        TokenNwModel tokenNwModel = ModelGenerationUtil.INSTANCE.createTokenNwModel();
        new ArrangeBuilder().withTokenFromServiceAfterLogin(tokenNwModel);

        LoginParams loginParams = new LoginParams("username", "password");
        TestObserver<TokenEntity> getObserver = interactor.getSingle(loginParams).test();

        getObserver.assertValueAt(0, tokenEntity -> tokenEntity.getToken().equals(tokenNwModel.getToken()));
        getObserver.assertValueCount(1);
        getObserver.assertComplete();
    }

    @Test
    public void getStreamEmitsErrorWhenNetworkServiceErrors() {
        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInLoginFromService(throwable);

        LoginParams loginParams = new LoginParams("username", "password");
        TestObserver<TokenEntity> getObserver = interactor.getSingle(loginParams).test();

        getObserver.assertError(throwable);
    }
    */


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

    private class ArrangeBuilder {

        private ArrangeBuilder withTokenFromServiceAfterLogin(TokenNwModel tokenNwModel) {
            Mockito.when(service.login(Mockito.anyString(), Mockito.anyString())).thenReturn(Single.just(tokenNwModel));
            return this;
        }

        private ArrangeBuilder withErrorInLoginFromService(Throwable error) {
            Mockito.when(service.login(Mockito.anyString(), Mockito.anyString())).thenReturn(Single.error(error));
            return this;
        }


    }
}
