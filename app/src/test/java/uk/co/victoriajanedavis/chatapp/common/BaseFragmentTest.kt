package uk.co.victoriajanedavis.chatapp.common

import uk.co.victoriajanedavis.chatapp.injection.component.DaggerFragmentTestComponent
import uk.co.victoriajanedavis.chatapp.injection.component.FragmentTestComponent
import uk.co.victoriajanedavis.chatapp.presentation.common.InjectingFragmentFactory

abstract class BaseFragmentTest : BaseTest() {

    internal lateinit var fragmentTestComponent: FragmentTestComponent
    internal lateinit var fragmentFactory: InjectingFragmentFactory

    open fun setUp() {
        fragmentTestComponent = DaggerFragmentTestComponent.create()
        fragmentFactory = fragmentTestComponent.fragmentFactory()
    }
}
