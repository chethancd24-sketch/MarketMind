package com.example.marketmind.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.marketmind.R
import java.util.Locale

data class AllocationSlice(val name: String, val percentage: Float, val color: Int)

class AllocationDonutChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var slices: List<AllocationSlice> = emptyList()
    private var chartTitle: String = ""

    private val slicePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val holePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private val rectF = RectF()

    init {
        holePaint.color = ContextCompat.getColor(context, R.color.surface_card_dark)
        textPaint.color = ContextCompat.getColor(context, R.color.text_primary_dark)
    }

    fun setData(slicesList: List<AllocationSlice>?, title: String = "") {
        slices = slicesList ?: emptyList()
        chartTitle = title
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0 || h <= 0) return

        val cx = w / 2f
        val cy = h / 2f
        val radius = (w.coerceAtMost(h) / 2f) * 0.85f
        rectF.set(cx - radius, cy - radius, cx + radius, cy + radius)

        if (slices.isEmpty()) {
            slicePaint.color = Color.parseColor("#253148")
            canvas.drawCircle(cx, cy, radius, slicePaint)
            canvas.drawCircle(cx, cy, radius * 0.65f, holePaint)
            textPaint.textSize = radius * 0.2f
            canvas.drawText("No Allocation", cx, cy + 10f, textPaint)
            return
        }

        var startAngle = -90f
        val total = slices.sumOf { it.percentage.toDouble() }.toFloat().coerceAtLeast(1f)

        for (slice in slices) {
            val sweepAngle = (slice.percentage / total) * 360f
            slicePaint.color = slice.color
            canvas.drawArc(rectF, startAngle, sweepAngle, true, slicePaint)
            startAngle += sweepAngle
        }

        // Inner Hole for Donut effect
        canvas.drawCircle(cx, cy, radius * 0.62f, holePaint)

        // Center Title
        if (chartTitle.isNotEmpty()) {
            textPaint.textSize = radius * 0.22f
            textPaint.color = ContextCompat.getColor(context, R.color.text_primary_dark)
            canvas.drawText(chartTitle, cx, cy + 10f, textPaint)
        }
    }
}