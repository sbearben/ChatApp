package uk.co.victoriajanedavis.chatapp.data.repositories;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatMembershipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.MessageNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.MessageRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.ChatMembershipCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.MessageCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;

@RunWith(AndroidJUnit4.class)
public class MessageRepositoryTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<ChatMembershipDbModel> chatStore;

    private MessageReactiveStore messageStore;
    private MessageRepository repository;

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

        MessageCache messagesCache = new MessageCache(database);
        messageStore = new MessageReactiveStore(messagesCache);

        repository = new MessageRepository(messageStore, service);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void emptyListIsEmittedWhenCacheIsEmpty() {
        repository.getAllMessagesInChat(UUID.randomUUID()).test().assertValue(List::isEmpty);
        repository.getAllMessagesInChat(UUID.randomUUID()).test().assertNotComplete();
    }

    @Test
    public void lastStoredObjectIsEmittedAfterSubscription() {
        ChatMembershipDbModel chatDbModel = ModelGenerationUtil.createChatMembershipDbModel();
        List<MessageDbModel> messageDbModels = createAndStoreMessageDbModels(chatDbModel,3);

        TestObserver<List<MessageEntity>> getObserver = repository.getAllMessagesInChat(chatDbModel.getUuid()).test();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        //getObserver.assertValueAt(0, list -> list.get(0).equals(entities.get(0)));
        getObserver.assertValueCount(1);
    }

    @Test
    public void fetchedListIsEmittedAfterSubscription() {
        ChatMembershipDbModel chatDbModel = ModelGenerationUtil.createChatMembershipDbModel();
        chatStore.storeSingular(chatDbModel).subscribe();
        new ArrangeBuilder().withMessagesFromService(chatDbModel.getUuid(), ModelGenerationUtil.createMessageNwList(chatDbModel.getUuid(), 3));

        TestObserver fetchObserver = repository.fetchInitialMessagesInChat(chatDbModel.getUuid()).test();
        TestObserver<List<MessageEntity>> getObserver = repository.getAllMessagesInChat(chatDbModel.getUuid()).test();

        fetchObserver.assertComplete();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        getObserver.assertValueCount(1);
    }

    @Test
    public void emptyListAndFetchedListIsEmittedWhenSubscribedBeforeFetch() {
        ChatMembershipDbModel chatDbModel = ModelGenerationUtil.createChatMembershipDbModel();
        chatStore.storeSingular(chatDbModel).subscribe();
        new ArrangeBuilder().withMessagesFromService(chatDbModel.getUuid(), ModelGenerationUtil.createMessageNwList(chatDbModel.getUuid(), 3));

        TestObserver<List<MessageEntity>> getObserver = repository.getAllMessagesInChat(chatDbModel.getUuid()).test();
        TestObserver fetchObserver = repository.fetchInitialMessagesInChat(chatDbModel.getUuid()).test();

        getObserver.assertValueAt(0, List::isEmpty);
        getObserver.assertValueAt(1, list -> list.size() == 3);
        getObserver.assertValueCount(2);

        fetchObserver.assertComplete();
    }

    @Test
    public void emptyListReturnedFromNetworkReplacesCurrentlyStoredItems() {
        ChatMembershipDbModel chatDbModel = ModelGenerationUtil.createChatMembershipDbModel();
        chatStore.storeSingular(chatDbModel).subscribe();

        new ArrangeBuilder().withMessagesFromService(chatDbModel.getUuid(), new ArrayList<>());
        createAndStoreMessageDbModels(chatDbModel,3);

        TestObserver<List<MessageEntity>> getObserver = repository.getAllMessagesInChat(chatDbModel.getUuid()).test();
        TestObserver fetchObserver = repository.fetchInitialMessagesInChat(chatDbModel.getUuid()).test();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        getObserver.assertValueAt(1, List::isEmpty);
        getObserver.assertValueCount(2);

        fetchObserver.assertComplete();
    }

    @Test
    public void fetchMoreItemsAreStoredAndAppendedToStream() {
        ChatMembershipDbModel chatDbModel = ModelGenerationUtil.createChatMembershipDbModel();

        new ArrangeBuilder().withMoreMessagesFromService(ModelGenerationUtil.createMessageNwList(chatDbModel.getUuid(), 3));
        createAndStoreMessageDbModels(chatDbModel,3);

        TestObserver<List<MessageEntity>> getObserver = repository.getAllMessagesInChat(chatDbModel.getUuid()).test();
        TestObserver fetchObserver = repository.fetchMoreMessagesInChatOlderThanOldestInDb(chatDbModel.getUuid()).test();

        getObserver.assertValueAt(0, list -> list.size() == 3);
        getObserver.assertValueAt(1, list -> list.size() == 6);
        getObserver.assertValueCount(2);

        fetchObserver.assertComplete();
    }


    private List<MessageDbModel> createAndStoreMessageDbModels(ChatMembershipDbModel chatDbModel, int number) {
        chatStore.storeSingular(chatDbModel).subscribe();

        List<MessageDbModel> messageDbModels = new ArrayList<>(number);
        for (int i=0; i<number; i++) {
            MessageDbModel messageDbModel = new MessageDbModel();
            messageDbModel.setUuid(UUID.randomUUID());

            messageDbModel.setChatUuid(chatDbModel.getUuid());
            messageDbModel.setCreated(new Date());

            messageDbModels.add(messageDbModel);
        }

        messageStore.storeAll(messageDbModels).subscribe();
        return messageDbModels;
    }

    private class ArrangeBuilder {

        private ArrangeBuilder withMessagesFromService(UUID chatUuid, List<MessageNwModel> nwModels) {
            Mockito.when(service.getNewestChatMessages(chatUuid.toString(), 30)).thenReturn(Single.just(nwModels));
            return this;
        }

        private ArrangeBuilder withMoreMessagesFromService(List<MessageNwModel> nwModels) {
            Mockito.when(service.getChatMessagesOlderThanGivenDate(Mockito.anyString(), Mockito.any(Date.class), Mockito.anyInt())).thenReturn(Single.just(nwModels));
            return this;
        }

    }
}
