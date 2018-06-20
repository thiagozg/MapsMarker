package br.com.mapsmarker.features.home

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.action.ViewActions.*
import android.support.test.rule.ActivityTestRule
import android.view.KeyEvent
import android.widget.EditText
import br.com.mapsmarker.*
import br.com.mapsmarker.model.domain.SearchResponseVO
import br.com.mapsmarker.model.repository.GoogleMapsRepository
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import io.reactivex.Single
import org.mockito.ArgumentMatchers

//import com.squareup.okhttp.mockwebserver.MockResponse
//import com.squareup.okhttp.mockwebserver.MockWebServer

class HomeArrangeRobot {
    fun mockSearchRequest(server: MockWebServer, fileName: String) =
            server.enqueue(MockResponse()
                    .setResponseCode(200)
                    .setBody(readJsonFile(getInstrumentation().getContext(), fileName))
            )

//    fun mockSearchRequest(repository: GoogleMapsRepository) {
//        whenever(repository.searchByQuery(ArgumentMatchers.anyString()))
//                .thenReturn(Single.just(
//                        getJsonObject(getInstrumentation().getContext(),
//                                "query_springfield_response_200.json",
//                                SearchResponseVO::class.java)))
//    }

    fun startHomeActivity(rule: ActivityTestRule<HomeActivity>) = rule.launchActivity(null)
}

class HomeActRobot {
    fun clickOnSearchIcon() = viewBy(R.id.action_search).perform(click())

    fun typeOnSearchTab(value: String) = viewBy(EditText::class.java)
            .perform(typeText(value), pressKey(KeyEvent.KEYCODE_ENTER))
}

class HomeAssertRobot {
    fun checkItemTitleLatitudeLongite(place: String, lat: Double, lng: Double) {
        viewBy("Place: $place").isDisplayed()
        viewBy("Location (Lat: $lat | Lng: $lng)").isDisplayed()
    }
}
