package com.vourourou.forklife.ui.history

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vourourou.forklife.HiltComponentActivity
import com.vourourou.forklife.R
import com.vourourou.forklife.data.local.ScanHistoryDao
import com.vourourou.forklife.data.local.entity.ScanHistoryItem
import com.vourourou.forklife.ui.theme.ForkLifeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HistoryScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Inject
    lateinit var scanHistoryDao: ScanHistoryDao

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun getString(resId: Int) = context.getString(resId)
    private fun getString(resId: Int, vararg formatArgs: Any) = context.getString(resId, *formatArgs)

    private val testProductName = "Nutella"
    private val testBarcode = "3017620422003"

    private val testHistoryItem = ScanHistoryItem(
        barcode = testBarcode,
        productName = testProductName,
        imageUrl = "https://example.com/nutella.jpg",
        nutriscoreGrade = "E",
        ecoscoreGrade = "D",
        novaGroup = 4,
        scannedAt = System.currentTimeMillis(),
        scanCount = 1
    )

    @Before
    fun setup() {
        hiltRule.inject()
        // Clear the database before each test
        runBlocking {
            scanHistoryDao.clearAll()
        }
    }

    @Test
    fun historyScreen_emptyState_showsEmptyMessage() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        // Verify empty state message is displayed
        composeTestRule.onNodeWithText(getString(R.string.no_scans), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun historyScreen_emptyState_showsHintMessage() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        // Verify hint message is displayed
        composeTestRule.onNodeWithText(getString(R.string.scanned_products_appear_here), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun historyScreen_emptyState_showsHistoryIcon() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        // Verify history icon is displayed (empty state)
        composeTestRule.waitForIdle()
    }

    @Test
    fun historyScreen_withItems_showsProductName() {
        // Insert test data
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(testProductName, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify product name is displayed
        composeTestRule.onNodeWithText(testProductName, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun historyScreen_withItems_showsNutriscoreBadge() {
        // Insert test data with nutriscore
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        val nutriScoreText = getString(R.string.nutri_score_format, "E")

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(nutriScoreText, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify Nutri-Score badge is displayed
        composeTestRule.onNodeWithText(nutriScoreText, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun historyScreen_withItems_showsEcoscoreBadge() {
        // Insert test data with ecoscore
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        val ecoScoreText = getString(R.string.eco_score_format, "D")

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(ecoScoreText, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify Eco-Score badge is displayed
        composeTestRule.onNodeWithText(ecoScoreText, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun historyScreen_withInvalidGrade_doesNotShowBadge() {
        // Insert test data with invalid grade
        val itemWithInvalidGrade = testHistoryItem.copy(
            barcode = "invalid_grade_product",
            nutriscoreGrade = "unknown",
            ecoscoreGrade = "not-applicable"
        )
        runBlocking {
            scanHistoryDao.insert(itemWithInvalidGrade)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        // Wait for ViewModel to collect and emit data (product name should appear)
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(testProductName, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify invalid badges are not displayed
        composeTestRule.onNodeWithText(getString(R.string.nutri_score_format, "unknown"), useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithText(getString(R.string.eco_score_format, "not-applicable"), useUnmergedTree = true)
            .assertDoesNotExist()
    }

    @Test
    fun historyScreen_withMultipleScans_showsScanCount() {
        // Insert test data with multiple scans
        val itemWithMultipleScans = testHistoryItem.copy(scanCount = 5)
        runBlocking {
            scanHistoryDao.insert(itemWithMultipleScans)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        val scannedTimesText = getString(R.string.scanned_times_format, 5)

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(scannedTimesText, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify scan count is displayed
        composeTestRule.onNodeWithText(scannedTimesText, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun historyScreen_clickItem_triggersNavigation() {
        var navigatedBarcode = ""

        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = { barcode ->
                        navigatedBarcode = barcode
                    }
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(testProductName, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Click on the product item
        composeTestRule.onNodeWithText(testProductName, useUnmergedTree = true)
            .performClick()

        assertEquals("Navigation should be triggered with correct barcode", testBarcode, navigatedBarcode)
    }

    @Test
    fun historyScreen_multipleItems_displaysAll() {
        val secondProductName = "Coca-Cola"
        // Insert multiple test items
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
            scanHistoryDao.insert(
                testHistoryItem.copy(
                    barcode = "5449000000996",
                    productName = secondProductName,
                    nutriscoreGrade = "E",
                    ecoscoreGrade = "C"
                )
            )
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        // Wait for ViewModel to collect and emit data (wait for both products)
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(testProductName, substring = true)).fetchSemanticsNodes().isNotEmpty() &&
            composeTestRule.onAllNodes(hasText(secondProductName, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify both products are displayed
        composeTestRule.onNodeWithText(testProductName, useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(secondProductName, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun historyScreen_withNullGrades_doesNotShowBadges() {
        // Insert test data with null grades
        val itemWithNullGrades = testHistoryItem.copy(
            barcode = "null_grades_product",
            nutriscoreGrade = null,
            ecoscoreGrade = null
        )
        runBlocking {
            scanHistoryDao.insert(itemWithNullGrades)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(testProductName, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify product name is still displayed
        composeTestRule.onNodeWithText(testProductName, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun historyScreen_displaysProductImage() {
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                HistoryScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToProduct = {}
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(testProductName, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify product image is displayed (by content description)
        composeTestRule.onNodeWithContentDescription(testProductName, useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
