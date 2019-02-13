package uk.co.victoriajanedavis.chatapp.domain.interactors

import javax.inject.Inject

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import uk.co.victoriajanedavis.chatapp.data.repositories.ChatRepository
import uk.co.victoriajanedavis.chatapp.domain.entities.ChatEntity
import uk.co.victoriajanedavis.chatapp.domain.interactors.ReactiveInteractor.RetrieveInteractor

import io.reactivex.Single.just
import uk.co.victoriajanedavis.chatapp.presentation.common.StreamState
import uk.co.victoriajanedavis.chatapp.presentation.ext.toStreamState

class GetChatList @Inject constructor(
    private val chatRepository: ChatRepository
) : RetrieveInteractor<Void, StreamState<List<ChatEntity>>> {

    override fun getBehaviorStream(params: Void?): Observable<StreamState<List<ChatEntity>>> {
        return chatRepository.allChatMemberships()
            .flatMapSingle(::fetchWhenEmptyAndThenChatMemberships)
            //.startWith(chatRepository.allChatMemberships.toStreamState())
    }

    private fun fetchWhenEmptyAndThenChatMemberships(
        chatMemberships: List<ChatEntity>
    ): Single<StreamState<List<ChatEntity>>> {
        return fetchWhenEmpty(chatMemberships).andThen(just(chatMemberships)).toStreamState()
    }

    private fun fetchWhenEmpty(chatMemberships: List<ChatEntity>): Completable {
        return if (chatMemberships.isEmpty())
            chatRepository.fetchChatMemberships()
        else
            Completable.complete()
    }
}
