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
import uk.co.victoriajanedavis.chatapp.data.repositories.ReceivedFriendRequestRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.ReceivedFriendRequestCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetReceivedFriendRequestsList;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;

@RunWith(AndroidJUnit4.class)
public class GetReceivedFriendRequestsListTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<FriendshipDbModel> friendshipStore;
    private ReceivedFriendRequestRepository repository;
    private GetReceivedFriendRequestsList interactor;

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

        Cache.DiskCache<UUID, FriendshipDbModel> friendshipCache = new ReceivedFriendRequestCache(database);
        friendshipStore = new BaseReactiveStore<>(friendshipCache);

        repository = new ReceivedFriendRequestRepository(friendshipStore, service);

        interactor = new GetReceivedFriendRequestsList(repository);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void emptyCacheTriggersNetworkFetchAndEmissionOfFetchedItems() {
        new ArrangeBuilder().withReceivedFriendRequestsFromService(ModelGenerationUtil.createUserNwList(3));

        TestObserver<List<FriendshipEntity>> getObserver = interactor.getBehaviorStream(null).test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueAt(1, list -> list.size() == 3);
        getObserver.assertValueCount(2);
        getObserver.assertNotComplete();
    }

    @Test
    public void emptyNetworkFetchEmitsEmptyList() {
        new ArrangeBuilder().withReceivedFriendRequestsFromService(ModelGenerationUtil.createUserNwList(0));

        TestObserver<List<FriendshipEntity>> getObserver = interactor.getBehaviorStream(null).test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueCount(1);
        getObserver.assertNotComplete();
    }

    @Test
    public void getStreamEmitsErrorWhenNetworkServiceErrors() {
        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInReceivedFriendRequestsFromService(throwable);

        TestObserver<List<FriendshipEntity>> getObserver = interactor.getBehaviorStream(null).test();

        getObserver.assertError(throwable);
        getObserver.assertNotComplete();
    }


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

    private class ArrangeBuilder {

        private ArrangeBuilder withReceivedFriendRequestsFromService(List<UserNwModel> nwModels) {
            Mockito.when(service.getReceivedFriendRequests()).thenReturn(Single.just(nwModels));
            return this;
        }

        private ArrangeBuilder withErrorInReceivedFriendRequestsFromService(Throwable error) {
            Mockito.when(service.getReceivedFriendRequests()).thenReturn(Single.error(error));
            return this;
        }

    }
}
