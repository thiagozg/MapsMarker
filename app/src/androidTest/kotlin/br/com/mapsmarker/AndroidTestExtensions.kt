package br.com.mapsmarker

import android.content.Context
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import br.com.mapsmarker.AndroidTestConstantas.GSON
import br.com.mapsmarker.features.Constants
import com.google.gson.Gson
import java.io.BufferedReader


object AndroidTestConstantas {
    val GSON = Gson()
}

fun changeBaseUrl(newBaseUrl: String) { // TODO: finalize this logic
    val field = Constants.javaClass.getDeclaredField("BASE_URL")
//    val modifiersField = field.javaClass.getDeclaredField("modifiers")

    val isModifierAccessible = field.isAccessible()
    field.setAccessible(!isModifierAccessible)
//    val originalModifiersInt = field.getInt()
//    field.setInt(field, field.getModifiers() and Modifier.FINAL.inv())

    field.set(null, newBaseUrl)

    field.setAccessible(isModifierAccessible)
//    field.setInt(field, originalModifiersInt)
}

fun viewBy(id: Int) = onView(withId(id))

fun viewBy(text: String) = onView(withText(text))

inline fun <reified T : View> viewBy(clazz: Class<T>) = onView(ViewMatchers.isAssignableFrom(clazz))

fun ViewInteraction.isDisplayed() = this.check(matches(ViewMatchers.isDisplayed()));

inline fun <reified T> getJsonObject(context: Context, fileName: String, clazz: Class<T>) =
        GSON.fromJson(readJsonFile(context, fileName), clazz)

fun readJsonFile(context: Context, filePath: String): String {
    val stream = context.getResources().getAssets().open(filePath)
    val stringFromFile = stream.bufferedReader().use(BufferedReader::readText)
    stream.close()
    return stringFromFile
}
