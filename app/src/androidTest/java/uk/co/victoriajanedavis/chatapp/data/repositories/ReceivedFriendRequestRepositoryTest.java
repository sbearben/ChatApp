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
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.ReceivedFriendRequestCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;

@RunWith(AndroidJUnit4.class)
public class ReceivedFriendRequestRepositoryTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<FriendshipDbModel> friendshipStore;
    private ReceivedFriendRequestRepository repository;

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

        Cache.DiskCache<UUID, FriendshipDbModel> receivedRequestsCache = new ReceivedFriendRequestCache(database);
        friendshipStore = new BaseReactiveStore<>(receivedRequestsCache);

        repository = new ReceivedFriendRequestRepository(friendshipStore, service);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void emptyListIsEmittedWhenCacheIsEmpty() {
        repository.getAllReceivedFriendRequests().test().assertValue(List::isEmpty);
        repository.getAllReceivedFriendRequests().test().assertNotComplete();
    }

    @Test
    public void lastStoredObjectIsEmittedAfterSubscription() {
        List<FriendshipDbModel> dbModels = createAndStoreReceivedRequestFriendshipDbModels(1);

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllReceivedFriendRequests().test();

        getObserver.assertValueAt(0, list -> list.size() == 1);
        getObserver.assertValueAt(0, list -> list.get(0).getUuid().equals(dbModels.get(0).getUuid()));
        getObserver.assertValueCount(1);
    }

    @Test
    public void fetchedListIsEmittedAfterSubscription() {
        new ArrangeBuilder().withReceivedFriendRequestsFromService(ModelGenerationUtil.createUserNwList(3));

        TestObserver fetchObserver = repository.fetchReceivedFriendRequests().test();
        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllReceivedFriendRequests().test();

        fetchObserver.assertComplete();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        getObserver.assertValueCount(1);
    }

    @Test
    public void emptyListAndFetchedListIsEmittedWhenSubscribedBeforeFetch() {
        new ArrangeBuilder().withReceivedFriendRequestsFromService(ModelGenerationUtil.createUserNwList(3));

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllReceivedFriendRequests().test();
        TestObserver fetchObserver = repository.fetchReceivedFriendRequests().test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueAt(1, list -> list.size() == 3);
        getObserver.assertValueCount(2);

        fetchObserver.assertComplete();
    }

    @Test
    public void emptyListReturnedFromNetworkReplacesCurrentlyStoredItems() {
        new ArrangeBuilder().withReceivedFriendRequestsFromService(new ArrayList<>());
        createAndStoreReceivedRequestFriendshipDbModels(3);

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllReceivedFriendRequests().test();
        TestObserver fetchObserver = repository.fetchReceivedFriendRequests().test();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        getObserver.assertValueAt(1, List::isEmpty);
        getObserver.assertValueCount(2);

        fetchObserver.assertComplete();
    }

    @Test
    public void acceptedFriendRequestIsReceivedFromNetworkAndStoredAsAcceptedFriend() {
        UserNwModel nwModel = ModelGenerationUtil.createUserNwModel("rickardo");
        new ArrangeBuilder().withAcceptedFriendRequestFromService(nwModel);

        friendshipStore.storeSingular(userNwModelToReceivedFriendshipDbModel(nwModel)).subscribe();

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllReceivedFriendRequests().test();
        TestObserver<FriendshipEntity> acceptObserver = repository.acceptReceivedFriendRequest(nwModel.getUsername()).test();

        getObserver.assertValueAt(0, list -> list.size() == 1);
        getObserver.assertValueAt(1, List::isEmpty);
        getObserver.assertValueCount(2);

        acceptObserver.assertValueAt(0, entity -> entity.getUuid().equals(nwModel.getUuid()));
        acceptObserver.assertComplete();
    }

    @Test
    public void rejectedFriendRequestIsReceivedFromNetworkAndDeleted() {
        UserNwModel nwModel = ModelGenerationUtil.createUserNwModel("rickardo");
        new ArrangeBuilder().withRejectedFriendRequestFromService(nwModel);

        friendshipStore.storeSingular(userNwModelToReceivedFriendshipDbModel(nwModel)).subscribe();

        TestObserver<List<FriendshipEntity>> getObserver = repository.getAllReceivedFriendRequests().test();
        TestObserver<FriendshipEntity> rejectObserver = repository.rejectReceivedFriendRequest(nwModel.getUsername()).test();

        getObserver.assertValueAt(0, list -> list.size() == 1);
        getObserver.assertValueAt(1, List::isEmpty);
        getObserver.assertValueCount(2);

        rejectObserver.assertValueAt(0, entity -> entity.getUuid().equals(nwModel.getUuid()));
        rejectObserver.assertComplete();
    }

    private List<FriendshipDbModel> createAndStoreReceivedRequestFriendshipDbModels(int number) {
        List<FriendshipDbModel> friendshipDbModels = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            FriendshipDbModel friendDbModel = new FriendshipDbModel();
            friendDbModel.setUuid(UUID.randomUUID());

            friendDbModel.setFriendshipAccepted(false);
            friendDbModel.setSentFromCurrentUser(false);

            friendshipDbModels.add(friendDbModel);
        }

        friendshipStore.storeAll(friendshipDbModels).subscribe();
        return friendshipDbModels;
    }

    private FriendshipDbModel userNwModelToReceivedFriendshipDbModel(UserNwModel nwModel) {
        UserNwFriendshipDbMapper mapper = new UserNwFriendshipDbMapper(false, false);
        return mapper.mapFrom(nwModel);
    }


    private class ArrangeBuilder {

        private ArrangeBuilder withReceivedFriendRequestsFromService(List<UserNwModel> nwModels) {
            Mockito.when(service.getReceivedFriendRequests()).thenReturn(Single.just(nwModels));
            return this;
        }

        private ArrangeBuilder withAcceptedFriendRequestFromService(UserNwModel nwModel) {
            Mockito.when(service.acceptFriendRequest(nwModel.getUsername())).thenReturn(Single.just(nwModel));
            return this;
        }

        private ArrangeBuilder withRejectedFriendRequestFromService(UserNwModel nwModel) {
            Mockito.when(service.rejectFriendRequest(nwModel.getUsername())).thenReturn(Single.just(nwModel));
            return this;
        }

    }
}
