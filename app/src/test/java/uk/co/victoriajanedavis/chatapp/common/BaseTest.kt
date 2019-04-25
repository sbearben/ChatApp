package uk.co.victoriajanedavis.chatapp.common

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import uk.co.victoriajanedavis.chatapp.injection.component.UnitTestApplicationComponent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import uk.co.victoriajanedavis.chatapp.TestChatApp
import uk.co.victoriajanedavis.chatapp.data.room.ChatAppDatabase


@RunWith(MockitoJUnitRunner.StrictStubs::class)
abstract class BaseTest {

    @Rule @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Rule @JvmField
    val overrideSchedulersRule = RxSchedulerOverrideRule()

    // Required to get Arch Components (Room) to execute tasks synchronously
    @Rule @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()
}
