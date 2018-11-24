package uk.co.victoriajanedavis.chatapp.presentation.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import uk.co.victoriajanedavis.chatapp.injection.scopes.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@ApplicationScope
class ViewModelFactory @Inject constructor(
        private val creators: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        try {
            @Suppress("UNCHECKED_CAST")
            return creators[modelClass]?.get() as T
        } catch (e: Exception) {
            throw IllegalArgumentException("$modelClass cannot be created without a @Provides annotated method.")
        }
    }
}