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
import uk.co.victoriajanedavis.chatapp.data.model.network.MessageNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.MessageRepository;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.ChatMembershipCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.MessageCache;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatMembershipEntity;
import uk.co.victoriajanedavis.chatapp.domain.entities.MessageEntity;
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage;
import uk.co.victoriajanedavis.chatapp.domain.interactors.SendChatMessage.MessageParams;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;


@RunWith(AndroidJUnit4.class)
public class SendChatMessageTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<ChatMembershipDbModel> chatStore;
    private MessageReactiveStore messageStore;

    private MessageRepository repository;
    private SendChatMessage interactor;

    @Mock
    private ChatAppService service;


    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                ChatAppDatabase.class)
                .allowMainThreadQueries()
                .build();

        Cache.DiskCache<UUID, ChatMembershipDbModel> chatCache = new ChatMembershipCache(database);
        chatStore = new BaseReactiveStore<>(chatCache);

        MessageCache messageCache = new MessageCache(database);
        messageStore = new MessageReactiveStore(messageCache);

        repository = new MessageRepository(messageStore, service);

        interactor = new SendChatMessage(repository);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Test
    public void sendingMessageReturnsMessageFromNetworkAndEmitsToGetAllStream() {
        ChatMembershipDbModel chatDbModel = ModelGenerationUtil.createChatMembershipDbModel();
        MessageNwModel messageNwModel = ModelGenerationUtil.createMessageNwModel(chatDbModel.getUuid());
        messageNwModel.setText("hello");

        new ArrangeBuilder().withNewlySentMessageFromService(messageNwModel);

        chatStore.storeSingular(chatDbModel).subscribe();

        TestObserver<List<MessageEntity>> getAllObserver = repository.getAllMessagesInChat(chatDbModel.getUuid()).test();
        TestObserver<MessageEntity> sendObserver = interactor.getSingle(new MessageParams(messageNwModel.getChatUuid(), messageNwModel.getText())).test();

        getAllObserver.assertValueAt(0, List::isEmpty);
        getAllObserver.assertValueAt(1, list -> list.size() == 1);
        getAllObserver.assertValueAt(1, list -> list.get(0).getChatUuid().equals(messageNwModel.getChatUuid()));
        getAllObserver.assertValueAt(1, list -> list.get(0).getText().equals("hello"));
        getAllObserver.assertValueCount(2);

        sendObserver.assertValueAt(0, messageEntity -> messageEntity.getChatUuid().equals(messageNwModel.getChatUuid()));
        sendObserver.assertValueCount(1);
        sendObserver.assertComplete();
    }

    @Test
    public void sendMessageSingleEmitsErrorWhenNetworkServiceErrors() {
        ChatMembershipEntity chatEntity = new ChatMembershipEntity(UUID.randomUUID());
        MessageNwModel messageNwModel = ModelGenerationUtil.createMessageNwModel(chatEntity.getUuid());
        messageNwModel.setText("hello");

        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInSendChatMessageFromService(throwable);

        TestObserver<MessageEntity> sendObserver = interactor.getSingle(new MessageParams(messageNwModel.getChatUuid(), messageNwModel.getText())).test();

        sendObserver.assertError(throwable);
    }


    /****************************************************/
    /****************** Helper methods ******************/
    /****************************************************/

    private class ArrangeBuilder {

        private ArrangeBuilder withNewlySentMessageFromService(MessageNwModel messageNwModel) {
            Mockito.when(service.postMessageToChat(messageNwModel.getChatUuid().toString(), messageNwModel.getText()))
                    .thenReturn(Single.just(messageNwModel));
            return this;
        }

        private ArrangeBuilder withErrorInSendChatMessageFromService(Throwable error) {
            Mockito.when(service.postMessageToChat(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(Single.error(error));
            return this;
        }

    }
}
