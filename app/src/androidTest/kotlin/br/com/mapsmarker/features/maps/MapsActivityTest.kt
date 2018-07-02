package br.com.mapsmarker.features.maps

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.mapsmarker.features.map.MapsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapsActivityTest {

    @get:Rule val activityRule = ActivityTestRule(MapsActivity::class.java, false, false)

    @Test
    fun checkTitleChangedWhenClicked() {
        arrange(activityRule) {
            startMapsActivity()
        }
        act {
            clickOnMarker()
        }
        assert {
            checkItemTitle()
        }
    }

    @Test
    fun checkActionSaveSucceeded() {
        arrange(activityRule) {
            startMapsActivity()
            removeLocationFromDatabase("ChIJvwJwLkfewFQR1j9KnOnbNgU")
        }
        act {
            clickOnMarker()
            clickOnSaveActionIcon()
        }
        assert {
            checkToastMessageSucceesShowed(activityRule.activity)
            checkIconSaveChangeToDelete()
        }
    }

    @Test
    fun checkActionDeleteSucceeded() {
        arrange(activityRule) {
            startMapsActivity()
            saveLocationFromDatabase("ChIJvwJwLkfewFQR1j9KnOnbNgU")
        }
        act {
            clickOnMarker()
            clickOnDeleteActionIcon()
            confirmDeleteLocationOnDialog()
        }
        assert {
            checkIconDeleteChangeToSave()
        }
    }

    fun arrange(activityRule: ActivityTestRule<MapsActivity>, func: MapsArrangeRobot.() -> Unit) =
            MapsArrangeRobot(activityRule).apply { func() }
    fun act(func: MapsActRobot.() -> Unit) = MapsActRobot().apply { func() }
    fun assert(func: MapsAssertRobot.() -> Unit) = MapsAssertRobot().apply { func() }

}