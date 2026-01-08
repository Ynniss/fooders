package com.vourourou.forklife.ui.product

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vourourou.forklife.HiltComponentActivity
import com.vourourou.forklife.di.FakeOpenFoodFactsApi
import com.vourourou.forklife.ui.theme.ForkLifeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ProductDetailScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        FakeOpenFoodFactsApi.shouldReturnError = false
        FakeOpenFoodFactsApi.customProduct = null
    }

    @After
    fun tearDown() {
        FakeOpenFoodFactsApi.shouldReturnError = false
        FakeOpenFoodFactsApi.customProduct = null
    }

    @Test
    fun productDetailScreen_displaysTopAppBar() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = {}
                )
            }
        }

        // Verify top app bar title is displayed
        composeTestRule.onNodeWithText("Product details", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productDetailScreen_displaysBackButton() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = {}
                )
            }
        }

        // Verify back button is displayed
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productDetailScreen_clickBackButton_triggersNavigation() {
        var backClicked = false

        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = { backClicked = true }
                )
            }
        }

        // Click back button
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .performClick()

        assertTrue("Back navigation should be triggered", backClicked)
    }

    @Test
    fun productDetailScreen_loadsProduct_showsProductName() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Wait for product to load and verify product name is displayed
        composeTestRule.onNodeWithText("Nutella", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productDetailScreen_loadsProduct_showsBarcode() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify barcode is displayed
        composeTestRule.onNodeWithText("Code: 3017620422003", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productDetailScreen_displaysTabs() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

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
    fun productDetailScreen_canSwitchTabs() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Click on Ingredients tab
        composeTestRule.onNodeWithText("Ingredients", useUnmergedTree = true)
            .performClick()

        // Wait for animation to complete
        composeTestRule.waitForIdle()

        // The tab should still be visible (as we switched to it)
        composeTestRule.onNodeWithText("Ingredients", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productDetailScreen_canSwitchToCharacteristicsTab() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Click on Characteristics tab
        composeTestRule.onNodeWithText("Characteristics", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()
    }

    @Test
    fun productDetailScreen_canSwitchToEnvironmentTab() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Click on Environment tab
        composeTestRule.onNodeWithText("Environment", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()
    }

    @Test
    fun productDetailScreen_productNotFound_showsErrorMessage() {
        FakeOpenFoodFactsApi.shouldReturnError = true

        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "1234567890123",
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify error message is displayed
        composeTestRule.onNodeWithText("Product not found", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productDetailScreen_productNotFound_showsBarcode() {
        FakeOpenFoodFactsApi.shouldReturnError = true

        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "1234567890123",
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify barcode is displayed in error state
        composeTestRule.onNodeWithText("Barcode: 1234567890123", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productDetailScreen_productNotFound_showsNotInDatabaseMessage() {
        FakeOpenFoodFactsApi.shouldReturnError = true

        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "1234567890123",
                    onNavigateBack = {}
                )
            }
        }

        // Wait for the API call to complete and error state to display
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Product not found", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verify additional info is displayed (actual string from strings.xml)
        composeTestRule.onNodeWithText("This product is not in the OpenFoodFacts database", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun productDetailScreen_displaysProductImage() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ProductDetailScreen(
                    barcode = "3017620422003",
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify product image is displayed
        composeTestRule.onNodeWithContentDescription("Product image", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
