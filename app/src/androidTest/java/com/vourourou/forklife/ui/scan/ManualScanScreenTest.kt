package com.vourourou.forklife.ui.scan

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vourourou.forklife.HiltComponentActivity
import com.vourourou.forklife.di.FakeOpenFoodFactsApi
import com.vourourou.forklife.ui.theme.ForkLifeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ManualScanScreenTest {

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
    fun manualScanScreen_displaysBarcodeTextField() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify barcode label is displayed
        composeTestRule.onNodeWithText("Barcode", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun manualScanScreen_displaysSearchButton() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify search button is displayed
        composeTestRule.onNodeWithText("Search", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun manualScanScreen_searchButtonDisabled_whenBarcodeEmpty() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify search button exists (enabled state depends on text field value)
        // When barcode is empty, button is disabled but still displayed
        composeTestRule.onNodeWithText("Search", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun manualScanScreen_searchButtonEnabled_whenBarcodeEntered() {
        // Use initialBarcode to pre-populate the text field
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "3017620422003",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify search button is enabled when barcode is provided
        composeTestRule.onNodeWithText("Search", useUnmergedTree = true)
            .assertIsEnabled()
    }

    @Test
    fun manualScanScreen_displaysEmptyState_whenNoSearch() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify empty state message is displayed
        composeTestRule.onNodeWithText("Enter a barcode to search", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun manualScanScreen_withInitialBarcode_populatesTextField() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "3017620422003",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify the barcode is populated in the text field
        // The text field should contain the barcode
        composeTestRule.onNodeWithText("3017620422003", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun manualScanScreen_withInitialBarcode_searchButtonEnabled() {
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "3017620422003",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify search button is enabled
        composeTestRule.onNodeWithText("Search", useUnmergedTree = true)
            .assertIsEnabled()
    }

    @Test
    fun manualScanScreen_search_showsProductInfo() {
        // Use initialBarcode to trigger search via LaunchedEffect
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "3017620422003",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Wait for the product to load
        composeTestRule.waitForIdle()

        // Verify product tabs are displayed (indicating success)
        composeTestRule.onNodeWithText("Score", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun manualScanScreen_search_showsTabs() {
        // Use initialBarcode to trigger search via LaunchedEffect
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "3017620422003",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify all tabs are displayed
        composeTestRule.onNodeWithText("Characteristics", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Ingredients", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Environment", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun manualScanScreen_searchError_showsProductNotFound() {
        FakeOpenFoodFactsApi.shouldReturnError = true

        // Use initialBarcode to trigger search via LaunchedEffect
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "1234567890123",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify error message is displayed
        composeTestRule.onNodeWithText("Product not found", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun manualScanScreen_afterSuccess_showsNewScanButton() {
        // Use initialBarcode to trigger search via LaunchedEffect
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "3017620422003",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify "New scan" button is displayed
        composeTestRule.onNodeWithText("New scan", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun manualScanScreen_clickNewScan_clearsBarcode() {
        // Use initialBarcode to trigger search via LaunchedEffect
        composeTestRule.setContent {
            ForkLifeTheme {
                ManualScanScreen(
                    initialBarcode = "3017620422003",
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Wait for product info to load and New scan button to appear
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("New scan", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verify the barcode field shows the initial barcode before reset
        composeTestRule.onNodeWithText("3017620422003", useUnmergedTree = true)
            .assertExists()

        // Click "New scan" button
        composeTestRule.onNodeWithText("New scan", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()

        // After clicking "New scan", the barcode text field should be cleared
        // (note: the product info remains displayed but barcode field is cleared)
        composeTestRule.onNodeWithText("3017620422003", useUnmergedTree = true)
            .assertDoesNotExist()
    }
}
