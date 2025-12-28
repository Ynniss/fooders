package com.vourourou.forklife.di

import android.content.Context
import androidx.room.Room
import com.vourourou.forklife.BuildConfig
import com.vourourou.forklife.data.local.ForkLifeDatabase
import com.vourourou.forklife.data.local.ScanHistoryDao
import com.vourourou.forklife.data.remote.OpenFoodFactsApi
import com.vourourou.forklife.data.repository.HistoryRepository
import com.vourourou.forklife.data.repository.OpenFoodFactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", BuildConfig.API_USER_AGENT)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideOpenFoodFactsApi(client: OkHttpClient): OpenFoodFactsApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOpenFoodFactsRepository(api: OpenFoodFactsApi): OpenFoodFactsRepository {
        return OpenFoodFactsRepository(api)
    }

    @Singleton
    @Provides
    fun provideForkLifeDatabase(@ApplicationContext context: Context): ForkLifeDatabase {
        return Room.databaseBuilder(
            context,
            ForkLifeDatabase::class.java,
            "forklife_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideScanHistoryDao(database: ForkLifeDatabase): ScanHistoryDao {
        return database.scanHistoryDao()
    }

    @Singleton
    @Provides
    fun provideHistoryRepository(scanHistoryDao: ScanHistoryDao): HistoryRepository {
        return HistoryRepository(scanHistoryDao)
    }

}