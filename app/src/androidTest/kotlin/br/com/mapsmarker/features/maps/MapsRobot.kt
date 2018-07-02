package br.com.mapsmarker.features.maps

import android.content.Context
import android.content.Intent
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.rule.ActivityTestRule
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObject
import android.support.test.uiautomator.UiSelector
import br.com.mapsmarker.*
import br.com.mapsmarker.features.Constants.KEY_PLACE
import br.com.mapsmarker.features.map.MapsActivity
import br.com.mapsmarker.model.domain.ResultVO
import br.com.mapsmarker.model.domain.SearchResponseVO
import org.parceler.Parcels

class MapsArrangeRobot(val rule: ActivityTestRule<MapsActivity>) {
    fun startMapsActivity() = rule.launchActivity(mockIntent(getInstrumentation().context))

    private fun mockIntent(context: Context) = Intent().apply {
        val mockSearchResponse = getJsonObject(context,
                "query_springfield_successful.json",
                SearchResponseVO::class.java)
        this.putExtra(KEY_PLACE, Parcels.wrap(mockSearchResponse.results))
    }

    fun removeLocationFromDatabase(placeId: String) = storeLocationFromDatabase(placeId, false)
    fun saveLocationFromDatabase(placeId: String) = storeLocationFromDatabase(placeId, true)

    private fun storeLocationFromDatabase(placeId: String, shouldSave: Boolean) =
            rule.activity.viewModel.storeLocation(ResultVO(placeId = placeId), shouldSave)
}

class MapsActRobot {
    private fun clickOnActionIcon() = viewBy(R.id.action).click()
    fun clickOnDeleteActionIcon() = clickOnActionIcon()
    fun clickOnSaveActionIcon() = clickOnActionIcon()

    fun clickOnMarker() = getMarker()!!.click()

    fun confirmDeleteLocationOnDialog() = viewBy("Delete").click()
}

class MapsAssertRobot {
    init { sleep(500L) }

    fun checkItemTitle() = getMarker()!!.exists()

    fun checkToastMessageSucceesShowed(activity: MapsActivity) =
            viewBy("Location saved with successful!")
                    .onScreen(activity)
                    .isDisplayed()

    fun checkIconSaveChangeToDelete() = viewByDescription("Delete").isDisplayed()

    fun checkIconDeleteChangeToSave() = viewByDescription("Save").isDisplayed()

}

private fun getMarker(): UiObject? {
    val titleMocked = "Springfield, OR, EUA: 44.0462362 / -123.0220289"
    val device = UiDevice.getInstance(getInstrumentation())
    val marker = device.findObject(UiSelector().descriptionContains(titleMocked))
    return marker
}
