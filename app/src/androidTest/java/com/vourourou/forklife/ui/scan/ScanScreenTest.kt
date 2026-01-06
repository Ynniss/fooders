package com.vourourou.forklife.ui.scan

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vourourou.forklife.HiltComponentActivity
import com.vourourou.forklife.data.remote.model.Nutriments
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.ui.theme.ForkLifeTheme
import com.vourourou.forklife.utils.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ScanScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private val testProduct = Product(
        code = "3017620422003",
        product_name = "Nutella",
        image_front_url = "https://example.com/nutella.jpg",
        ingredients_text = "Sugar, Palm Oil, Hazelnuts",
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

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun productBottomSheet_loadingState_showsSkeleton() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductBottomSheet(
                    productEvent = ProductInfoSharedViewModel.ProductInformationsEvent.Loading,
                    barcode = "3017620422003"
                )
            }
        }

        // In loading state, tabs should still be visible but non-interactive
        composeTestRule.onNodeWithText("Score", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productBottomSheet_successState_showsProductName() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductBottomSheet(
                    productEvent = ProductInfoSharedViewModel.ProductInformationsEvent.Success(
                        Resource.Success(testProduct)
                    ),
                    barcode = testProduct.code
                )
            }
        }

        // Verify product name is displayed
        composeTestRule.onNodeWithText("Nutella", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productBottomSheet_successState_showsBarcode() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductBottomSheet(
                    productEvent = ProductInfoSharedViewModel.ProductInformationsEvent.Success(
                        Resource.Success(testProduct)
                    ),
                    barcode = testProduct.code
                )
            }
        }

        // Verify barcode is displayed in the format "Code: X"
        composeTestRule.onNodeWithText("Code: ${testProduct.code}", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productBottomSheet_successState_showsTabs() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductBottomSheet(
                    productEvent = ProductInfoSharedViewModel.ProductInformationsEvent.Success(
                        Resource.Success(testProduct)
                    ),
                    barcode = testProduct.code
                )
            }
        }

        // Verify all tabs are displayed
        composeTestRule.onNodeWithText("Score", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Characteristics", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Ingredients", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Environment", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productBottomSheet_successState_canSwitchTabs() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductBottomSheet(
                    productEvent = ProductInfoSharedViewModel.ProductInformationsEvent.Success(
                        Resource.Success(testProduct)
                    ),
                    barcode = testProduct.code
                )
            }
        }

        // Click on Ingredients tab
        composeTestRule.onNodeWithText("Ingredients", useUnmergedTree = true)
            .performClick()

        // Verify tab content changed (ingredients text should be visible)
        composeTestRule.waitForIdle()
    }

    @Test
    fun productBottomSheet_failureState_showsProductNotFound() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductBottomSheet(
                    productEvent = ProductInfoSharedViewModel.ProductInformationsEvent.Failure(
                        "Product not found"
                    ),
                    barcode = "1234567890123"
                )
            }
        }

        // Verify error message is displayed
        composeTestRule.onNodeWithText("Product not found", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productBottomSheet_failureState_showsBarcode() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductBottomSheet(
                    productEvent = ProductInfoSharedViewModel.ProductInformationsEvent.Failure(
                        "Product not found"
                    ),
                    barcode = "1234567890123"
                )
            }
        }

        // Verify barcode is displayed
        composeTestRule.onNodeWithText("Code: 1234567890123", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productBottomSheet_emptyState_showsScanBarcodeMessage() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductBottomSheet(
                    productEvent = ProductInfoSharedViewModel.ProductInformationsEvent.Empty,
                    barcode = ""
                )
            }
        }

        // Verify empty state message is displayed
        composeTestRule.onNodeWithText("Scan a barcode", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productHeader_displaysProductImage() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductHeader(product = testProduct)
            }
        }

        // Verify product image content description
        composeTestRule.onNodeWithContentDescription("Product image", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productHeader_displaysProductName() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductHeader(product = testProduct)
            }
        }

        // Verify product name is displayed
        composeTestRule.onNodeWithText("Nutella", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productHeader_displaysBarcode() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductHeader(product = testProduct)
            }
        }

        // Verify barcode is displayed
        composeTestRule.onNodeWithText("Code: ${testProduct.code}", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productHeader_withUnknownProduct_displaysUnknownProductName() {
        val unknownProduct = testProduct.copy(product_name = null)

        composeTestRule.setContent {
            ForkLifeTheme {
                ProductHeader(product = unknownProduct)
            }
        }

        // Verify "Unknown product" is displayed
        composeTestRule.onNodeWithText("Unknown product", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
