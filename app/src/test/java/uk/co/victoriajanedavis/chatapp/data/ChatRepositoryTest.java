package uk.co.victoriajanedavis.chatapp.data;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.ChatMembershipNwModel;
import uk.co.victoriajanedavis.chatapp.data.model.network.UserNwModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.repositories.ChatRepository;
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService;
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity;
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipEntity;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ChatRepositoryTest extends BaseTest {

    @Mock
    private BaseReactiveStore<ChatEntity, ChatDbModel> chatStore;

    @Mock
    private BaseReactiveStore<FriendshipEntity, FriendshipDbModel> friendStore;

    @Mock
    private ChatAppService service;


    private ChatRepository repository;

    @Before
    public void setUp() {
        repository = new ChatRepository(chatStore, friendStore, service);
    }

    @Test
    public void getAllChatMembershipsReturnsStoreObservable() {
        List<ChatEntity> list = createChatMembershipEntityList();
        new ArrangeBuilder().withListFromChatMembershipsStore(list)
                            .withFriendshipEntityFromFriendStore(Mockito.mock(FriendshipEntity.class));

        TestObserver<List<ChatEntity>> to = repository.getAllChatMemberships().test();
        to.assertValueAt(0, testObjectList -> testObjectList.equals(list));
        to.assertValueAt(0, testObjectList -> testObjectList.size() == 3);

        Mockito.verify(chatStore).getAll(null);
    }

    @Test
    public void friendStoreReturnsEachChatMembershipsAssociatedFriendship() {
        List<ChatEntity> list = createChatMembershipEntityList();
        new ArrangeBuilder().withListFromChatMembershipsStore(list)
                .withFriendshipEntityFromFriendStore(Mockito.mock(FriendshipEntity.class));

        repository.getAllChatMemberships().subscribe();

        Mockito.verify(friendStore).getSingular(list.get(0).getUuid());
        Mockito.verify(friendStore).getSingular(list.get(1).getUuid());
        Mockito.verify(friendStore).getSingular(list.get(2).getUuid());
    }

    @Test
    public void fetchChatMembershipsEmitsErrorWhenNetworkServiceErrors() {
        Throwable throwable = Mockito.mock(Throwable.class);
        new ArrangeBuilder().withErrorInChatMembershipsFromService(throwable);

        repository.fetchChatMemberships().test().assertError(throwable);
    }

    /*
    @Test
    public void creditDraftRawItemsFromServiceAreMapped() throws Exception {
        List<ChatMembershipNwModel> networkList = createChatMembershipNwList();
        new ArrangeBuilder().withChatMembershipsFromService(networkList)
                            .withCompletableFromChatStoreReplaceAll();

        repository.fetchChatMemberships().subscribe();
        //TestObserver to = repository.fetchChatMemberships().test();
        //to.assertComplete();

        Mockito.verify(chatStore).replaceAll(networkList.get(0));
        Mockito.verify(friendStore).replaceAll(networkList.get(0));
    }
    */

    @Test
    public void creditDraftsAreStoredInStoreViaReplaceAll() throws Exception {
        ChatEntity entity = Mockito.mock(ChatEntity.class);
        new ArrangeBuilder().withChatMembershipsFromService(
                Collections.singletonList(Mockito.mock(ChatMembershipNwModel.class)));

        repository.fetchChatMemberships().subscribe();

        Mockito.verify(chatStore).replaceAll(Collections.singletonList(entity));
    }

    /*
    private static List<ChatMembershipNwModel> createChatMembershipNwList() {
        return new ArrayList<ChatMembershipNwModel>() {{
            add(Mockito.mock(ChatMembershipNwModel.class));
            add(Mockito.mock(ChatMembershipNwModel.class));
            add(Mockito.mock(ChatMembershipNwModel.class));
        }};
    }
    */

    private static List<ChatMembershipNwModel> createChatMembershipNwList() {
        return new ArrayList<ChatMembershipNwModel>() {{
            add(new ChatMembershipNwModel(UUID.randomUUID(), createUserNwModel()));
            add(new ChatMembershipNwModel(UUID.randomUUID(), createUserNwModel()));
            add(new ChatMembershipNwModel(UUID.randomUUID(), createUserNwModel()));
        }};
    }

    private static UserNwModel createUserNwModel() {
        return new UserNwModel(UUID.randomUUID());
    }

    private static List<ChatEntity> createChatMembershipEntityList() {
        return new ArrayList<ChatEntity>() {{
            add(new ChatEntity(UUID.randomUUID()));
            add(new ChatEntity(UUID.randomUUID()));
            add(new ChatEntity(UUID.randomUUID()));
        }};
    }

    private class ArrangeBuilder {

        private ArrangeBuilder withListFromChatMembershipsStore(List<ChatEntity> chatEntities) {
            Mockito.when(chatStore.getAll(null)).thenReturn(Observable.just(chatEntities));
            return this;
        }

        private ArrangeBuilder withChatMembershipsFromService(List<ChatMembershipNwModel> nwModels) {
            Mockito.when(service.getChatMemberships()).thenReturn(Single.just(nwModels));
            return this;
        }

        private ArrangeBuilder withErrorInChatMembershipsFromService(Throwable error) {
            Mockito.when(service.getChatMemberships()).thenReturn(Single.error(error));
            return this;
        }

        private ArrangeBuilder withFriendshipEntityFromFriendStore(FriendshipEntity friendEntity) {
            Mockito.when(friendStore.getSingular(Mockito.any(UUID.class)))
                    .thenReturn(Observable.just(friendEntity));
            return this;
        }

        private ArrangeBuilder withCompletableFromChatStoreReplaceAll() {
            Mockito.when(chatStore.replaceAll(Mockito.any())).thenReturn(Completable.complete());
            return this;
        }
    }
}
