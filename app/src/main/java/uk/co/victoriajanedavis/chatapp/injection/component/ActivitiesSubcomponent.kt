package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.Subcomponent
import dagger.android.AndroidInjector
import uk.co.victoriajanedavis.chatapp.injection.module.FragmentsModule
import uk.co.victoriajanedavis.chatapp.injection.module.FragmentsViewModelModule
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainActivity

@Subcomponent(modules = [
    FragmentsModule::class,
    FragmentsViewModelModule::class
])
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<MainActivity>()
}