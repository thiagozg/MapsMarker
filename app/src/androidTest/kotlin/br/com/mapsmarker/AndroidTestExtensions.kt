package br.com.mapsmarker

import android.content.Context
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.app.AppCompatActivity
import android.view.View
import br.com.mapsmarker.AndroidTestConstantas.GSON
import com.google.gson.Gson
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import java.io.BufferedReader


object AndroidTestConstantas {
    val GSON = Gson()
}

fun viewBy(id: Int) = onView(withId(id))

fun viewBy(text: String) = onView(withText(text))

inline fun <reified T : View> viewBy(clazz: Class<T>) = onView(ViewMatchers.isAssignableFrom(clazz))

fun ViewInteraction.onScreen(activity: AppCompatActivity) = inRoot(withDecorView(not(`is`(activity.getWindow().getDecorView()))))

fun ViewInteraction.isDisplayed() = this.check(matches(ViewMatchers.isDisplayed()));

inline fun <reified T> getJsonObject(context: Context, fileName: String, clazz: Class<T>) =
        GSON.fromJson(readJsonFile(context, fileName), clazz)

fun readJsonFile(context: Context, filePath: String): String {
    val stream = context.getResources().getAssets().open(filePath)
    val stringFromFile = stream.bufferedReader().use(BufferedReader::readText)
    stream.close()
    return stringFromFile
}

fun sleep(millis: Long = 1000L) = Thread.sleep(millis)
