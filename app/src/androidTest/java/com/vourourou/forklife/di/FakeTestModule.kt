package com.vourourou.forklife.di

import android.content.Context
import androidx.room.Room
import com.vourourou.forklife.data.local.ForkLifeDatabase
import com.vourourou.forklife.data.local.ScanHistoryDao
import com.vourourou.forklife.data.remote.OpenFoodFactsApi
import com.vourourou.forklife.data.remote.model.Nutriments
import com.vourourou.forklife.data.remote.model.OpenFoodFactsResponse
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.data.repository.HistoryRepository
import com.vourourou.forklife.data.repository.OpenFoodFactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Response
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object FakeTestModule {

    @Singleton
    @Provides
    fun provideFakeOpenFoodFactsApi(): OpenFoodFactsApi {
        return FakeOpenFoodFactsApi()
    }

    @Singleton
    @Provides
    fun provideOpenFoodFactsRepository(api: OpenFoodFactsApi): OpenFoodFactsRepository {
        return OpenFoodFactsRepository(api)
    }

    @Singleton
    @Provides
    fun provideForkLifeDatabase(@ApplicationContext context: Context): ForkLifeDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            ForkLifeDatabase::class.java
        ).allowMainThreadQueries().build()
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

class FakeOpenFoodFactsApi : OpenFoodFactsApi {

    companion object {
        val testProduct = Product(
            code = "3017620422003",
            product_name = "Nutella",
            image_front_url = "https://example.com/nutella.jpg",
            image_ingredients_url = null,
            image_nutrition_url = null,
            ingredients_text = "Sugar, Palm Oil, Hazelnuts, Cocoa, Skim Milk",
            nutriscore_grade = "E",
            ecoscore_grade = "D",
            nova_group = 4,
            nutriments = Nutriments(
                energy_kcal_100g = 539.0,
                fat_100g = 30.9,
                saturated_fat_100g = 10.6,
                carbohydrates_100g = 57.5,
                sugars_100g = 56.3,
                fiber_100g = 0.0,
                proteins_100g = 6.3,
                salt_100g = 0.107
            ),
            packaging = "Glass jar"
        )

        var shouldReturnError = false
        var customProduct: Product? = null
    }

    override suspend fun getProduct(barcode: String): Response<OpenFoodFactsResponse> {
        if (shouldReturnError) {
            return Response.success(
                OpenFoodFactsResponse(
                    code = barcode,
                    status = 0,
                    status_verbose = "product not found",
                    product = null
                )
            )
        }

        val product = customProduct ?: testProduct.copy(code = barcode)
        return Response.success(
            OpenFoodFactsResponse(
                code = barcode,
                status = 1,
                status_verbose = "product found",
                product = product
            )
        )
    }
}
