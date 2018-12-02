package uk.co.victoriajanedavis.chatapp.data.repositories;


import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.mockito.Mock;
import org.mockito.Mockito;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.ChatMembershipNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.ChatMembershipCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.FriendshipCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.MessageCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;

@RunWith(AndroidJUnit4.class)
public class ChatRepositoryTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<ChatDbModel> chatStore;
    private BaseReactiveStore<FriendshipDbModel> friendshipStore;
    private ChatRepository chatRepository;

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

        Cache.DiskCache<UUID, ChatDbModel> chatCache = new ChatMembershipCache(database);
        chatStore = new BaseReactiveStore<>(chatCache);

        Cache.DiskCache<UUID, FriendshipDbModel> friendshipCache = new FriendshipCache(database);
        friendshipStore = new BaseReactiveStore<>(friendshipCache);

        MessageCache messageCache = new MessageCache(database);
        MessageReactiveStore messageStore = new MessageReactiveStore(messageCache);

        chatRepository = new ChatRepository(chatStore, friendshipStore, messageStore, service);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void emptyListIsEmittedWhenCacheIsEmpty() {
        chatRepository.getAllChatMemberships().test().assertValue(List::isEmpty);
        chatRepository.getAllChatMemberships().test().assertNotComplete();
    }

    @Test
    public void lastStoredObjectIsEmittedAfterSubscription() {
        List<ChatDbModel> dbModels = createAndStoreChatMembershipDbModelsWithFriendShips(1);

        TestObserver<List<ChatEntity>> getObserver = chatRepository.getAllChatMemberships().test();

        getObserver.assertValueAt(0, list -> list.size() == 1);
        getObserver.assertValueAt(0, list -> list.get(0).getUuid().equals(dbModels.get(0).getUuid()));
        getObserver.assertValueAt(0, list -> list.get(0).getFriendship().getChatUuid().equals(dbModels.get(0).getUuid()));
        getObserver.assertValueCount(1);
    }

    @Test
    public void fetchChatMembershipsEmitsErrorWhenNetworkServiceErrors() {
        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInChatMembershipsFromService(throwable);

        TestObserver fetchObserver = chatRepository.fetchChatMemberships().test();

        fetchObserver.assertError(throwable);
        fetchObserver.assertNotComplete();
    }

    @Test
    public void fetchedListIsEmittedAfterSubscription() {
        new ArrangeBuilder().withChatMembershipsFromService(ModelGenerationUtil.createChatMembershipNwList(3));

        TestObserver fetchObserver = chatRepository.fetchChatMemberships().test();
        TestObserver<List<ChatEntity>> getObserver = chatRepository.getAllChatMemberships().test();

        fetchObserver.assertComplete();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        getObserver.assertValueCount(1);
    }

    @Test
    public void emptyListAndFetchedListIsEmittedWhenSubscribedBeforeFetch() {
        new ArrangeBuilder().withChatMembershipsFromService(ModelGenerationUtil.createChatMembershipNwList(3));

        TestObserver<List<ChatEntity>> getObserver = chatRepository.getAllChatMemberships().test();
        TestObserver fetchObserver = chatRepository.fetchChatMemberships().test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueAt(1, list -> list.size() == 3);
        getObserver.assertValueCount(2);

        fetchObserver.assertComplete();
    }

    @Test
    public void emptyListReturnedFromNetworkIsNotFiltered() {
        new ArrangeBuilder().withChatMembershipsFromService(new ArrayList<>());
        createAndStoreChatMembershipDbModelsWithFriendShips(3);

        TestObserver<List<ChatEntity>> getObserver = chatRepository.getAllChatMemberships().test();
        TestObserver fetchObserver = chatRepository.fetchChatMemberships().test();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        getObserver.assertValueAt(1, List::isEmpty);
        getObserver.assertValueCount(2);

        fetchObserver.assertComplete();
    }


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

    private List<ChatDbModel> createAndStoreChatMembershipDbModelsWithFriendShips(int number) {
        List<ChatDbModel> chatDbModels = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            ChatDbModel dbModel = ModelGenerationUtil.createChatMembershipDbModelWithFriendshipDbModel();
            chatDbModels.add(dbModel);

            chatStore.storeSingular(dbModel).subscribe();
            friendshipStore.storeSingular(dbModel.getFriendship()).subscribe();
        }

        return chatDbModels;
    }

    private class ArrangeBuilder {

        private ArrangeBuilder withChatMembershipsFromService(List<ChatMembershipNwModel> nwModels) {
            Mockito.when(service.getChatMemberships()).thenReturn(Single.just(nwModels));
            return this;
        }

        private ArrangeBuilder withErrorInChatMembershipsFromService(Throwable error) {
            Mockito.when(service.getChatMemberships()).thenReturn(Single.error(error));
            return this;
        }
    }
}
