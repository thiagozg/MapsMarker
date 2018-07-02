package br.com.mapsmarker.features.home

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @get:Rule val activityRule = ActivityTestRule(HomeActivity::class.java, false, false)

    private val server by lazy { MockWebServer() }

    @Before
    fun setUp() {
        server.start(36004)
        server.url("/")
    }

    @After
    fun tearDown() = server.shutdown()

    @Test
    fun checkItemInfoSuccessful() {
        arrange {
            mockSearchRequest(server, "query_springfield_successful.json")
            startHomeActivity(activityRule)
        }
        act {
            clickOnSearchIcon()
            typeOnSearchTab("Springfield")
        }
        assert {
            checkItemTitleLatitudeLongite("Springfield, OR, EUA", 44.046236, -123.022029)
        }
    }

    @Test
    fun checkItemNoResults() {
        arrange {
            mockSearchRequest(server, "query_no_results.json")
            startHomeActivity(activityRule)
        }
        act {
            clickOnSearchIcon()
            typeOnSearchTab("HUEHuehu3")
        }
        assert {
            checkItemNoResultsDisplayed()
        }
    }

    @Test
    fun checkItemDisplayAll() {
        arrange {
            mockSearchRequest(server, "query_display_all.json")
            startHomeActivity(activityRule)
        }
        act {
            clickOnSearchIcon()
            typeOnSearchTab("666")
        }
        assert {
            checkItemDisplayAllDisplayed()
        }
    }

    @Test
    fun checkLoadingProgress() {
        arrange {
            startHomeActivity(activityRule)
        }
        act {
            clickOnSearchIcon()
            typeOnSearchTab("Springfield")
        }
        assert {
            checkLoadingProgressDisplayed()
        }
    }

    @Test
    fun checkErrorToastExceededLimit() {
        arrange {
            mockSearchRequest(server, "query_exceeded_limit.json")
            startHomeActivity(activityRule)
        }
        act {
            clickOnSearchIcon()
            typeOnSearchTab("limit")
        }
        assert {
            checkToastLimitShowed(activityRule.activity)
        }
    }

    fun arrange(func: HomeArrangeRobot.() -> Unit) = HomeArrangeRobot().apply { func() }
    fun act(func: HomeActRobot.() -> Unit) = HomeActRobot().apply { func() }
    fun assert(func: HomeAssertRobot.() -> Unit) = HomeAssertRobot().apply { func() }

}