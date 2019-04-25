package uk.co.victoriajanedavis.chatapp.injection.component

import dagger.Component
import uk.co.victoriajanedavis.chatapp.injection.module.FragmentsTestModule
import uk.co.victoriajanedavis.chatapp.injection.module.MockViewModelModule
import uk.co.victoriajanedavis.chatapp.presentation.common.InjectingFragmentFactory
import uk.co.victoriajanedavis.chatapp.presentation.ui.LoginFragmentTest
import javax.inject.Singleton

@Singleton
@Component(modules = [
    MockViewModelModule::class,
    FragmentsTestModule::class
])
interface FragmentTestComponent {

    fun inject(test: LoginFragmentTest)

    fun fragmentFactory(): InjectingFragmentFactory
}