package uk.co.victoriajanedavis.chatapp.data.store

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uk.co.victoriajanedavis.chatapp.common.BaseRoboelectricTest

import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.common.ModelGenerationUtil
import uk.co.victoriajanedavis.chatapp.data.common.TimestampProvider
import uk.co.victoriajanedavis.chatapp.data.repositories.store.RecentMessagesStore
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.domain.ReactiveStore
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@RunWith(AndroidJUnit4::class)
class RecentMessagesReactiveStoreTest : BaseRoboelectricTest() {

    // See https://medium.com/@WindRider/correct-usage-of-dagger-2-named-annotation-in-kotlin-8ab17ced6928
    @field:[Inject Named(RecentMessagesStore)] lateinit var reactiveStore: ReactiveStore<UUID, MessageDbModel>
    @Inject lateinit var database: ChatAppDatabase


    @Before
    override fun setUp() {
        super.setUp()
        unitTestComponent.inject(this)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun nothingIsEmittedFromSingularStreamWhenCacheIsEmpty() {
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
        val dbModel = ModelGenerationUtil.createMessageDbModel()
        reactiveStore.storeSingular(dbModel).subscribe()

        reactiveStore.getSingular(dbModel.uuid).test().assertValue(dbModel)
        reactiveStore.getAll(null).test().assertValue { list -> list.size == 1 }
    }

    @Test
    fun singularStreamEmitsWhenSingleObjectIsStored() {
        val dbModel = ModelGenerationUtil.createMessageDbModel()

        val to = reactiveStore.getSingular(dbModel.uuid).test()
        reactiveStore.storeSingular(dbModel).subscribe()

        to.assertValueAt(0) { storedObject -> storedObject == dbModel }
    }

    @Test
    fun oneRecentMessagePerChatEmitsFromAllStream() {
        val numChats = 3
        val numMessagesPerChat = 2

        val list = createMessagesForEachChat(numChats, numMessagesPerChat).toMutableList()
        reactiveStore.storeAll(list).subscribe()

        val to = reactiveStore.getAll(null).test()

        to.assertValueAt(0) { testObjectList -> testObjectList.size == numChats }

        val newChatMessages = createMessagesForEachChat(1, numMessagesPerChat)
        list.addAll(newChatMessages)
        reactiveStore.storeAll(newChatMessages).subscribe()

        to.assertValueAt(1) { testObjectList -> testObjectList.size == numChats+1 }
    }

    private fun createMessagesForEachChat(numChats: Int, numMessagesPerChat: Int): List<MessageDbModel> {
        return mutableListOf<MessageDbModel>().apply {
            for(i in (0 until numChats)) {
                addAll(createMessagesForChatUuid(numMessagesPerChat, UUID.randomUUID()))
            }
        }
    }

    private fun createMessagesForChatUuid(numMessages: Int, chatUuid: UUID): List<MessageDbModel> {
        return MutableList(numMessages) { i ->
            ModelGenerationUtil.createMessageDbModel(
                chatUuid = chatUuid,
                created = Date(TimestampProvider.currentTimeMillis() - i*1000)
            )
        }
    }
}
