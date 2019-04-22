package uk.co.victoriajanedavis.chatapp.domain;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import uk.co.victoriajanedavis.chatapp.data.mappers.UserNwFriendshipDbMapper;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.UserNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.SentFriendRequestCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.CancelSentFriendRequest;
import uk.co.victoriajanedavis.chatapp.common.BaseTest;
import uk.co.victoriajanedavis.chatapp.common.ModelGenerationUtil;

public class CancelSentFriendRequestTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<FriendshipDbModel> friendshipStore;
    private SentFriendRequestRepository repository;
    private CancelSentFriendRequest interactor;

    private UserNwFriendshipDbMapper nwDbMapper = new UserNwFriendshipDbMapper(false, true);

    @Mock
    private ChatAppService service;


    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                ChatAppDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        Cache.DiskCache<UUID, FriendshipDbModel> friendshipCache = new SentFriendRequestCache(database);
        friendshipStore = new BaseReactiveStore<>(friendshipCache);

        repository = new SentFriendRequestRepository(friendshipStore, service);

        interactor = new CancelSentFriendRequest(repository);
    }

    @After
    public void closeDb() {
        database.close();
    }

    /*

    @Test
    public void cancelingFriendRequestReturnsUserFromNetworkAndRemovesFromGetAllStream() {
        UserNwModel userNwModel = ModelGenerationUtil.INSTANCE.createUserNwModel("rickardo");
        new ArrangeBuilder().withNewlySentFriendRequestFromService(userNwModel);
        friendshipStore.storeSingular(nwDbMapper.mapFrom(userNwModel)).subscribe();

        TestObserver<List<FriendshipEntity>> getAllObserver = repository.getAllSentFriendRequests().test();
        TestObserver<FriendshipEntity> sendObserver = interactor.getSingle("rickardo").test();

        getAllObserver.assertValueAt(0, list -> list.size() == 1);
        getAllObserver.assertValueAt(0, list -> list.get(0).getUuid().equals(userNwModel.getUuid()));
        getAllObserver.assertValueAt(1, List::isEmpty);
        getAllObserver.assertValueCount(2);
        getAllObserver.assertNotComplete();

        sendObserver.assertValueAt(0, userEntity -> userEntity.getUuid().equals(userNwModel.getUuid()));
        sendObserver.assertValueCount(1);
        sendObserver.assertComplete();
    }

    @Test
    public void cancelFriendRequestSingleEmitsErrorWhenNetworkServiceErrors() {
        UserNwModel userNwModel = ModelGenerationUtil.INSTANCE.createUserNwModel("rickardo");
        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInSendFriendRequestFromService(throwable);

        TestObserver<FriendshipEntity> sendObserver = interactor.getSingle(userNwModel.getUsername()).test();

        sendObserver.assertError(throwable);
    }
    */


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

    private class ArrangeBuilder {

        private ArrangeBuilder withNewlySentFriendRequestFromService(UserNwModel userNwModel) {
            Mockito.when(service.cancelSentFriendRequest(userNwModel.getUsername())).thenReturn(Single.just(userNwModel));
            return this;
        }

        private ArrangeBuilder withErrorInSendFriendRequestFromService(Throwable error) {
            Mockito.when(service.cancelSentFriendRequest(Mockito.anyString())).thenReturn(Single.error(error));
            return this;
        }

    }
}
