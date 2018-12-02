package uk.co.victoriajanedavis.chatapp.data.services;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import uk.co.victoriajanedavis.chatapp.data.model.network.*;

public interface ChatAppService {

    @POST("chat/api-token-auth/")
    @FormUrlEncoded
    Single<TokenNwModel> getAuthenticatedToken(@Field("username") String username,
                                               @Field("password") String password);

    @GET("chat/users/{user_uuid}/")
    Single<UserNwModel> getUser(@Path("user_uuid") String uuid);

    /*************** Login/Logout/Register Endpoints ***************/

    @POST("rest-auth/login/")
    @FormUrlEncoded
    Single<TokenNwModel> login(@Field("username") String username,
                               @Field("password") String password);

    @POST("rest-auth/logout/")
    @FormUrlEncoded
    Completable logout();

    @POST("rest-auth/registration/")
    @FormUrlEncoded
    Single<TokenNwModel> register(@Field("username") String username,
                                  @Field("email") String email,
                                  @Field("password1") String password1,
                                  @Field("password2") String password2);

    /*************** RECEIVED Friend Request Endpoints ***************/
    @GET("chat/me/receivedfriendrequests/")
    Single<List<UserNwModel>> getReceivedFriendRequests();

    @POST("chat/me/receivedfriendrequests/")
    Single<UserNwModel> acceptFriendRequest(@Query("username") String username);

    @DELETE("chat/me/receivedfriendrequests/")
    Single<UserNwModel> rejectFriendRequest(@Query("username") String username);


    /*************** SENT Friend Request Endpoints ***************/
    @GET("chat/me/sentfriendrequests/")
    Single<List<UserNwModel>> getSentFriendRequests();

    @POST("chat/me/sentfriendrequests/")
    Single<UserNwModel> sendFriendRequest(@Query("username") String username);

    @DELETE("chat/me/sentfriendrequests/")
    Single<UserNwModel> cancelSentFriendRequest(@Query("username") String username);


    /*************** Friends List ***************/
    @GET("chat/me/friends/")
    Single<List<UserNwModel>> getFriends();


    /*************** Chat Membership List ***************/
    @GET("chat/me/chats/")
    Single<List<ChatMembershipNwModel>> getChatMemberships();


    /*************** Chat Details ***************/
    @GET("chat/me/chats/{chat_uuid}/")
    Single<CollectionNwModel<MessageNwModel>> getChatMessages(@Path("chat_uuid") String chatUuid);

    @GET("chat/me/chats/{chat_uuid}/")
    Single<List<MessageNwModel>> getNewestChatMessages(@Path("chat_uuid") String chatUuid,
                                                       @Query("per_page") Integer perPage);

    @GET
    Single<CollectionNwModel<MessageNwModel>> getChatMessagesByUrl(@Url String url);

    @GET("chat/me/chats/{chat_uuid}/")
    Single<List<MessageNwModel>> getChatMessagesOlderThanGivenDate(@Path("chat_uuid") String chatUuid,
                                                                   @Query("created_before") Date createdBefore,
                                                                   @Query("per_page") Integer perPage);

    @POST("chat/me/chats/{chat_uuid}/")
    @FormUrlEncoded
    Single<MessageNwModel> postMessageToChat(@Path("chat_uuid") String chatUuid,
                                             @Field("text") String message);


    /*************** Firebase Stuff ***************/
    @POST("chat/me/firebasetoken/")
    @FormUrlEncoded
    Completable postFirebaseToken(@Field("token") String token);

    @DELETE("chat/me/firebasetoken/")
    Completable invalidateFirebaseToken();




}
