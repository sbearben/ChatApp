package uk.co.victoriajanedavis.chatapp.data.repositories;
;
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
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import uk.co.victoriajanedavis.chatapp.data.mappers.TokenNwSpMapper;
import uk.co.victoriajanedavis.chatapp.data.model.network.TokenNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.TokenCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.TokenReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntity;
import uk.co.victoriajanedavis.chatapp.common.BaseTest;
import uk.co.victoriajanedavis.chatapp.common.ModelGenerationUtil;

public class TokenRepositoryTest extends BaseTest {

    private TokenReactiveStore tokenStore;
    private TokenRepository repository;

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
    }


    @Test
    public void emptyTokenIsEmittedWhenCacheIsEmpty() {
        TestObserver<TokenEntity> requestObserver = repository.requestTokenSingle().test();

        requestObserver.assertValue(TokenEntity::isEmpty);
        requestObserver.assertComplete();
    }

    @Test
    public void fetchedTokenAfterLoginIsStored() {
        TokenNwModel tokenNwModel = ModelGenerationUtil.INSTANCE.createTokenNwModel();
        new ArrangeBuilder().withTokenFromServiceAfterLogin(tokenNwModel);

        TestObserver fetchObserver = repository.fetchTokenViaLoginAndStore("username", "password").test();
        TestObserver<TokenEntity> requestObserver = repository.requestTokenSingle().test();

        fetchObserver.assertComplete();

        requestObserver.assertValueAt(0, tokenEntity -> tokenEntity.getToken().equals(tokenNwModel.getToken()));
        requestObserver.assertValueCount(1);
        requestObserver.assertComplete();
    }

    @Test
    public void fetchedTokenAfterRegisterIsStored() {
        TokenNwModel tokenNwModel = ModelGenerationUtil.INSTANCE.createTokenNwModel();
        new ArrangeBuilder().withTokenFromServiceAfterRegister(tokenNwModel);

        TestObserver fetchObserver = repository.fetchTokenViaRegisterAndStore("username", "email", "password1", "password2").test();
        TestObserver<TokenEntity> requestObserver = repository.requestTokenSingle().test();

        fetchObserver.assertComplete();

        requestObserver.assertValueAt(0, tokenEntity -> tokenEntity.getToken().equals(tokenNwModel.getToken()));
        requestObserver.assertValueCount(1);
        requestObserver.assertComplete();
    }

    @Test
    public void storageIsClearedOnNetworkLogout() {
        TokenNwModel tokenNwModel = ModelGenerationUtil.INSTANCE.createTokenNwModel();
        new ArrangeBuilder().withLogoutFromServiceCompletes();

        tokenStore.storeSingular(new TokenNwSpMapper().mapFrom(tokenNwModel)).subscribe();

        TestObserver<TokenEntity> requestObserver1 = repository.requestTokenSingle().test();
        TestObserver logoutObserver = repository.deleteTokenViaLogout().test();
        TestObserver<TokenEntity> requestObserver2 = repository.requestTokenSingle().test();

        requestObserver1.assertValueAt(0, tokenEntity -> tokenEntity.getToken().equals(tokenNwModel.getToken()));
        requestObserver1.assertValueCount(1);
        requestObserver1.assertComplete();

        logoutObserver.assertComplete();

        requestObserver2.assertValueAt(0, TokenEntity::isEmpty);
        requestObserver2.assertValueCount(1);
        requestObserver2.assertComplete();
    }

    @Test
    public void tokenStreamIsUpdatedOnNetworkLogout() {
        TokenNwModel tokenNwModel = ModelGenerationUtil.INSTANCE.createTokenNwModel();
        new ArrangeBuilder().withLogoutFromServiceCompletes();

        tokenStore.storeSingular(new TokenNwSpMapper().mapFrom(tokenNwModel)).subscribe();

        TestObserver<TokenEntity> getObserver = repository.getTokenStream().test();
        TestObserver logoutObserver = repository.deleteTokenViaLogout().test();

        getObserver.assertValueAt(0, tokenEntity -> tokenEntity.getToken().equals(tokenNwModel.getToken()));
        getObserver.assertValueAt(1, TokenEntity::isEmpty);
        getObserver.assertValueCount(2);
        getObserver.assertNotComplete();

        logoutObserver.assertComplete();
    }


    private class ArrangeBuilder {

        private ArrangeBuilder withTokenFromServiceAfterLogin(TokenNwModel tokenNwModel) {
            Mockito.when(service.login(Mockito.anyString(), Mockito.anyString())).thenReturn(Single.just(tokenNwModel));
            return this;
        }

        private ArrangeBuilder withTokenFromServiceAfterRegister(TokenNwModel tokenNwModel) {
            Mockito.when(service.register(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(Single.just(tokenNwModel));
            return this;
        }

        private ArrangeBuilder withLogoutFromServiceCompletes() {
            Mockito.when(service.logout()).thenReturn(Completable.complete());
            return this;
        }
    }
    */
}
