package br.com.mapsmarker.features.home

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.action.ViewActions.*
import android.support.test.rule.ActivityTestRule
import android.view.KeyEvent
import android.widget.EditText
import br.com.mapsmarker.R
import br.com.mapsmarker.isDisplayed
import br.com.mapsmarker.readJsonFile
import br.com.mapsmarker.viewBy
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer

class HomeArrangeRobot {
    fun mockSearchRequest(server: MockWebServer, fileName: String) =
            server.enqueue(MockResponse()
                    .setResponseCode(200)
                    .setBody(readJsonFile(getInstrumentation().getContext(), fileName))
            )

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
