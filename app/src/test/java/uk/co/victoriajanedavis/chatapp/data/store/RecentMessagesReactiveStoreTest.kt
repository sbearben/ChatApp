package uk.co.victoriajanedavis.chatapp.data.store

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.util.ArrayList
import java.util.UUID

import io.reactivex.observers.TestObserver
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.RecentMessagesCache
import uk.co.victoriajanedavis.chatapp.common.BaseTest
import uk.co.victoriajanedavis.chatapp.common.ModelGenerationUtil
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.RecentMessagesStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore
import uk.co.victoriajanedavis.chatapp.injection.component.DaggerUnitTestApplicationComponent
import uk.co.victoriajanedavis.chatapp.injection.component.UnitTestApplicationComponent
import javax.inject.Inject
import javax.inject.Named

@RunWith(AndroidJUnit4::class)
class RecentMessagesReactiveStoreTest : BaseTest() {

    // See https://medium.com/@WindRider/correct-usage-of-dagger-2-named-annotation-in-kotlin-8ab17ced6928
    @field:[Inject Named(RecentMessagesStore)] lateinit var reactiveStore: ReactiveStore<UUID, MessageDbModel>


    @Before
    override fun setUp() {
        super.setUp()
        unitTestComponent.inject(this)
    }

    @Test
    fun nothingIsEmittedWhenCacheIsEmpty() {
        reactiveStore.getSingular(UUID.randomUUID()).test().assertEmpty()
        reactiveStore.getSingular(UUID.randomUUID()).test().assertNotComplete()
    }

    @Test
    fun emptyListIsEmittedWhenCacheIsEmpty() {
        reactiveStore.getAll(null).test().assertValue { list -> list.isEmpty() }
        reactiveStore.getAll(null).test().assertNotComplete()
    }

    @Test
    fun lastStoredObjectIsEmittedAfterSubscription() {
        val dbModel = ModelGenerationUtil.createMessageDbModel("Message")
        reactiveStore.storeSingular(dbModel).subscribe()

        reactiveStore.getSingular(dbModel.uuid).test().assertValue(dbModel)
        reactiveStore.getAll(null).test().assertValue { list -> list.size == 1 }
    }

    /*
    @Test
    fun singularStreamEmitsWhenSingleObjectIsStored() {
        val dbModel = ModelGenerationUtil.createChatMembershipDbModel()

        val to = reactiveStore.getSingular(dbModel.getUuid()).test()
        reactiveStore.storeSingular(dbModel).subscribe()

        to.assertValueAt(0) { testObject -> testObject == dbModel }
    }

    @Test
    fun allStreamEmitsWhenSingleObjectIsStored() {
        val list = ModelGenerationUtil.createChatMembershipDbModelList(3)
        reactiveStore.storeAll(list).subscribe()

        val to = reactiveStore.getAll(null).test()

        to.assertValueAt(0) { testObjectList -> testObjectList == list }
        to.assertValueAt(0) { testObjectList -> testObjectList.size == 3 }

        val newDbModel = ModelGenerationUtil.createChatMembershipDbModel()
        list.add(newDbModel)
        reactiveStore.storeSingular(newDbModel).subscribe()

        to.assertValueAt(1) { testObjectList -> testObjectList == list }
        to.assertValueAt(1) { testObjectList -> testObjectList.size == 4 }
    }

    @Test
    fun allStreamEmitsWhenObjectListIsStored() {
        val list = ModelGenerationUtil.createChatMembershipDbModelList(3)

        val to = reactiveStore.getAll(null).test()
        reactiveStore.storeAll(list).subscribe()

        /* IMPORTANT: if we checked the index at 0 this test would fail because we are storing the
         * list after we subscribed the testObserver, meaning the list emitted at index 0 would be
         * the empty list since upon subscription an empty list is emitted when there is nothing stored
         */
        to.assertValueAt(1) { testObjectList -> testObjectList == list }
    }

    @Test
    fun singularStreamEmitsWhenObjectListIsReplaced() {
        val list = ModelGenerationUtil.createChatMembershipDbModelList(3)
        val firstDbModel = list[0]

        reactiveStore.storeSingular(firstDbModel).subscribe()

        val to = reactiveStore.getSingular(firstDbModel.getUuid()).test()
        reactiveStore.replaceAll(null, list).subscribe()

        /* Since when we replaceAll the new list includes the old singular entity that we stored,
         * thus the testObserver will emit a second time (index 1).
         */
        to.assertValueAt(1) { testObject -> testObject == firstDbModel }
    }

    @Test
    fun allStreamEmitsWhenObjectListIsReplaced() {
        val list = ModelGenerationUtil.createChatMembershipDbModelList(3)
        reactiveStore.storeAll(list).subscribe()

        val to = reactiveStore.getAll(null).test()
        reactiveStore.replaceAll(null, list).subscribe()

        to.assertValueAt(0) { testObjectList -> testObjectList == list }
        //to.assertValueAt(1, testObjectList -> testObjectList.size() == 0); //empty is emitted
        to.assertValueAt(1) { testObjectList -> testObjectList == list }
    }

    @Test
    fun singularStreamEmitsOnceWhenSameItemInsertedTwice() {
        val dbModel = ModelGenerationUtil.createChatMembershipDbModel()
        val list = ArrayList<ChatDbModel>()
        list.add(dbModel)

        reactiveStore.storeAll(list).subscribe()
        reactiveStore.storeSingular(dbModel).subscribe()

        val to = reactiveStore.getSingular(dbModel.getUuid()).test()

        to.assertValueAt(0) { testObject -> testObject == dbModel }
        to.assertValueCount(1) //ensure only 1 emission
    }
    */
}
