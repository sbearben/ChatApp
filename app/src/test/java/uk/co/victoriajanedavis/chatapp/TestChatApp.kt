package uk.co.victoriajanedavis.chatapp

import org.robolectric.TestLifecycleApplication
import uk.co.victoriajanedavis.chatapp.injection.component.ApplicationComponent
import uk.co.victoriajanedavis.chatapp.injection.component.DaggerUnitTestApplicationComponent
import java.lang.reflect.Method


class TestChatApp : ChatApp() { //, TestLifecycleApplication {

    override fun createAppComponent(): ApplicationComponent {
        return DaggerUnitTestApplicationComponent.builder()
            .application(this)
            .build()
    }

    override fun initLeakCanary() {
    }

    override fun mustDie(`object`: Any) {
    }

    /*
    override fun beforeTest(method: Method?) {
    }

    override fun prepareTest(test: Any?) {
    }

    override fun afterTest(method: Method?) {
    }
    */
}