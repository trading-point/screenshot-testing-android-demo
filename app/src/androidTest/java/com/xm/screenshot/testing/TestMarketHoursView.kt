package com.xm.screenshot.testing

import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.xm.MarketHoursView
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

/**
 * Created by Christoforos Filippou on 30/03/2019
 */
@Ignore
class TestMarketHoursView : ScreenshotTestBase() {

    private lateinit var combos: List<TestDevice>

    @Before
    override fun setUp() {
        super.setUp()

        combos = getViewConfigCombos(WRAP_CONTENT)
    }

    @Test
    fun marketHoursView_evenHourDisplayTimes() {
        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            listOf(
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(6, 0),
                    MarketHoursView.DisplayedTime(20, 0)
                )
            ),
            MarketHoursView.DisplayedTime(14, 0),
            true
        )

        for (combo in combos) {
            updateResources(combo.locale, combo.isDarkTheme)
            createViewAndRecord(marketDisplayInfo, combo)
        }
    }

    @Test
    fun marketHoursView_oddHourDisplayTimes() {
        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            listOf(
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(7, 0),
                    MarketHoursView.DisplayedTime(21, 0)
                )
            ),
            MarketHoursView.DisplayedTime(15, 0),
            true
        )

        for (combo in combos) {
            updateResources(combo.locale, combo.isDarkTheme)
            createViewAndRecord(marketDisplayInfo, combo)
        }
    }

    @Test
    fun marketHoursView_zeroAndTwentyFourHourDisplayTimes_AndCurrentTimeZero() {
        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            listOf(
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(0, 0),
                    MarketHoursView.DisplayedTime(24, 0)
                )
            ),
            MarketHoursView.DisplayedTime(0, 0),
            true
        )

        for (combo in combos) {
            updateResources(combo.locale, combo.isDarkTheme)
            createViewAndRecord(marketDisplayInfo, combo)
        }
    }

    @Test
    fun marketHoursView_zeroAndTwentyFourHourDisplayTimes_AndCurrentTime24() {
        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            listOf(
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(0, 0),
                    MarketHoursView.DisplayedTime(24, 0)
                )
            ),
            MarketHoursView.DisplayedTime(24, 0),
            true
        )

        for (combo in combos) {
            updateResources(combo.locale, combo.isDarkTheme)
            createViewAndRecord(marketDisplayInfo, combo)
        }
    }

    @Test
    fun marketHoursView_oddHourDisplaytimes_WhenCloseToEdgeValues() {
        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            listOf(
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(1, 0),
                    MarketHoursView.DisplayedTime(23, 0)
                )
            ),
            MarketHoursView.DisplayedTime(1, 0),
            true
        )

        for (combo in combos) {
            updateResources(combo.locale, combo.isDarkTheme)
            createViewAndRecord(marketDisplayInfo, combo)
        }
    }

    @Test
    fun marketHoursView_displayTimesWithMinuteOffsets() {
        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            listOf(
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(7, 30),
                    MarketHoursView.DisplayedTime(21, 30)
                )
            ),
            MarketHoursView.DisplayedTime(6, 30),
            false
        )

        for (combo in combos) {
            updateResources(combo.locale, combo.isDarkTheme)
            createViewAndRecord(marketDisplayInfo, combo)
        }
    }

    @Test
    fun marketHoursView_2MarketSessions() {
        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            listOf(
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(1, 0),
                    MarketHoursView.DisplayedTime(5, 0)
                ),
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(21, 0),
                    MarketHoursView.DisplayedTime(24, 0)
                )
            ),
            MarketHoursView.DisplayedTime(17, 0),
            false
        )

        for (combo in combos) {
            updateResources(combo.locale, combo.isDarkTheme)
            createViewAndRecord(marketDisplayInfo, combo)
        }
    }

    @Test
    fun marketHoursView_multipleMarketSessions() {
        val marketDisplayInfo = MarketHoursView.MarketDisplayInfo(
            listOf(
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(1, 0),
                    MarketHoursView.DisplayedTime(5, 0)
                ),
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(7, 0),
                    MarketHoursView.DisplayedTime(18, 0)
                ),
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(19, 0),
                    MarketHoursView.DisplayedTime(20, 30)
                ),
                MarketHoursView.MarketHoursSession(
                    MarketHoursView.DisplayedTime(21, 0),
                    MarketHoursView.DisplayedTime(24, 0)
                )
            ),
            MarketHoursView.DisplayedTime(18, 30),
            false
        )

        for (combo in combos) {
            updateResources(combo.locale, combo.isDarkTheme)
            createViewAndRecord(marketDisplayInfo, combo)
        }
    }

    private fun createViewAndRecord(marketDisplayInfo: MarketHoursView.MarketDisplayInfo, device: TestDevice) {
        val marketHoursView = MarketHoursView(context)
        marketHoursView.setMarketDisplayInfo(marketDisplayInfo)
        record(marketHoursView, device)
    }
}
