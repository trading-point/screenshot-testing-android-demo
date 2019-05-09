package com.xm

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.market_hours_view.view.*
import java.util.concurrent.TimeUnit





















class MarketHoursView @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    data class DisplayedTime(val hour: Int, val minute: Int)

    data class MarketHoursSession(val marketOpenTime: DisplayedTime,
                                  val marketCloseTime: DisplayedTime)

    data class MarketDisplayInfo(val marketHoursSessions: List<MarketHoursSession>,
                                 val currentTime: DisplayedTime,
                                 val isOpen: Boolean)

































    private fun timeToMinutes(hour: Int): Float {
        return timeToMinutes(hour, 0)
    }

    /**
     * A method to convert hours an minutes to minutes
     */
    private fun timeToMinutes(hour: Int, minute: Int): Float {
        return (TimeUnit.HOURS.toMinutes(hour.toLong()) + minute).toFloat()
    }





































    /**
     * A method to normalize the given time inside the range of a specific 2-hour block
     * To do that for a given range [a,b] with x values we use this formula:
     * n = (b-a) * ((x(i) - x(min)) / (x(max) - x(min))) + a
     */
    private fun normalizeTime(hour: Int, minute: Int, hourCoordinatesRange: CoordinatesRange): Float {
        val timeInMinutes = timeToMinutes(hour, minute)

        val maxHour = getRangeMaxHour(hour)
        val minHour = getRangeMinHour(hour)

        val normalized = (timeInMinutes - timeToMinutes(minHour)) / (timeToMinutes(maxHour) - timeToMinutes(minHour))

        return (normalized * (hourCoordinatesRange.end - hourCoordinatesRange.start)) + hourCoordinatesRange.start
    }



























    data class CoordinatesRange(val start: Float, val end: Float) {
        constructor(start: Int, end: Int) : this(start.toFloat(), end.toFloat())
    }

    companion object {
        private const val HOURS_PER_BLOCK = 2
    }

    private val currentLinePaint: Paint = Paint()

    private val tineRangePaint: Paint = Paint()

    private lateinit var marketDisplayInfo: MarketDisplayInfo

    init {
        View.inflate(context, R.layout.market_hours_view, this)
        // By default drawing for this view is disabled so we have to clear this flag in order to draw
        setWillNotDraw(false)

        currentLinePaint.color = ContextCompat.getColor(context, R.color.lineColor)
        currentLinePaint.isAntiAlias = true
        currentLinePaint.style = Paint.Style.STROKE
        currentLinePaint.strokeWidth = context.resources.getDimensionPixelSize(R.dimen.market_hours_current_time_line_width).toFloat()

        tineRangePaint.color = ContextCompat.getColor(context, R.color.green)
        tineRangePaint.isAntiAlias = true
        tineRangePaint.style = Paint.Style.FILL
        tineRangePaint.strokeWidth = context.resources.getDimensionPixelSize(R.dimen.market_hours_time_bar_height).toFloat()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        drawTimeRanges(canvas)
        drawCurrentTimeLine(canvas)
    }

    fun setMarketDisplayInfo(marketDisplayInfo: MarketDisplayInfo) {
        this.marketDisplayInfo = marketDisplayInfo
        //after setting the time ranges apply any changes needed to the the market state text
        setUpForMarketStatus(marketDisplayInfo.isOpen)
        //invalidate in order to force redraw which will fire drawing the time ranges
        invalidate()
    }

    private fun setUpForMarketStatus(isOpen: Boolean) {
        if (isOpen) {
            market_state.text = context.getString(R.string.details_info_market_hours_open)
            market_state.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        } else {
            market_state.text = context.getString(R.string.details_info_market_hours_closed)
            market_state.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
        }
    }

    private fun drawTimeRanges(canvas: Canvas) {
        // Line's width will expand from the specified point upwards and downwards
        // So we need to shift the center of it to the center of the time bar view
        val barPosition = time_bar.top.toFloat() + (time_bar.height / 2)

        marketDisplayInfo.marketHoursSessions.forEach {
            val lineRange = getDisplayedTimeRange(it)
            canvas.drawLine(lineRange.start, barPosition, lineRange.end, barPosition, tineRangePaint)
        }
    }

    private fun drawCurrentTimeLine(canvas: Canvas) {
        // find the time block where the current time line should be drawn and normalize the value
        val normalizedCurrentTime = normalizeTime(marketDisplayInfo.currentTime.hour,
            marketDisplayInfo.currentTime.minute,
            findHourViewsRange(marketDisplayInfo.currentTime.hour))
        // calculate the exact x position
        val lineXPosition = when (marketDisplayInfo.currentTime.hour) {
            0, 24 -> normalizedCurrentTime
            else -> normalizedCurrentTime + (currentLinePaint.strokeWidth / 2)     //center line to the time line point
        }

        //calculate how much the line should expand above and below the time bar
        val height = (context.resources.getDimensionPixelSize(R.dimen.market_hours_current_time_line_height).toFloat() - time_bar.height) / 2

        val lineYStartPosition = time_bar.top.toFloat() - height
        val lineYEndPosition = time_bar.bottom.toFloat() + height

        canvas.drawLine(lineXPosition, lineYStartPosition, lineXPosition, lineYEndPosition, currentLinePaint)
    }

    private fun getDisplayedTimeRange(marketHoursSession: MarketHoursSession): CoordinatesRange {

        val lineStart = normalizeTime(marketHoursSession.marketOpenTime.hour,
            marketHoursSession.marketOpenTime.minute,
            findHourViewsRange(marketHoursSession.marketOpenTime.hour))

        val lineEnd = normalizeTime(marketHoursSession.marketCloseTime.hour,
            marketHoursSession.marketCloseTime.minute,
            findHourViewsRange(marketHoursSession.marketCloseTime.hour))

        return CoordinatesRange(lineStart, lineEnd)
    }

    /**
     * A method to find the range where the given hour value is
     * Every block should start and finish at the middle of the hour numbers except
     * the first and last value where the block should start/finish at the start/end of th view
     */
    private fun findHourViewsRange(hour: Int): CoordinatesRange {
        val minHour = getRangeMinHour(hour)
        val maxHour = getRangeMaxHour(hour)

        val leftHourView = findViewById<TextView>(getHourViewId(minHour))
        val rightHourView = findViewById<TextView>(getHourViewId(maxHour))

        return when {
            minHour == 0 -> CoordinatesRange(leftHourView.left, rightHourView.left + (rightHourView.width / 2))
            maxHour == 24 -> CoordinatesRange(leftHourView.left + (leftHourView.width / 2), rightHourView.right)
            else -> CoordinatesRange(leftHourView.left + (leftHourView.width / 2), rightHourView.left + (rightHourView.width / 2))
        }
    }

    //find the smallest hour in the surrounding HOURS_PER_BLOCK block
    private fun getRangeMinHour(hour: Int): Int {
        return when {
            hour == 24 -> hour - HOURS_PER_BLOCK       //if it is the max possible hour then subtract by HOURS_PER_BLOCK
            (hour % HOURS_PER_BLOCK) == 0 -> hour      //this means that the value is already the minimum
            else -> Math.max(hour - (hour % HOURS_PER_BLOCK), 0)
        }
    }

    //find the biggest hour in the surrounding HOURS_PER_BLOCK block
    private fun getRangeMaxHour(hour: Int): Int {
        return when {
            (hour % HOURS_PER_BLOCK) == 0 -> Math.min(hour + HOURS_PER_BLOCK, 24)
            else -> Math.min(hour + (hour % HOURS_PER_BLOCK), 24)
        }
    }

    private fun getHourViewId(hour: Int): Int {
        return context.resources.getIdentifier("hour_$hour", "id", context.packageName)
    }
}