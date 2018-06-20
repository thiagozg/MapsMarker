package br.com.mapsmarker

import android.support.test.InstrumentationRegistry
import br.com.mapsmarker.di.AppComponent
import br.com.mapsmarker.di.modules.*
import dagger.android.support.AndroidSupportInjectionModule
import it.cosenonjaviste.daggermock.DaggerMock

fun espressoDaggerMockRule() = DaggerMock.rule<AppComponent>(
        AndroidSupportInjectionModule::class.java,
        AndroidModule(),
        NetModule(),
        RetrofitModule(),
        ApiModule(),
        RoomModule(),
        BuildersModule::class.java) {
    set { component -> component.inject(app) }
}

val app: CustomApplication
    get() = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            as CustomApplication