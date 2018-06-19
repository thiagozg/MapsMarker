package br.com.mapsmarker.features.home

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.mapsmarker.changeBaseUrl
import br.com.mapsmarker.features.Constants
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @get:Rule
    val rule = ActivityTestRule(HomeActivity::class.java, false, false)

    private var server: MockWebServer? = null

    @Before
    fun setUp() {
        server = MockWebServer()
        server!!.start()
        changeBaseUrl(server!!.url("/").toString())
    }

    @Test
    fun checkItemInfo() {
        arrange {
            mockSearchRequest(server!!, "query_springfield_response_200.json")
            startHomeActivity(rule)
        }
        act {
            clickOnSearchIcon()
            typeOnSearchTab("Springfield")
        }
        assert {
            checkItemTitleLatitudeLongite("Springfield, OR, EUA", 44.0462362, -123.0220289)
        }
    }

    fun checkErrorToast() {
        // TODO: .... https://github.com/riggaroo/android-retrofit-test-examples/blob/master/RetrofitTestExample/app/src/androidTest/java/za/co/riggaroo/retrofittestexample/MainActivityTest.java
    }

    fun arrange(func: HomeArrangeRobot.() -> Unit) = HomeArrangeRobot().apply { func() }
    fun act(func: HomeActRobot.() -> Unit) = HomeActRobot().apply { func() }
    fun assert(func: HomeAssertRobot.() -> Unit) = HomeAssertRobot().apply { func() }

}