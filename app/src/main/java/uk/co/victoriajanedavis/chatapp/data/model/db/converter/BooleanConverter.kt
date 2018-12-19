package uk.co.victoriajanedavis.chatapp.data.model.db.converter

import androidx.room.TypeConverter

class BooleanConverter {
    @TypeConverter
    fun toBoolean(value: Int): Boolean {
        return value != 0
    }

    @TypeConverter
    fun toInt(value: Boolean): Int {
        return if (value) 1 else 0
    }
}
