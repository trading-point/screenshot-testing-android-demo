package com.xm.screenshot.testing

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.ViewHelpers
import com.xm.MarketHoursView
import com.xm.MarketHoursView.*
import org.junit.Rule
import org.junit.Test


class TestDemo {





























    /**
     * Allows of granting runtime permissions for Android M (23) and above.
     * Useful for avoiding permission dialogs in UI-tests
     */
    @Rule @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(WRITE_EXTERNAL_STORAGE)























































    /*
        Current Time:                                                   v
        Market Sessions:         |  [++++++++]                          [++++++]|
    */
    @Test
    fun marketHoursView_2MarketSessions() {
        // create the first market session
        val marketSession1 = MarketHoursSession(
            marketOpenTime = DisplayedTime(hour = 1, minute = 0),
            marketCloseTime = DisplayedTime(hour = 5, minute = 0)
        )

        // create the second market session
        val marketSession2 = MarketHoursSession(
            marketOpenTime = DisplayedTime(hour = 21, minute = 0),
            marketCloseTime = DisplayedTime(hour = 24, minute = 0)
        )

        val currentTime = DisplayedTime(hour = 21, minute = 0)

        val marketDisplayInfo = MarketDisplayInfo(
            marketHoursSessions = listOf(marketSession1, marketSession2),
            currentTime = currentTime,
            isOpen = false
        )












        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  v

        // create the view
        val marketHoursView = MarketHoursView(context = InstrumentationRegistry.getInstrumentation().targetContext)

        // provide the data to the view
        marketHoursView.setMarketDisplayInfo(marketDisplayInfo = marketDisplayInfo)

        // inflate the view
        ViewHelpers.setupView(marketHoursView).setExactWidthDp(1080).layout()

        // Finally! take a screenshot
        Screenshot.snap(marketHoursView).record()

        //                  ^
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |
        //                  |





















    }































    /**
     * ./gradlew executeScreenshotTests -Pandroid.testInstrumentationRunnerArguments.package=com.xm.screenshot.testing -Precord
     */


























































}
