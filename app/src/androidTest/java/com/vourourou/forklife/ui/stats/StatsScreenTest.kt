package com.vourourou.forklife.ui.stats

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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class StatsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Inject
    lateinit var scanHistoryDao: ScanHistoryDao

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun getString(resId: Int) = context.getString(resId)
    private fun getQuantityString(resId: Int, quantity: Int) =
        context.resources.getQuantityString(resId, quantity, quantity)

    private val testProductName = "Nutella"

    private val testHistoryItem = ScanHistoryItem(
        barcode = "3017620422003",
        productName = testProductName,
        imageUrl = "https://example.com/nutella.jpg",
        nutriscoreGrade = "E",
        ecoscoreGrade = "D",
        novaGroup = 4,
        scannedAt = System.currentTimeMillis(),
        scanCount = 5
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
    fun statsScreen_emptyState_showsZeroTotalScans() {
        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify total scans label is displayed (which implies 0 is shown)
        composeTestRule.onNodeWithText(getString(R.string.total_scans), useUnmergedTree = true)
            .assertIsDisplayed()
        // When there are multiple "0" values, we just check the label exists
        // The actual value is verified by checking the Total scans label
        composeTestRule.onAllNodesWithText("0", useUnmergedTree = true)
            .assertCountEquals(2) // 0 for total scans and 0 for unique products
    }

    @Test
    fun statsScreen_emptyState_showsTotalScansLabel() {
        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify "Total scans" label is displayed
        composeTestRule.onNodeWithText(getString(R.string.total_scans), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_emptyState_showsUniqueProductsLabel() {
        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify "Unique products" label is displayed
        composeTestRule.onNodeWithText(getString(R.string.unique_products), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_emptyState_showsMostScannedProductsSection() {
        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify "Most scanned products" section is displayed
        composeTestRule.onNodeWithText(getString(R.string.most_scanned_products), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_emptyState_showsEmptyStateCard() {
        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify empty state message is displayed
        composeTestRule.onNodeWithText(getString(R.string.no_statistics_yet), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_emptyState_showsStartScanningHint() {
        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify hint message is displayed
        composeTestRule.onNodeWithText(getString(R.string.start_scanning_to_see_stats), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_withData_showsTotalScans() {
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText("5", substring = false)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify total scans shows the scan count
        composeTestRule.onNodeWithText("5", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_withData_showsUniqueProducts() {
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText("1", substring = false)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify unique products shows 1
        composeTestRule.onNodeWithText("1", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_withData_showsMostScannedProduct() {
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(testProductName, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify most scanned product is displayed
        composeTestRule.onNodeWithText(testProductName, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_withData_showsScanCountForProduct() {
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        val scansCountText = getQuantityString(R.plurals.scans_count, 5)

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(scansCountText, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify scan count is displayed (pluralized)
        composeTestRule.onNodeWithText(scansCountText, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_withMultipleProducts_showsRanking() {
        val secondProductName = "Coca-Cola"
        val thirdProductName = "Oreo"
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
            scanHistoryDao.insert(
                testHistoryItem.copy(
                    barcode = "5449000000996",
                    productName = secondProductName,
                    scanCount = 3
                )
            )
            scanHistoryDao.insert(
                testHistoryItem.copy(
                    barcode = "3041090000000",
                    productName = thirdProductName,
                    scanCount = 2
                )
            )
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText(testProductName, substring = true)).fetchSemanticsNodes().isNotEmpty() &&
            composeTestRule.onAllNodes(hasText(secondProductName, substring = true)).fetchSemanticsNodes().isNotEmpty() &&
            composeTestRule.onAllNodes(hasText(thirdProductName, substring = true)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify all products are displayed
        composeTestRule.onNodeWithText(testProductName, useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(secondProductName, useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(thirdProductName, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_withMultipleProducts_showsCorrectTotalScans() {
        runBlocking {
            scanHistoryDao.insert(testHistoryItem) // 5 scans
            scanHistoryDao.insert(
                testHistoryItem.copy(
                    barcode = "5449000000996",
                    productName = "Coca-Cola",
                    scanCount = 3
                )
            ) // 3 scans
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText("8", substring = false)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify total scans shows 8 (5 + 3)
        composeTestRule.onNodeWithText("8", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_withMultipleProducts_showsCorrectUniqueCount() {
        runBlocking {
            scanHistoryDao.insert(testHistoryItem)
            scanHistoryDao.insert(
                testHistoryItem.copy(
                    barcode = "5449000000996",
                    productName = "Coca-Cola",
                    scanCount = 3
                )
            )
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Wait for ViewModel to collect and emit data
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(hasText("2", substring = false)).fetchSemanticsNodes().isNotEmpty()
        }

        // Verify unique products shows 2
        composeTestRule.onNodeWithText("2", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun statsScreen_showsEmptyFavoritesHint_whenNoProducts() {
        composeTestRule.setContent {
            ForkLifeTheme {
                StatsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify empty favorites hint
        composeTestRule.onNodeWithText(getString(R.string.scan_products_to_see_favorites), useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
