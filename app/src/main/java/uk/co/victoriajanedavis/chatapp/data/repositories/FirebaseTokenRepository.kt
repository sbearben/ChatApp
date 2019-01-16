package uk.co.victoriajanedavis.chatapp.data.repositories

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.model.sharedpref.FirebaseTokenSpModel
import uk.co.victoriajanedavis.chatapp.data.repositories.store.BasePublishSubjectSingularStore
import uk.co.victoriajanedavis.chatapp.data.services.ChatAppService
import javax.inject.Inject

class FirebaseTokenRepository @Inject constructor(
    private val chatService: ChatAppService,
    private val firebaseTokenStore: BasePublishSubjectSingularStore<FirebaseTokenSpModel>
) {

    fun getTokenStream() : Observable<String> {
        return firebaseTokenStore.getSingular()
            .map { tokenSpModel -> tokenSpModel.token }
    }

    fun fetchToken() : Completable {
        return Single.fromCallable  {
            var firebaseToken = ""
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
                { r -> r.run() },  // this Executor gets the InstanceId synchronously
                { instanceIdResult ->  firebaseToken = instanceIdResult.token }
            )
            return@fromCallable firebaseToken
        }
            .flatMapCompletable { token -> storeToken(token) }
    }

    fun postTokenToBackend(token: String) : Completable {
        return chatService.postFirebaseToken(token)
    }


    fun deleteTokenFromBackend() : Completable {
        return chatService.deleteFirebaseToken()
    }

    fun storeToken(token : String) : Completable {
        return firebaseTokenStore.storeSingular(FirebaseTokenSpModel(token))
    }
}