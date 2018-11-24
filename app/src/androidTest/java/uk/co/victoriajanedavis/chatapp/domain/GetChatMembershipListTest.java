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
import uk.co.victoriajanedavis.chatapp.data.model.network.ChatMembershipNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.ChatRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.ChatMembershipCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.FriendshipCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatMembershipEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.GetChatMembershipList;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;

@RunWith(AndroidJUnit4.class)
public class GetChatMembershipListTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<ChatMembershipDbModel> chatStore;
    private BaseReactiveStore<FriendshipDbModel> friendshipStore;
    private ChatRepository chatRepository;
    private GetChatMembershipList interactor;

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

        Cache.DiskCache<UUID, FriendshipDbModel> friendshipCache = new FriendshipCache(database);
        friendshipStore = new BaseReactiveStore<>(friendshipCache);

        chatRepository = new ChatRepository(chatStore, friendshipStore, service);

        interactor = new GetChatMembershipList(chatRepository);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void emptyCacheTriggersNetworkFetchAndEmissionOfFetchedItems() {
        new ArrangeBuilder().withChatMembershipsFromService(ModelGenerationUtil.createChatMembershipNwList(3));

        TestObserver<List<ChatMembershipEntity>> getObserver = interactor.getBehaviorStream(null).test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueAt(1, list -> list.size() == 3);
        getObserver.assertValueCount(2);
        getObserver.assertNotComplete();
    }

    @Test
    public void emptyNetworkFetchEmitsEmptyList() {
        new ArrangeBuilder().withChatMembershipsFromService(ModelGenerationUtil.createChatMembershipNwList(0));

        TestObserver<List<ChatMembershipEntity>> getObserver = interactor.getBehaviorStream(null).test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueCount(1);
        getObserver.assertNotComplete();
    }

    @Test
    public void getStreamEmitsErrorWhenNetworkServiceErrors() {
        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInChatMembershipsFromService(throwable);

        TestObserver<List<ChatMembershipEntity>> getObserver = interactor.getBehaviorStream(null).test();

        getObserver.assertError(throwable);
        getObserver.assertNotComplete();
    }


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

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
