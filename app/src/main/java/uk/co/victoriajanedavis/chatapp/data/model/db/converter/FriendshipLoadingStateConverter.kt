package uk.co.victoriajanedavis.chatapp.data.model.db.converter

import androidx.room.TypeConverter
import uk.co.victoriajanedavis.chatapp.domain.entities.FriendshipLoadingState

class FriendshipLoadingStateConverter {
    @TypeConverter
    fun toFriendshipLoadingState(value: Int): FriendshipLoadingState {
        return enumValues<FriendshipLoadingState>()[value]
    }

    @TypeConverter
    fun toInt(value: FriendshipLoadingState): Int {
        return value.ordinal
    }
}
