package uk.co.victoriajanedavis.chatapp.data.services

import okhttp3.Interceptor
import okhttp3.Response
import uk.co.victoriajanedavis.chatapp.domain.entities.TokenEntityHolder
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class AuthenticationInterceptor @Inject constructor(
    private val tokenHolder : TokenEntityHolder
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (tokenHolder.tokenEntity == null) {
            throw NullPointerException("Error: Token hasn't been set in TokenHolder")
        }

        val tokenEntity = tokenHolder.tokenEntity!!
        val original = chain.request()

        val newRequestBuilder = original.newBuilder().method(original.method(), original.body())
        if (!tokenEntity.isEmpty) {
            newRequestBuilder.apply {
                header("Authorization", "Token ${tokenEntity.token}")
            }
        }

        return chain.proceed(newRequestBuilder.build())
    }
}