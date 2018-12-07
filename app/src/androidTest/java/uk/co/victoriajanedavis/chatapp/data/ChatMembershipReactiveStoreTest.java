package uk.co.victoriajanedavis.chatapp.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.observers.TestObserver;
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel;
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore;
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase;
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.ChatMembershipCache;
import uk.co.victoriajanedavis.chatapp.domain.Cache;
import uk.co.victoriajanedavis.chatapp.test_common.BaseTest;
import uk.co.victoriajanedavis.chatapp.test_common.ModelGenerationUtil;

@RunWith(AndroidJUnit4.class)
public class ChatMembershipReactiveStoreTest extends BaseTest {

    private ChatAppDatabase database;
    private BaseReactiveStore<ChatDbModel> reactiveStore;


    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                ChatAppDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        Cache.DiskCache<UUID, ChatDbModel> cache = new ChatMembershipCache(database);

        reactiveStore = new BaseReactiveStore<>(cache);
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void nothingIsEmittedWhenCacheIsEmpty() {
        reactiveStore.getSingular(UUID.randomUUID()).test().assertEmpty();
        reactiveStore.getSingular(UUID.randomUUID()).test().assertNotComplete();
    }

    @Test
    public void emptyListIsEmittedWhenCacheIsEmpty() {
        reactiveStore.getAll(null).test().assertValue(List::isEmpty);
        reactiveStore.getAll(null).test().assertNotComplete();
    }

    @Test
    public void lastStoredObjectIsEmittedAfterSubscription() {
        ChatDbModel dbModel = ModelGenerationUtil.createChatMembershipDbModel();
        reactiveStore.storeSingular(dbModel).subscribe();

        reactiveStore.getSingular(dbModel.getUuid()).test().assertValue(dbModel);
        reactiveStore.getAll(null).test().assertValue(list -> list.size() == 1);
    }

    @Test
    public void singularStreamEmitsWhenSingleObjectIsStored() {
        ChatDbModel dbModel = ModelGenerationUtil.createChatMembershipDbModel();

        TestObserver<ChatDbModel> to = reactiveStore.getSingular(dbModel.getUuid()).test();
        reactiveStore.storeSingular(dbModel).subscribe();

        to.assertValueAt(0, testObject -> testObject.equals(dbModel));
    }

    @Test
    public void allStreamEmitsWhenSingleObjectIsStored() {
        List<ChatDbModel> list = ModelGenerationUtil.createChatMembershipDbModelList(3);
        reactiveStore.storeAll(list).subscribe();

        TestObserver<List<ChatDbModel>> to = reactiveStore.getAll(null).test();

        to.assertValueAt(0, testObjectList -> testObjectList.equals(list));
        to.assertValueAt(0, testObjectList -> testObjectList.size() == 3);

        ChatDbModel newDbModel = ModelGenerationUtil.createChatMembershipDbModel();
        list.add(newDbModel);
        reactiveStore.storeSingular(newDbModel).subscribe();

        to.assertValueAt(1, testObjectList -> testObjectList.equals(list));
        to.assertValueAt(1, testObjectList -> testObjectList.size() == 4);
    }

    @Test
    public void allStreamEmitsWhenObjectListIsStored() {
        List<ChatDbModel> list = ModelGenerationUtil.createChatMembershipDbModelList(3);

        TestObserver<List<ChatDbModel>> to = reactiveStore.getAll(null).test();
        reactiveStore.storeAll(list).subscribe();

        /* IMPORTANT: if we checked the index at 0 this test would fail because we are storing the
         * list after we subscribed the testObserver, meaning the list emitted at index 0 would be
         * the empty list since upon subscription an empty list is emitted when there is nothing stored
         */
        to.assertValueAt(1, testObjectList -> testObjectList.equals(list));
    }

    @Test
    public void singularStreamEmitsWhenObjectListIsReplaced() {
        List<ChatDbModel> list = ModelGenerationUtil.createChatMembershipDbModelList(3);
        ChatDbModel firstDbModel = list.get(0);

        reactiveStore.storeSingular(firstDbModel).subscribe();

        TestObserver<ChatDbModel> to = reactiveStore.getSingular(firstDbModel.getUuid()).test();
        reactiveStore.replaceAll(null, list).subscribe();

        /* Since when we replaceAll the new list includes the old singular entity that we stored,
         * thus the testObserver will emit a second time (index 1).
         */
        to.assertValueAt(1, testObject -> testObject.equals(firstDbModel));
    }

    @Test
    public void allStreamEmitsWhenObjectListIsReplaced() {
        List<ChatDbModel> list = ModelGenerationUtil.createChatMembershipDbModelList(3);
        reactiveStore.storeAll(list).subscribe();

        TestObserver<List<ChatDbModel>> to = reactiveStore.getAll(null).test();
        reactiveStore.replaceAll(null, list).subscribe();

        to.assertValueAt(0, testObjectList -> testObjectList.equals(list));
        //to.assertValueAt(1, testObjectList -> testObjectList.size() == 0); //empty is emitted
        to.assertValueAt(1, testObjectList -> testObjectList.equals(list));
    }

    @Test
    public void singularStreamEmitsOnceWhenSameItemInsertedTwice() {
        ChatDbModel dbModel = ModelGenerationUtil.createChatMembershipDbModel();
        List<ChatDbModel> list = new ArrayList<>();
        list.add(dbModel);

        reactiveStore.storeAll(list).subscribe();
        reactiveStore.storeSingular(dbModel).subscribe();

        TestObserver<ChatDbModel> to = reactiveStore.getSingular(dbModel.getUuid()).test();

        to.assertValueAt(0, testObject -> testObject.equals(dbModel));
        to.assertValueCount(1); //ensure only 1 emission
    }
}
