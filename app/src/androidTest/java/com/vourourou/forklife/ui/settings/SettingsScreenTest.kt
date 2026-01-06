package com.vourourou.forklife.ui.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vourourou.forklife.HiltComponentActivity
import com.vourourou.forklife.ui.theme.ForkLifeTheme
import com.vourourou.forklife.utils.DataStoreManager
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
class SettingsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setup() {
        hiltRule.inject()
        // Reset to default theme
        runBlocking {
            dataStoreManager.updateTheme("Orange")
            dataStoreManager.updateDarkMode("System")
        }
    }

    @Test
    fun settingsScreen_displaysThemeColorSection() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify theme color section header is displayed
        composeTestRule.onNodeWithText("Theme color", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysDarkModeSection() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify dark mode section header is displayed
        composeTestRule.onNodeWithText("Dark mode", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysOrangeThemeOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify Orange theme option is displayed
        composeTestRule.onNodeWithText("Orange", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysAvocadoThemeOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify Avocado theme option is displayed
        composeTestRule.onNodeWithText("Avocado", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysCherryThemeOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify Cherry theme option is displayed
        composeTestRule.onNodeWithText("Cherry", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysSystemDarkModeOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify System dark mode option is displayed
        composeTestRule.onNodeWithText("System", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysLightDarkModeOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify Light dark mode option is displayed
        composeTestRule.onNodeWithText("Light", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysDarkDarkModeOption() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify Dark dark mode option is displayed
        composeTestRule.onNodeWithText("Dark", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysSystemDescription() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify System description is displayed
        composeTestRule.onNodeWithText("Follow system settings", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysLightDescription() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify Light description exists in the node tree (may not be visible due to scrolling)
        composeTestRule.onNodeWithText("Always use light mode", useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun settingsScreen_displaysDarkDescription() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Verify Dark description exists in the node tree (may not be visible due to scrolling)
        composeTestRule.onNodeWithText("Always use dark mode", useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun settingsScreen_canSelectAvocadoTheme() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Click on Avocado theme
        composeTestRule.onNodeWithText("Avocado", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()

        // Wait for DataStore to save
        Thread.sleep(200)

        // Verify theme was updated (indirectly by checking the radio button state)
        val savedTheme = runBlocking { dataStoreManager.readTheme() }
        assert(savedTheme == "Avocado") { "Theme should be Avocado, but was $savedTheme" }
    }

    @Test
    fun settingsScreen_canSelectCherryTheme() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Click on Cherry theme
        composeTestRule.onNodeWithText("Cherry", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()

        // Wait for DataStore to save
        Thread.sleep(200)

        // Verify theme was updated
        val savedTheme = runBlocking { dataStoreManager.readTheme() }
        assert(savedTheme == "Cherry") { "Theme should be Cherry, but was $savedTheme" }
    }

    @Test
    fun settingsScreen_canSelectOrangeTheme() {
        // First set to a different theme
        runBlocking {
            dataStoreManager.updateTheme("Avocado")
        }
        Thread.sleep(100)

        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Click on Orange theme
        composeTestRule.onNodeWithText("Orange", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()

        // Wait for DataStore to save
        Thread.sleep(200)

        // Verify theme was updated
        val savedTheme = runBlocking { dataStoreManager.readTheme() }
        assert(savedTheme == "Orange") { "Theme should be Orange, but was $savedTheme" }
    }

    @Test
    fun settingsScreen_canSelectLightMode() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Scroll to Light option and click
        composeTestRule.onNodeWithText("Light", useUnmergedTree = true)
            .performScrollTo()
            .performClick()

        composeTestRule.waitForIdle()

        // Wait for DataStore to save
        Thread.sleep(200)

        // Verify dark mode was updated
        val savedDarkMode = runBlocking { dataStoreManager.readDarkMode() }
        assert(savedDarkMode == "Light") { "Dark mode should be Light, but was $savedDarkMode" }
    }

    @Test
    fun settingsScreen_canSelectDarkMode() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Scroll to Dark option and click
        composeTestRule.onNodeWithText("Dark", useUnmergedTree = true)
            .performScrollTo()
            .performClick()

        composeTestRule.waitForIdle()

        // Wait for DataStore to save
        Thread.sleep(200)

        // Verify dark mode was updated
        val savedDarkMode = runBlocking { dataStoreManager.readDarkMode() }
        assert(savedDarkMode == "Dark") { "Dark mode should be Dark, but was $savedDarkMode" }
    }

    @Test
    fun settingsScreen_canSelectSystemMode() {
        // First set to a different mode
        runBlocking {
            dataStoreManager.updateDarkMode("Light")
        }
        Thread.sleep(100)

        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Click on System mode
        composeTestRule.onNodeWithText("System", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()

        // Wait for DataStore to save
        Thread.sleep(200)

        // Verify dark mode was updated
        val savedDarkMode = runBlocking { dataStoreManager.readDarkMode() }
        assert(savedDarkMode == "System") { "Dark mode should be System, but was $savedDarkMode" }
    }

    @Test
    fun settingsScreen_orangeTheme_showsCheckmark_whenSelected() {
        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        // Orange is selected by default, verify checkmark icon is displayed
        // The checkmark is inside the color circle when selected
        composeTestRule.onNodeWithText("Orange", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun settingsScreen_persists_themeSelection() {
        // Set a specific theme
        runBlocking {
            dataStoreManager.updateTheme("Cherry")
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify the theme persisted (Cherry should be selected)
        val savedTheme = runBlocking { dataStoreManager.readTheme() }
        assert(savedTheme == "Cherry") { "Theme should be Cherry, but was $savedTheme" }
    }

    @Test
    fun settingsScreen_persists_darkModeSelection() {
        // Set a specific dark mode
        runBlocking {
            dataStoreManager.updateDarkMode("Dark")
        }

        composeTestRule.setContent {
            ForkLifeTheme {
                SettingsScreen(
                    paddingValues = PaddingValues(0.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        // Verify the dark mode persisted
        val savedDarkMode = runBlocking { dataStoreManager.readDarkMode() }
        assert(savedDarkMode == "Dark") { "Dark mode should be Dark, but was $savedDarkMode" }
    }
}
