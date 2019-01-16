package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.Subcomponent
import dagger.android.AndroidInjector
import uk.co.victoriajanedavis.chatapp.presentation.fcm.MyFirebaseService

@Subcomponent
interface MyFirebaseServiceSubcomponent : AndroidInjector<MyFirebaseService> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<MyFirebaseService>()
}