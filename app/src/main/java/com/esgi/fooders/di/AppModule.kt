package com.esgi.fooders.di

import com.esgi.fooders.data.remote.FoodersApi
import com.esgi.fooders.data.repository.LoginRepository
import com.esgi.fooders.utils.Constants.FOODERS_API_BASE_URL
import com.esgi.fooders.utils.Constants.GITHUB_API_BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideFoodersApi(): FoodersApi {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        val gson = gsonBuilder.create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(FOODERS_API_BASE_URL)
            .build()
            .create(FoodersApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(api: FoodersApi): LoginRepository {
        return LoginRepository(api)
    }

}