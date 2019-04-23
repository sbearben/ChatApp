package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.Subcomponent
import dagger.android.AndroidInjector
import uk.co.victoriajanedavis.chatapp.injection.module.ActivitiesViewModelModule
import uk.co.victoriajanedavis.chatapp.injection.module.FragmentsModule
import uk.co.victoriajanedavis.chatapp.injection.scopes.PerActivity
import uk.co.victoriajanedavis.chatapp.presentation.ui.main.MainActivity

@PerActivity
@Subcomponent(modules = [
    FragmentsModule::class,
    ActivitiesViewModelModule::class
])
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<MainActivity>()
}