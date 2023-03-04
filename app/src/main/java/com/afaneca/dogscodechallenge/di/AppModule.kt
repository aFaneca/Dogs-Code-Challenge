package com.afaneca.dogscodechallenge.di

import com.afaneca.dogscodechallenge.BuildConfig
import com.afaneca.dogscodechallenge.common.Constants
import com.afaneca.dogscodechallenge.data.remote.DogApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  AppModule {

    //region HTTP
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        // Enable HTTP logging for debug only
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else logging.setLevel(HttpLoggingInterceptor.Level.NONE)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                // Inject x-api-key header into every outgoing request
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("x-api-key", BuildConfig.DOG_API_KEY)
                }.build())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideAfaScoreApi(httpClient: OkHttpClient): DogApi = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(DogApi::class.java)

    //endregion
}