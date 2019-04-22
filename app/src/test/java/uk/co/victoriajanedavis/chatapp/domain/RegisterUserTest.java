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
import uk.co.victoriajanedavis.chatapp.domain.interactors.RegisterUser.RegisterParams;
import uk.co.victoriajanedavis.chatapp.domain.interactors.RegisterUser;
import uk.co.victoriajanedavis.chatapp.common.BaseTest;
import uk.co.victoriajanedavis.chatapp.common.ModelGenerationUtil;

public class RegisterUserTest extends BaseTest {

    private TokenReactiveStore tokenStore;
    private TokenRepository repository;
    private RegisterUser interactor;

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

        interactor = new RegisterUser(repository);
    }


    @Test
    public void getTriggersNetworkRegisterAndEmissionOfFetchedToken() {
        TokenNwModel tokenNwModel = ModelGenerationUtil.INSTANCE.createTokenNwModel();
        new ArrangeBuilder().withTokenFromServiceAfterRegister(tokenNwModel);

        RegisterParams registerParams = new RegisterParams("username", "email@email.com", "password1", "password2");
        TestObserver<TokenEntity> getObserver = interactor.getSingle(registerParams).test();

        getObserver.assertValueAt(0, tokenEntity -> tokenEntity.getToken().equals(tokenNwModel.getToken()));
        getObserver.assertValueCount(1);
        getObserver.assertComplete();
    }

    @Test
    public void getStreamEmitsErrorWhenNetworkServiceErrors() {
        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInRegisterFromService(throwable);

        RegisterParams registerParams = new RegisterParams("username", "email@email.com", "password1", "password2");
        TestObserver<TokenEntity> getObserver = interactor.getSingle(registerParams).test();

        getObserver.assertError(throwable);
    }
    */


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

    private class ArrangeBuilder {

        private ArrangeBuilder withTokenFromServiceAfterRegister(TokenNwModel tokenNwModel) {
            Mockito.when(service.register(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(Single.just(tokenNwModel));
            return this;
        }

        private ArrangeBuilder withErrorInRegisterFromService(Throwable error) {
            Mockito.when(service.register(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(Single.error(error));
            return this;
        }


    }
}
