package com.afaneca.dogscodechallenge.di

import android.content.Context
import androidx.room.Room
import com.afaneca.dogscodechallenge.BuildConfig
import com.afaneca.dogscodechallenge.common.AppDispatchers
import com.afaneca.dogscodechallenge.common.Constants
import com.afaneca.dogscodechallenge.data.local.db.DogDatabase
import com.afaneca.dogscodechallenge.data.local.db.breed.BreedDao
import com.afaneca.dogscodechallenge.data.local.db.dog.DogDao
import com.afaneca.dogscodechallenge.data.remote.DogApi
import com.afaneca.dogscodechallenge.data.repository.LiveDogBreedsRepository
import com.afaneca.dogscodechallenge.domain.repository.DogBreedsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //region HTTP
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        // Enable HTTP logging for debug only
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else logging.setLevel(HttpLoggingInterceptor.Level.NONE)
        return OkHttpClient.Builder().addInterceptor(logging).addInterceptor { chain ->
            // Inject x-api-key header into every outgoing request
            chain.proceed(chain.request().newBuilder().also {
                it.addHeader("x-api-key", BuildConfig.DOG_API_KEY)
            }.build())
        }.build()
    }

    @Provides
    @Singleton
    fun provideDogApi(httpClient: OkHttpClient): DogApi =
        Retrofit.Builder().baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient).build()
            .create(DogApi::class.java)

    //endregion

    //region repositories
    @Provides
    @Singleton
    fun provideDogBreedsRepository(
        api: DogApi,
        dogDao: DogDao,
        breedDao: BreedDao
    ): DogBreedsRepository = LiveDogBreedsRepository(dogDao, breedDao, api)
    //endregion

    //region DB
    @Provides
    @Singleton
    fun provideDogDatabase(@ApplicationContext context: Context): DogDatabase =
        Room.databaseBuilder(context, DogDatabase::class.java, "dog-db").build()

    @Provides
    @Singleton
    fun provideDogDao(db: DogDatabase) = db.dogDao()

    @Provides
    @Singleton
    fun provideBreedDao(db: DogDatabase) = db.breedDao()
    //endregion

    //region misc
    @Provides
    @Singleton
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers(Dispatchers.IO)
    //endregion
}