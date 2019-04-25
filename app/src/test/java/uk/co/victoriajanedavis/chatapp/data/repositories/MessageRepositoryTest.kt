package uk.co.victoriajanedavis.chatapp.data.repositories

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

import java.util.Date
import java.util.UUID

import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.common.BaseRoboelectricTest
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.model.network.MessageNwModel
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.common.MockitoUtil
import uk.co.victoriajanedavis.chatapp.common.ModelGenerationUtil
import uk.co.victoriajanedavis.chatapp.data.common.TimestampProvider
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class MessageRepositoryTest : BaseRoboelectricTest() {

    @Inject lateinit var repository: MessageRepository
    @Inject lateinit var store: MessageReactiveStore
    @Inject lateinit var database: ChatAppDatabase
    @Inject lateinit var service: ChatAppService


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
    fun emptyListIsEmittedWhenCacheIsEmpty() {
        repository.getAllMessagesInChat(UUID.randomUUID()).test().assertValue { list -> list.isEmpty() }
        repository.getAllMessagesInChat(UUID.randomUUID()).test().assertNotComplete()
    }

    @Test
    fun lastStoredObjectIsEmittedAfterSubscription() {
        val chatUuid = UUID.randomUUID()
        val messageDbModels = createMessagesForChatUuid(3, chatUuid)
        store.storeAll(messageDbModels).subscribe()

        val getObserver = repository.getAllMessagesInChat(chatUuid).test()

        getObserver.assertValueAt(0) { list -> list.size == 3 }
        //getObserver.assertValueAt(0, list -> list.get(0).equals(entities.get(0)));
        getObserver.assertValueCount(1)
    }

    @Test
    fun fetchedListIsEmittedAfterSubscription() {
        val chatUuid = UUID.randomUUID()
        ArrangeBuilder().withMessagesFromService(
            ModelGenerationUtil.createMessageNwList(chatUuid, 3)
        )

        val fetchObserver = repository.fetchInitialMessagesInChat(chatUuid).test()
        val getObserver = repository.getAllMessagesInChat(chatUuid).test()

        fetchObserver.assertComplete()

        getObserver.assertValueAt(0) { list -> list.size == 3 }
        getObserver.assertValueCount(1)
    }

    @Test
    fun emptyListAndFetchedListIsEmittedWhenSubscribedBeforeFetch() {
        val chatUuid = UUID.randomUUID()
        ArrangeBuilder().withMessagesFromService(
            ModelGenerationUtil.createMessageNwList(chatUuid, 3)
        )

        val getObserver = repository.getAllMessagesInChat(chatUuid).test()
        val fetchObserver = repository.fetchInitialMessagesInChat(chatUuid).test()

        getObserver.assertValueAt(0) { it.isEmpty() }
        getObserver.assertValueAt(1) { it.size == 3 }
        getObserver.assertValueCount(2)

        fetchObserver.assertComplete()
    }

    @Test
    fun emptyListReturnedFromNetworkReplacesCurrentlyStoredItems() {
        val chatUuid = UUID.randomUUID()
        val messageDbModels = createMessagesForChatUuid(3, chatUuid)

        ArrangeBuilder().withMessagesFromService(emptyList())

        store.storeAll(messageDbModels).subscribe()

        val getObserver = repository.getAllMessagesInChat(chatUuid).test()
        val fetchObserver = repository.fetchInitialMessagesInChat(chatUuid).test()

        getObserver.assertValueAt(0) { it.size == 3 }
        getObserver.assertValueAt(1) { it.isEmpty() }
        getObserver.assertValueCount(2)

        fetchObserver.assertComplete()
    }

    @Test
    fun fetchMoreItemsAreStoredAndAppendedToStream() {
        val chatUuid = UUID.randomUUID()
        val messageDbModels = createMessagesForChatUuid(3, chatUuid)

        ArrangeBuilder().withMoreMessagesFromService(
            ModelGenerationUtil.createMessageNwList(chatUuid, 3)
        )

        store.storeAll(messageDbModels).subscribe()

        val getObserver = repository.getAllMessagesInChat(chatUuid).test()
        val fetchObserver = repository.fetchMoreMessagesInChatOlderThanOldestInDb(chatUuid).test()

        getObserver.assertValueAt(0) { list -> list.size == 3 }
        getObserver.assertValueAt(1) { list -> list.size == 6 }
        getObserver.assertValueCount(2)

        fetchObserver.assertComplete()
    }

    private fun createMessagesForChatUuid(numMessages: Int, chatUuid: UUID): List<MessageDbModel> {
        return MutableList(numMessages) { i ->
            ModelGenerationUtil.createMessageDbModel(
                chatUuid = chatUuid,
                created = Date(TimestampProvider.currentTimeMillis() - i*1000)
            )
        }
    }

    private inner class ArrangeBuilder {

        fun withMessagesFromService(nwModels: List<MessageNwModel>): ArrangeBuilder {
            Mockito.`when`(service.getNewestChatMessages(
                Mockito.anyString(),
                Mockito.anyInt())
            ).thenReturn(Single.just(nwModels))
            return this
        }

        fun withMoreMessagesFromService(nwModels: List<MessageNwModel>): ArrangeBuilder {
            Mockito.`when`(
                service.getChatMessagesOlderThanGivenDate(
                    Mockito.anyString(),
                    MockitoUtil.any(Date::class.java),
                    Mockito.anyInt()
                )
            ).thenReturn(Single.just(nwModels))
            return this
        }

    }
}