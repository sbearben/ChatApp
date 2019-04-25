package uk.co.victoriajanedavis.chatapp.presentation.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import uk.co.victoriajanedavis.chatapp.R
import uk.co.victoriajanedavis.chatapp.common.BaseTest
import uk.co.victoriajanedavis.chatapp.presentation.common.State
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginFragment
import uk.co.victoriajanedavis.chatapp.presentation.ui.login.LoginViewModel

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest : BaseTest() {

    private val viewModelFactory = Mockito.mock(ViewModelProvider.Factory::class.java)
    private val viewModel = Mockito.mock(LoginViewModel::class.java)
    private val fragmentFactory = object: FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
            return LoginFragment(viewModelFactory)
        }
    }

    private val loginLiveData = MutableLiveData<State<Void>>()


    @Before
    fun setUp() {
        `when`(viewModelFactory.create(LoginViewModel::class.java)).thenReturn(viewModel)
        `when`(viewModel.getLoginUserLiveData()).thenReturn(loginLiveData)
    }

    // These annotations are for roboelectric 4.3-alpha-2 and make the isDisplayed() check pass
    //@LooperMode(LooperMode.Mode.PAUSED)
    //@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
    @Test fun loadingViewsShowWhenStateIsLoading() {
        val scenario = launchFragmentInContainer<LoginFragment>(
            themeResId = R.style.Theme_MaterialComponents_Light_NoActionBar,
            factory = fragmentFactory)
        loginLiveData.value = State.ShowLoading

        //onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }


}