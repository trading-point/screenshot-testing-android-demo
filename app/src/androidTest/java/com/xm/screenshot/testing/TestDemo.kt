package com.xm.screenshot.testing

import android.content.Context
import android.content.res.Configuration
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.ViewHelpers
import com.xm.MarketHoursView
import com.xm.R
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import java.util.*

class TestDemo {






































    data class TestCombo(val width: Int, val height: Int, val isDarkTheme: Boolean, val locale: Locale)

























    @Rule @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
























    @Rule @JvmField
    val testName = TestName()
























    private lateinit var combos: List<TestCombo>

    private lateinit var context: Context


























    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context.setTheme(R.style.AppTheme)

        val testCombo1 = TestCombo(width = 480, height = 800, isDarkTheme = true, locale = Locale.ENGLISH)
        val testCombo2 = TestCombo(width = 720, height = 1280, isDarkTheme = false, locale = Locale("el"))

        combos = listOf(testCombo1, testCombo2)
    }








































    private fun updateConfiguration(locale: Locale, isDarkTheme: Boolean) {
        val configuration = context.resources.configuration

        configuration.setLocale(locale)

        configuration.uiMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()
        if (isDarkTheme) {
            configuration.uiMode = configuration.uiMode or Configuration.UI_MODE_NIGHT_YES
        } else {
            configuration.uiMode = configuration.uiMode or Configuration.UI_MODE_NIGHT_NO
        }

        context = context.createConfigurationContext(configuration)
    }
































    /*
        Current Time:                                            v
        Market Sessions:         |  [++++++++]                          [++++++]|
    */
























    @Test
    fun marketHoursView_2MarketSessions() {
        val marketSession1 = MarketHoursView.MarketHoursSession(
            marketOpenTime = MarketHoursView.DisplayedTime(hour = 1, minute = 0),
            marketCloseTime = MarketHoursView.DisplayedTime(hour = 5, minute = 0)
        )

        val marketSession2 = MarketHoursView.MarketHoursSession(
            marketOpenTime = MarketHoursView.DisplayedTime(hour = 21, minute = 0),
            marketCloseTime = MarketHoursView.DisplayedTime(hour = 24, minute = 0)
        )

        val currentTime = MarketHoursView.DisplayedTime(hour = 18, minute = 0)

        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            marketHoursSessions = listOf(marketSession1, marketSession2),
            currentTime = currentTime,
            isOpen = false
        )

















        for (combo in combos) {
            updateConfiguration(locale = combo.locale, isDarkTheme = combo.isDarkTheme)

            val marketHoursView = MarketHoursView(context = context)
            marketHoursView.setMarketDisplayInfo(marketDisplayInfo = marketDisplayInfo)

            ViewHelpers.setupView(marketHoursView).setExactWidthDp(combo.width).layout()
            Screenshot.snap(marketHoursView).setName(testName.methodName + "_" + combo.toString()).record()
        }





















    }































    /*
    ./gradlew executeScreenshotTests -Pandroid.testInstrumentationRunnerArguments.package=com.xm.screenshot.testing -Precord
     */


























































    /*
        Current Time:                                    v
        Market Sessions:         |[++++++++++++++++++++++++++++++++++++++++++++]|
    */
    @Test
    @Ignore
    fun marketHoursView_zeroAndTwentyFourHourDisplayTimes_AndCurrentTime12() {
        val marketSession = MarketHoursView.MarketHoursSession(
            marketOpenTime = MarketHoursView.DisplayedTime(0, 0),
            marketCloseTime = MarketHoursView.DisplayedTime(24, 0)
        )

        val currentTime = MarketHoursView.DisplayedTime(12, 0)

        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            marketHoursSessions = listOf(marketSession),
            currentTime = currentTime,
            isOpen = true
        )

        for (combo in combos) {
            updateConfiguration(combo.locale, combo.isDarkTheme)

            val marketHoursView = MarketHoursView(context)
            marketHoursView.setMarketDisplayInfo(marketDisplayInfo)

            ViewHelpers.setupView(marketHoursView).setExactWidthDp(combo.width).layout()
            Screenshot.snap(marketHoursView).setName(testName.methodName + "_" + combo.toString()).record()
        }
    }

    /*
        Current Time:                         v
        Market Sessions:         |              [+++++++++++++++++++++++++]     |
    */
    @Test
    @Ignore
    fun marketHoursView_displayTimesWithMinuteOffsets() {
        val marketSession = MarketHoursView.MarketHoursSession(
            marketOpenTime = MarketHoursView.DisplayedTime(7, 30),
            marketCloseTime = MarketHoursView.DisplayedTime(21, 30)
        )

        val currentTime = MarketHoursView.DisplayedTime(6, 30)

        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            marketHoursSessions = listOf(marketSession),
            currentTime = currentTime,
            isOpen = false
        )

        for (combo in combos) {
            updateConfiguration(combo.locale, combo.isDarkTheme)

            val marketHoursView = MarketHoursView(context)
            marketHoursView.setMarketDisplayInfo(marketDisplayInfo)

            ViewHelpers.setupView(marketHoursView).setExactWidthDp(combo.width).layout()
            Screenshot.snap(marketHoursView).setName(testName.methodName + "_" + combo.toString()).record()
        }
    }

}
