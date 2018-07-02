package br.com.mapsmarker.di.modules

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class TestRetrofitModule {

    val BASE_URL_TEST: String = "http://localhost:36004/"

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient, gson: Gson) =
            builder(gson)
                    .baseUrl(BASE_URL_TEST)
                    .client(client)
                    .build()

    private fun builder(gson: Gson) =
            Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

}