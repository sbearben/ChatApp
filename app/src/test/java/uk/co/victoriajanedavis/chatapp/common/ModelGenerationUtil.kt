package uk.co.victoriajanedavis.chatapp.common

import io.reactivex.annotations.Nullable
import uk.co.victoriajanedavis.chatapp.data.common.TimestampProvider
import uk.co.victoriajanedavis.chatapp.data.model.db.ChatDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.FriendshipDbModel
import uk.co.victoriajanedavis.chatapp.data.model.db.MessageDbModel
import uk.co.victoriajanedavis.chatapp.data.model.network.*
import java.util.*

object ModelGenerationUtil {

    /*
    fun createChatMembershipNwList(number: Int): List<ChatMembershipNwModel> {
        val chatNwModels = ArrayList<ChatMembershipNwModel>(number)
        for (i in 0 until number) {
            chatNwModels.add(ChatMembershipNwModel(createChatNwModel(), createUserNwModel(null)))
        }

        return chatNwModels
    }


    fun createChatNwModel(): ChatNwModel {
        return ChatNwModel(UUID.randomUUID())
    }

    fun createUserNwModel(@Nullable username: String?): UserNwModel {
        val model = UserNwModel(UUID.randomUUID())
        model.setUsername(username)
        return model
    }

    fun createUserNwList(number: Int): List<UserNwModel> {
        val userNwModels = ArrayList<UserNwModel>(number)
        for (i in 0 until number) {
            userNwModels.add(createUserNwModel(null))
        }

        return userNwModels
    }

    fun createChatMembershipDbModelList(number: Int): List<ChatDbModel> {
        val chatDbModelList = ArrayList<ChatDbModel>(number)
        for (i in 0 until number) {
            chatDbModelList.add(createChatMembershipDbModelWithFriendshipDbModel())
        }

        return chatDbModelList
    }

    fun createChatMembershipDbModelWithFriendshipDbModel(): ChatDbModel {
        val dbModel = createChatMembershipDbModel()

        dbModel.setFriendship(createFriendshipDbModel(dbModel))
        return dbModel
    }

    */

    fun createMessageDbModel(text: String): MessageDbModel {
        return MessageDbModel(
            uuid = UUID.randomUUID(),
            text = text,
            created = Date(TimestampProvider.currentTimeMillis()),
            chatUuid = UUID.randomUUID(),
            userUuid = UUID.randomUUID(),
            userUsername = "Username",
            isFromCurrentUser = true
        )
    }

    /*
    fun createFriendshipDbModel(chatDbModel: ChatDbModel): FriendshipDbModel {
        val friendshipDbModel = FriendshipDbModel()
        friendshipDbModel.uuid = UUID.randomUUID()

        friendshipDbModel.chatUuid = chatDbModel.getUuid()
        return friendshipDbModel
    }

    fun createMessageNwList(chatUuid: UUID, number: Int): List<MessageNwModel> {
        val messageNwModels = ArrayList<MessageNwModel>(number)
        for (i in 0 until number) {
            messageNwModels.add(createMessageNwModel(chatUuid))
        }

        return messageNwModels
    }

    fun createMessageNwModel(@Nullable chatUuid: UUID): MessageNwModel {
        val model = MessageNwModel(UUID.randomUUID())
        model.setChatUuid(chatUuid)
        //model.setUser(createUserNwModel(null));
        return model
    }

    fun createTokenNwModel(): TokenNwModel {
        val model = TokenNwModel()

        model.setToken(UUID.randomUUID().toString())
        model.setUser(createUserNwModel(null))
        return model
    }
    */
}
