package uk.co.victoriajanedavis.chatapp.common

import uk.co.victoriajanedavis.chatapp.injection.component.UnitTestApplicationComponent
import androidx.test.core.app.ApplicationProvider
import uk.co.victoriajanedavis.chatapp.TestChatApp

abstract class BaseRoboelectricTest : BaseTest() {

    internal lateinit var unitTestComponent: UnitTestApplicationComponent

    open fun setUp() {
        //unitTestComponent = DaggerUnitTestApplicationComponent.builder().build()
        unitTestComponent = (ApplicationProvider.getApplicationContext() as TestChatApp)
            .appComponent as UnitTestApplicationComponent
        //database = unitTestComponent.chatAppDatabase()
    }
}
