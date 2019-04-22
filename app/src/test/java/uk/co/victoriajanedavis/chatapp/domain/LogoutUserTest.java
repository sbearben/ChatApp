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

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;
import uk.co.victoriajanedavis.chatapp.data.mappers.TokenNwSpMapper;
import uk.co.victoriajanedavis.chatapp.data.model.network.TokenNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.TokenRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.TokenCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.TokenReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.LogoutUser;
import uk.co.victoriajanedavis.chatapp.common.BaseTest;
import uk.co.victoriajanedavis.chatapp.common.ModelGenerationUtil;

public class LogoutUserTest extends BaseTest {

    private TokenReactiveStore tokenStore;
    private TokenRepository repository;
    private LogoutUser interactor;

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

        interactor = new LogoutUser(repository);
    }


    @Test
    public void getEmitsTokenFromStorageBeforeDeletionAndPerformsNetworkLogout() {
        TokenNwModel tokenNwModel = ModelGenerationUtil.INSTANCE.createTokenNwModel();
        new ArrangeBuilder().withCompleteFromServiceAfterLogout();

        tokenStore.storeSingular(new TokenNwSpMapper().mapFrom(tokenNwModel)).subscribe();

        TestObserver<TokenEntity> getObserver = interactor.getSingle(null).test();

        getObserver.assertValueAt(0, tokenEntity -> tokenEntity.getToken().equals(tokenNwModel.getToken()));
        getObserver.assertValueCount(1);
        getObserver.assertComplete();
    }

    @Test
    public void getStreamDoesntEmitErrorWhenNetworkLogoutErrors() {
        TokenNwModel tokenNwModel = ModelGenerationUtil.INSTANCE.createTokenNwModel();
        tokenStore.storeSingular(new TokenNwSpMapper().mapFrom(tokenNwModel)).subscribe();

        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInLogoutFromService(throwable);

        TestObserver<TokenEntity> getObserver = interactor.getSingle(null).test();

        getObserver.assertNoErrors();
        getObserver.assertValueAt(0, tokenEntity -> tokenEntity.getToken().equals(tokenNwModel.getToken()));
    }
    */


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

    private class ArrangeBuilder {

        private ArrangeBuilder withCompleteFromServiceAfterLogout() {
            Mockito.when(service.logout()).thenReturn(Completable.complete());
            return this;
        }

        private ArrangeBuilder withErrorInLogoutFromService(Throwable error) {
            Mockito.when(service.logout()).thenReturn(Completable.error(error));
            return this;
        }


    }
}
