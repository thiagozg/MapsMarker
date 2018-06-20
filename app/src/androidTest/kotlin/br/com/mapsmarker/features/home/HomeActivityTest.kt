package br.com.mapsmarker.features.home

import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.mapsmarker.changeBaseUrl
import br.com.mapsmarker.espressoDaggerMockRule
import br.com.mapsmarker.getJsonObject
import br.com.mapsmarker.model.domain.SearchResponseVO
import br.com.mapsmarker.model.repository.GoogleMapsRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.okhttp.mockwebserver.MockWebServer
import io.reactivex.Single
import org.bouncycastle.crypto.tls.ConnectionEnd.server
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @get:Rule
    val daggerRule = espressoDaggerMockRule()

    @get:Rule
    val activityRule = ActivityTestRule(HomeActivity::class.java, false, false)

    private val server: MockWebServer by lazy { MockWebServer() }

    @Before
    fun setUp() {
        System.setProperty(
                "dexmaker.dexcache",
                InstrumentationRegistry.getTargetContext().getCacheDir().getPath());

        server.start()
        changeBaseUrl(server.url("/").toString())
    }

//    val repository = mock<GoogleMapsRepository>()

    @Test
    fun checkItemInfo() {
        arrange {
            mockSearchRequest(server, "query_springfield_response_200.json")
//            mockSearchRequest(repository)
            startHomeActivity(activityRule)
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
        // TODO: ....
    }

    fun arrange(func: HomeArrangeRobot.() -> Unit) = HomeArrangeRobot().apply { func() }
    fun act(func: HomeActRobot.() -> Unit) = HomeActRobot().apply { func() }
    fun assert(func: HomeAssertRobot.() -> Unit) = HomeAssertRobot().apply { func() }

}