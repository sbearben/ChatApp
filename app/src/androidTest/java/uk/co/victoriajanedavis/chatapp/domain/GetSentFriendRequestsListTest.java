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
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatMembershipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.UserNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.SentFriendRequestRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.ChatMembershipCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.SentFriendRequestCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetSentFriendRequestsList;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;

@RunWith(AndroidJUnit4.class)
public class GetSentFriendRequestsListTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<ChatMembershipDbModel> chatStore;
    private BaseReactiveStore<FriendshipDbModel> friendshipStore;
    private SentFriendRequestRepository repository;
    private GetSentFriendRequestsList interactor;

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

        Cache.DiskCache<UUID, ChatMembershipDbModel> chatCache = new ChatMembershipCache(database);
        chatStore = new BaseReactiveStore<>(chatCache);

        Cache.DiskCache<UUID, FriendshipDbModel> friendshipCache = new SentFriendRequestCache(database);
        friendshipStore = new BaseReactiveStore<>(friendshipCache);

        repository = new SentFriendRequestRepository(friendshipStore, service);

        interactor = new GetSentFriendRequestsList(repository);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void emptyCacheTriggersNetworkFetchAndEmissionOfFetchedItems() {
        new ArrangeBuilder().withSentFriendRequestsFromService(ModelGenerationUtil.createUserNwList(3));

        TestObserver<List<FriendshipEntity>> getObserver = interactor.getBehaviorStream(null).test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueAt(1, list -> list.size() == 3);
        getObserver.assertValueCount(2);
        getObserver.assertNotComplete();
    }

    @Test
    public void emptyNetworkFetchEmitsEmptyList() {
        new ArrangeBuilder().withSentFriendRequestsFromService(ModelGenerationUtil.createUserNwList(0));

        TestObserver<List<FriendshipEntity>> getObserver = interactor.getBehaviorStream(null).test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueCount(1);
        getObserver.assertNotComplete();
    }

    @Test
    public void getStreamEmitsErrorWhenNetworkServiceErrors() {
        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInSentFriendRequestsFromService(throwable);

        TestObserver<List<FriendshipEntity>> getObserver = interactor.getBehaviorStream(null).test();

        getObserver.assertError(throwable);
        getObserver.assertNotComplete();
    }


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

    private class ArrangeBuilder {

        private ArrangeBuilder withSentFriendRequestsFromService(List<UserNwModel> nwModels) {
            Mockito.when(service.getSentFriendRequests()).thenReturn(Single.just(nwModels));
            return this;
        }

        private ArrangeBuilder withErrorInSentFriendRequestsFromService(Throwable error) {
            Mockito.when(service.getSentFriendRequests()).thenReturn(Single.error(error));
            return this;
        }

    }
}
