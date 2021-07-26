package com.esgi.fooders.di

import com.esgi.fooders.data.remote.FoodersApi
import com.esgi.fooders.data.repository.LoginRepository
import com.esgi.fooders.data.repository.ScanRepository
import com.esgi.fooders.utils.Constants.FOODERS_API_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideFoodersApi(): FoodersApi {
        val okHttpClient: OkHttpClient? = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(FOODERS_API_BASE_URL)
            .client(okHttpClient!!)
            .build()
            .create(FoodersApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(api: FoodersApi): LoginRepository {
        return LoginRepository(api)
    }

    @Singleton
    @Provides
    fun provideScanRepository(api: FoodersApi): ScanRepository {
        return ScanRepository(api)
    }

}