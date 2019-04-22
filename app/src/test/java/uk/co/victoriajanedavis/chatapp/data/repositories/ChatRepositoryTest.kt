package uk.co.victoriajanedavis.chatapp.data.repositories

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.util.ArrayList
import java.util.UUID

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.mockito.Mock
import org.mockito.Mockito
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.network.ChatMembershipNwModel
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.RecentMessagesCache
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.FriendshipCache
import uk.co.victoriajanedavis.chatapp.data.repositories.cache.MessageCache
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BaseReactiveStore
import uk.co.victoriajanedavis.chatapp.data.repositories.store.MessageReactiveStore
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import uk.co.victoriajanedavis.chatapp.domain.Cache
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.common.BaseTest
import uk.co.victoriajanedavis.chatapp.common.ModelGenerationUtil

class ChatRepositoryTest : BaseTest() {

    private var chatStore: BaseReactiveStore<ChatDbModel>? = null
    private var friendshipStore: BaseReactiveStore<FriendshipDbModel>? = null
    private var chatRepository: ChatRepository? = null

    @Mock
    private val service: ChatAppService? = null


    @Before
    override fun setUp() {
        //super.setUp()
        //val testApplicationComponent =
        //testApplicationComponent.inject(this)
    }

    @After
    fun closeDb() {
    }

    /*

    @Test
    fun emptyListIsEmittedWhenCacheIsEmpty() {
        chatRepository!!.getAllChatMemberships().test().assertValue(???({ isEmpty() }))
        chatRepository!!.getAllChatMemberships().test().assertNotComplete()
    }

    @Test
    fun lastStoredObjectIsEmittedAfterSubscription() {
        val dbModels = createAndStoreChatMembershipDbModelsWithFriendShips(1)

        val getObserver = chatRepository!!.getAllChatMemberships().test()

        getObserver.assertValueAt(0, { list -> list.size == 1 })
        getObserver.assertValueAt(0, { list -> list.get(0).uuid == dbModels[0].getUuid() })
        getObserver.assertValueAt(0, { list -> list.get(0).friendship.chatUuid == dbModels[0].getUuid() })
        getObserver.assertValueCount(1)
    }

    @Test
    fun fetchChatMembershipsEmitsErrorWhenNetworkServiceErrors() {
        val throwable = Mockito.mock(Throwable::class.java)
        ArrangeBuilder().withErrorInChatMembershipsFromService(throwable)

        val fetchObserver = chatRepository!!.fetchChatMemberships().test()

        fetchObserver.assertError(throwable)
        fetchObserver.assertNotComplete()
    }

    @Test
    fun fetchedListIsEmittedAfterSubscription() {
        ArrangeBuilder().withChatMembershipsFromService(ModelGenerationUtil.createChatMembershipNwList(3))

        val fetchObserver = chatRepository!!.fetchChatMemberships().test()
        val getObserver = chatRepository!!.getAllChatMemberships().test()

        fetchObserver.assertComplete()

        getObserver.assertValueAt(0, { list -> list.size == 3 })
        getObserver.assertValueCount(1)
    }

    @Test
    fun emptyListAndFetchedListIsEmittedWhenSubscribedBeforeFetch() {
        ArrangeBuilder().withChatMembershipsFromService(ModelGenerationUtil.createChatMembershipNwList(3))

        val getObserver = chatRepository!!.getAllChatMemberships().test()
        val fetchObserver = chatRepository!!.fetchChatMemberships().test()

        getObserver.assertValueAt(0, Predicate<List<ChatEntity>> { it.isEmpty() })
        getObserver.assertValueAt(1, { list -> list.size == 3 })
        getObserver.assertValueCount(2)

        fetchObserver.assertComplete()
    }

    @Test
    fun emptyListReturnedFromNetworkIsNotFiltered() {
        ArrangeBuilder().withChatMembershipsFromService(ArrayList())
        createAndStoreChatMembershipDbModelsWithFriendShips(3)

        val getObserver = chatRepository!!.getAllChatMemberships().test()
        val fetchObserver = chatRepository!!.fetchChatMemberships().test()

        getObserver.assertValueAt(0, { list -> list.size == 3 })
        getObserver.assertValueAt(1, Predicate<List<ChatEntity>> { it.isEmpty() })
        getObserver.assertValueCount(2)

        fetchObserver.assertComplete()
    }


    /** */
    /****************** Helper methods  */
    /** */

    private fun createAndStoreChatMembershipDbModelsWithFriendShips(number: Int): List<ChatDbModel> {
        val chatDbModels = ArrayList<ChatDbModel>(number)
        for (i in 0 until number) {
            val dbModel = ModelGenerationUtil.createChatMembershipDbModelWithFriendshipDbModel()
            chatDbModels.add(dbModel)

            chatStore!!.storeSingular(dbModel).subscribe()
            friendshipStore!!.storeSingular(dbModel.friendship).subscribe()
        }

        return chatDbModels
    }

    private inner class ArrangeBuilder {

        private fun withChatMembershipsFromService(nwModels: List<ChatMembershipNwModel>): ArrangeBuilder {
            Mockito.`when`(service!!.chatMemberships).thenReturn(Single.just(nwModels))
            return this
        }

        private fun withErrorInChatMembershipsFromService(error: Throwable): ArrangeBuilder {
            Mockito.`when`(service!!.chatMemberships).thenReturn(Single.error(error))
            return this
        }
    }
    */
}
