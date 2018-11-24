package uk.co.victoriajanedavis.chatapp.domain;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.UserNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.SentFriendRequestCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendFriendRequest;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;

@RunWith(AndroidJUnit4.class)
public class SendFriendRequestTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<FriendshipDbModel> friendshipStore;
    private SentFriendRequestRepository repository;
    private SendFriendRequest interactor;

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

        interactor = new SendFriendRequest(repository);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void sendingFriendRequestReturnsUserFromNetworkAndEmitsToGetAllStream() {
        UserNwModel userNwModel = ModelGenerationUtil.createUserNwModel("rickardo");
        new ArrangeBuilder().withNewlySentFriendRequestFromService(userNwModel);

        TestObserver<List<FriendshipEntity>> getAllObserver = repository.getAllSentFriendRequests().test();
        TestObserver<FriendshipEntity> sendObserver = interactor.getSingle("rickardo").test();

        getAllObserver.assertValueAt(0, List::isEmpty);
        getAllObserver.assertValueAt(1, list -> list.size() == 1);
        getAllObserver.assertValueAt(1, list -> list.get(0).getUuid().equals(userNwModel.getUuid()));
        getAllObserver.assertValueCount(2);
        getAllObserver.assertNotComplete();

        sendObserver.assertValueAt(0, userEntity -> userEntity.getUuid().equals(userNwModel.getUuid()));
        sendObserver.assertValueCount(1);
        sendObserver.assertComplete();
    }

    @Test
    public void sendFriendRequestSingleEmitsErrorWhenNetworkServiceErrors() {
        UserNwModel userNwModel = ModelGenerationUtil.createUserNwModel("rickardo");
        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInSendFriendRequestFromService(throwable);

        TestObserver<FriendshipEntity> sendObserver = interactor.getSingle(userNwModel.getUsername()).test();

        sendObserver.assertError(throwable);
    }


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

    private class ArrangeBuilder {

        private ArrangeBuilder withNewlySentFriendRequestFromService(UserNwModel userNwModel) {
            Mockito.when(service.sendFriendRequest(userNwModel.getUsername())).thenReturn(Single.just(userNwModel));
            return this;
        }

        private ArrangeBuilder withErrorInSendFriendRequestFromService(Throwable error) {
            Mockito.when(service.sendFriendRequest(Mockito.anyString())).thenReturn(Single.error(error));
            return this;
        }

    }
}
