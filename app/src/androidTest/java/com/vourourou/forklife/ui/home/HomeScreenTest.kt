package com.vourourou.forklife.ui.home

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vourourou.forklife.HiltComponentActivity
import com.vourourou.forklife.R
import com.vourourou.forklife.ui.theme.ForkLifeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun getString(resId: Int) = context.getString(resId)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun homeScreen_displaysQuickActionsSection() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        // Verify Quick Actions section header is displayed
        composeTestRule.onNodeWithText(getString(R.string.quick_actions), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysScanProductOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        // Verify Scan Product option is displayed
        composeTestRule.onNodeWithText(getString(R.string.scan_product), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysManualEntryOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        // Verify Manual Entry option is displayed
        composeTestRule.onNodeWithText(getString(R.string.manual_entry), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysYourActivitySection() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        // Verify Your Activity section header is displayed
        composeTestRule.onNodeWithText(getString(R.string.your_activity), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysHistoryOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        // Verify History option is displayed
        composeTestRule.onNodeWithText(getString(R.string.history), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysStatsOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        // Verify My Stats option is displayed
        composeTestRule.onNodeWithText(getString(R.string.my_stats), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysAboutOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        // Verify About option is displayed
        composeTestRule.onNodeWithText(getString(R.string.about), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_clickScanProduct_triggersNavigation() {
        var navigateCalled = false

        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = { navigateCalled = true },
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        composeTestRule.onNodeWithText(getString(R.string.scan_product), useUnmergedTree = true)
            .performClick()

        assertTrue("Navigate to scan should be called", navigateCalled)
    }

    @Test
    fun homeScreen_clickManualEntry_triggersNavigation() {
        var navigateCalled = false

        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = { navigateCalled = true },
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        composeTestRule.onNodeWithText(getString(R.string.manual_entry), useUnmergedTree = true)
            .performClick()

        assertTrue("Navigate to manual scan should be called", navigateCalled)
    }

    @Test
    fun homeScreen_clickHistory_triggersNavigation() {
        var navigateCalled = false

        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = { navigateCalled = true },
                    onNavigateToStats = {}
                )
            }
        }

        composeTestRule.onNodeWithText(getString(R.string.history), useUnmergedTree = true)
            .performClick()

        assertTrue("Navigate to history should be called", navigateCalled)
    }

    @Test
    fun homeScreen_clickStats_triggersNavigation() {
        var navigateCalled = false

        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = { navigateCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithText(getString(R.string.my_stats), useUnmergedTree = true)
            .performClick()

        assertTrue("Navigate to stats should be called", navigateCalled)
    }

    @Test
    fun homeScreen_clickAbout_opensDialog() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        // Click on About
        composeTestRule.onNodeWithText(getString(R.string.about), useUnmergedTree = true)
            .performClick()

        // Verify dialog is shown - check for dialog title
        composeTestRule.onNodeWithText(getString(R.string.about_forklife), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_aboutDialog_canBeClosed() {
        composeTestRule.setContent {
            ForkLifeTheme {
                HomeScreen(
                    paddingValues = PaddingValues(0.dp),
                    onNavigateToScan = {},
                    onNavigateToManualScan = {},
                    onNavigateToHistory = {},
                    onNavigateToStats = {}
                )
            }
        }

        // Open about dialog
        composeTestRule.onNodeWithText(getString(R.string.about), useUnmergedTree = true)
            .performClick()

        // Verify dialog is shown
        composeTestRule.onNodeWithText(getString(R.string.about_forklife), useUnmergedTree = true)
            .assertIsDisplayed()

        // Close dialog
        composeTestRule.onNodeWithText(getString(R.string.close), useUnmergedTree = true)
            .performClick()

        // Verify dialog is closed (About ForkLife should not be visible anymore)
        composeTestRule.onNodeWithText(getString(R.string.about_forklife), useUnmergedTree = true)
            .assertDoesNotExist()
    }
}
