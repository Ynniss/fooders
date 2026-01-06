package com.vourourou.forklife.ui

import com.vourourou.forklife.ui.history.HistoryScreenTest
import com.vourourou.forklife.ui.home.HomeScreenTest
import com.vourourou.forklife.ui.product.ProductDetailScreenTest
import com.vourourou.forklife.ui.scan.ManualScanScreenTest
import com.vourourou.forklife.ui.scan.ScanScreenTest
import com.vourourou.forklife.ui.settings.SettingsScreenTest
import com.vourourou.forklife.ui.stats.StatsScreenTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Test suite that runs all UI tests.
 *
 * Run from command line:
 *   ./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.vourourou.forklife.ui.UiTestSuite
 *
 * Or run from Android Studio by right-clicking this file and selecting "Run 'UiTestSuite'"
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    HomeScreenTest::class,
    HistoryScreenTest::class,
    StatsScreenTest::class,
    SettingsScreenTest::class,
    ScanScreenTest::class,
    ManualScanScreenTest::class,
    ProductDetailScreenTest::class
)
class UiTestSuite
