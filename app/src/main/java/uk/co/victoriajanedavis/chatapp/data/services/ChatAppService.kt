package uk.co.victoriajanedavis.chatapp.data.services

import java.util.Date

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import uk.co.victoriajanedavis.chatapp.data.model.network.*

interface ChatAppService {

    /*************** RECEIVED Friend Request Endpoints  */
    @get:GET("chat/me/receivedfriendrequests/")
    val receivedFriendRequests: Single<List<UserNwModel>>


    /*************** SENT Friend Request Endpoints  */
    @get:GET("chat/me/sentfriendrequests/")
    val sentFriendRequests: Single<List<UserNwModel>>


    /*************** Friends List  */
    @get:GET("chat/me/friends/")
    val friends: Single<List<UserNwModel>>


    /*************** Chat Membership List  */
    @get:GET("chat/me/chats/")
    val chatMemberships: Single<List<ChatMembershipNwModel>>

    @POST("chat/api-token-auth/")
    @FormUrlEncoded
    fun getAuthenticatedToken(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<TokenNwModel>

    @GET("chat/users/{user_uuid}/")
    fun getUser(@Path("user_uuid") uuid: String): Single<UserNwModel>

    /*************** Login/Logout/Register Endpoints  */

    @POST("rest-auth/login/")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<TokenNwModel>

    @POST("rest-auth/logout/")
    @FormUrlEncoded
    fun logout(): Completable

    @POST("rest-auth/registration/")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password1") password1: String,
        @Field("password2") password2: String
    ): Single<TokenNwModel>

    @POST("chat/me/receivedfriendrequests/")
    fun acceptFriendRequest(@Query("username") username: String): Single<UserNwModel>

    @DELETE("chat/me/receivedfriendrequests/")
    fun rejectFriendRequest(@Query("username") username: String): Single<UserNwModel>

    @POST("chat/me/sentfriendrequests/")
    fun sendFriendRequest(@Query("username") username: String): Single<UserNwModel>

    @DELETE("chat/me/sentfriendrequests/")
    fun cancelSentFriendRequest(@Query("username") username: String): Single<UserNwModel>


    /*************** Chat Details  */
    @GET("chat/me/chats/{chat_uuid}/")
    fun getChatMessages(@Path("chat_uuid") chatUuid: String): Single<CollectionNwModel<MessageNwModel>>

    @GET("chat/me/chats/{chat_uuid}/")
    fun getNewestChatMessages(
        @Path("chat_uuid") chatUuid: String,
        @Query("per_page") perPage: Int?
    ): Single<List<MessageNwModel>>

    @GET
    fun getChatMessagesByUrl(@Url url: String): Single<CollectionNwModel<MessageNwModel>>

    @GET("chat/me/chats/{chat_uuid}/")
    fun getChatMessagesOlderThanGivenDate(
        @Path("chat_uuid") chatUuid: String,
        @Query("created_before") createdBefore: Date,
        @Query("per_page") perPage: Int?
    ): Single<List<MessageNwModel>>

    @POST("chat/me/chats/{chat_uuid}/")
    @FormUrlEncoded
    fun postMessageToChat(
        @Path("chat_uuid") chatUuid: String,
        @Field("text") message: String
    ): Single<MessageNwModel>


    /*************** Firebase Stuff  */
    @POST("chat/me/firebasetoken/")
    @FormUrlEncoded
    fun postFirebaseToken(@Field("token") token: String): Completable

    @DELETE("chat/me/firebasetoken/")
    fun invalidateFirebaseToken(): Completable


}