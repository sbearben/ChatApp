package uk.co.victoriajanedavis.chatapp.data.services

import retrofit2.Converter
import retrofit2.Retrofit
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@ApplicationScope
class QueryConverterFactory @Inject constructor() : Converter.Factory() {

    override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<*, String>? {
        if (type == Date::class.java) {
            return createDateConverter()
        }
        return super.stringConverter(type, annotations, retrofit)
    }

    private fun createDateConverter(): Converter<Date, String> {
        return Converter { date ->
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(date)
        }
    }
}