package uk.co.victoriajanedavis.chatapp.data.repositories;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import uk.co.victoriajanedavis.chatapp.data.mappers.UserNwFriendshipDbMapper;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.UserNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.SentFriendRequestCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;

@RunWith(AndroidJUnit4.class)
public class SentFriendRequestRepositoryTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<FriendshipDbModel> friendshipStore;
    private SentFriendRequestRepository repository;

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

        Cache.DiskCache<UUID, FriendshipDbModel> receivedRequestsCache = new SentFriendRequestCache(database);
        friendshipStore = new BaseReactiveStore<>(receivedRequestsCache);

        repository = new SentFriendRequestRepository(friendshipStore, service);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void emptyListIsEmittedWhenCacheIsEmpty() {
        repository.getAllSentFriendRequests().test().assertValue(List::isEmpty);
        repository.getAllSentFriendRequests().test().assertNotComplete();
    }

    @Test
    public void lastStoredObjectIsEmittedAfterSubscription() {
        List<FriendshipDbModel> dbModels = createAndStoreSentFriendshipDbModels(1);

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllSentFriendRequests().test();

        getObserver.assertValueAt(0, list -> list.size() == 1);
        getObserver.assertValueAt(0, list -> list.get(0).getUuid().equals(dbModels.get(0).getUuid()));
        getObserver.assertValueCount(1);
    }

    @Test
    public void fetchedListIsEmittedAfterSubscription() {
        new ArrangeBuilder().withSentFriendRequestsFromService(ModelGenerationUtil.createUserNwList(3));

        TestObserver fetchObserver = repository.fetchSentFriendRequests().test();
        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllSentFriendRequests().test();

        fetchObserver.assertComplete();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        getObserver.assertValueCount(1);
    }

    @Test
    public void emptyListAndFetchedListIsEmittedWhenSubscribedBeforeFetch() {
        new ArrangeBuilder().withSentFriendRequestsFromService(ModelGenerationUtil.createUserNwList(3));

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllSentFriendRequests().test();
        TestObserver fetchObserver = repository.fetchSentFriendRequests().test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueAt(1, list -> list.size() == 3);
        getObserver.assertValueCount(2);

        fetchObserver.assertComplete();
    }

    @Test
    public void emptyListReturnedFromNetworkReplacesCurrentlyStoredItems() {
        new ArrangeBuilder().withSentFriendRequestsFromService(new ArrayList<>());
        createAndStoreSentFriendshipDbModels(3);

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllSentFriendRequests().test();
        TestObserver fetchObserver = repository.fetchSentFriendRequests().test();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        getObserver.assertValueAt(1, List::isEmpty);
        getObserver.assertValueCount(2);

        fetchObserver.assertComplete();
    }

    @Test
    public void newlySentFriendRequestIsReceivedFromNetworkAndStoredThenEmitted() {
        UserNwModel nwModel = ModelGenerationUtil.createUserNwModel("rickardo");
        new ArrangeBuilder().withSendNewFriendRequestFromService(nwModel);

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllSentFriendRequests().test();
        TestObserver<FriendshipEntity> acceptObserver = repository.sendFriendRequest(nwModel.getUsername()).test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueAt(1, list -> list.size() == 1);
        getObserver.assertValueCount(2);

        acceptObserver.assertValueAt(0, entity -> entity.getUuid().equals(nwModel.getUuid()));
        acceptObserver.assertComplete();
    }

    @Test
    public void canceledSentFriendRequestIsReceivedFromNetworkAndDeleted() {
        UserNwModel nwModel = ModelGenerationUtil.createUserNwModel("rickardo");
        new ArrangeBuilder().withCanceledSentFriendRequestFromService(nwModel);

        friendshipStore.storeSingular(userNwModelToSentFriendshipDbModel(nwModel)).subscribe();

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllSentFriendRequests().test();
        TestObserver<FriendshipEntity> canceledObserver = repository.cancelSentFriendRequest(nwModel.getUsername()).test();

        getObserver.assertValueAt(0, list -> list.size() == 1);
        getObserver.assertValueAt(1, List::isEmpty);
        getObserver.assertValueCount(2);

        canceledObserver.assertValueAt(0, entity -> entity.getUuid().equals(nwModel.getUuid()));
        canceledObserver.assertComplete();
    }

    private List<FriendshipDbModel> createAndStoreSentFriendshipDbModels(int number) {
        List<FriendshipDbModel> friendshipDbModels = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            FriendshipDbModel friendDbModel = new FriendshipDbModel();
            friendDbModel.setUuid(UUID.randomUUID());

            friendDbModel.setFriendshipAccepted(false);
            friendDbModel.setSentFromCurrentUser(true);

            friendshipDbModels.add(friendDbModel);
        }

        friendshipStore.storeAll(friendshipDbModels).subscribe();
        return friendshipDbModels;
    }

    private FriendshipDbModel userNwModelToSentFriendshipDbModel(UserNwModel nwModel) {
        UserNwFriendshipDbMapper mapper = new UserNwFriendshipDbMapper(false, true);
        return mapper.mapFrom(nwModel);
    }


    private class ArrangeBuilder {

        private ArrangeBuilder withSentFriendRequestsFromService(List<UserNwModel> nwModels) {
            Mockito.when(service.getSentFriendRequests()).thenReturn(Single.just(nwModels));
            return this;
        }

        private ArrangeBuilder withSendNewFriendRequestFromService(UserNwModel nwModel) {
            Mockito.when(service.sendFriendRequest(nwModel.getUsername())).thenReturn(Single.just(nwModel));
            return this;
        }

        private ArrangeBuilder withCanceledSentFriendRequestFromService(UserNwModel nwModel) {
            Mockito.when(service.cancelSentFriendRequest(nwModel.getUsername())).thenReturn(Single.just(nwModel));
            return this;
        }

    }
}
