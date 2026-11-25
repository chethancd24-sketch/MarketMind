package com.example.marketmind.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.marketmind.R

data class PerformancePoint(val timestamp: Long, val portfolioReturn: Float, val benchmarkReturn: Float)

class PerformanceChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var points: List<PerformancePoint> = emptyList()

    private val portfolioPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.parseColor("#3B82F6") // Accent Blue
    }
    private val benchmarkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.parseColor("#9CA3AF") // Secondary Grey
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.parseColor("#253148")
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#9CA3AF")
        textSize = 28f
    }

    private val pathPortfolio = Path()
    private val pathBenchmark = Path()

    init {
        portfolioPaint.color = ContextCompat.getColor(context, R.color.accent_blue)
        gridPaint.color = ContextCompat.getColor(context, R.color.stroke_dark)
        textPaint.color = ContextCompat.getColor(context, R.color.text_secondary_dark)
    }

    fun setData(data: List<PerformancePoint>?) {
        points = data ?: emptyList()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0 || h <= 0) return

        val paddingLeft = 90f
        val paddingRight = 40f
        val paddingTop = 40f
        val paddingBottom = 60f

        val chartWidth = w - paddingLeft - paddingRight
        val chartHeight = h - paddingTop - paddingBottom

        if (points.isEmpty()) {
            val emptyMsg = context.getString(R.string.analytics_empty)
            textPaint.textAlign = Paint.Align.CENTER
            canvas.drawText(emptyMsg, w / 2f, h / 2f, textPaint)
            textPaint.textAlign = Paint.Align.LEFT
            return
        }

        val maxVal = points.flatMap { listOf(it.portfolioReturn, it.benchmarkReturn) }.maxOrNull()?.coerceAtLeast(10f) ?: 10f
        val minVal = points.flatMap { listOf(it.portfolioReturn, it.benchmarkReturn) }.minOrNull()?.coerceAtMost(-10f) ?: -10f
        val range = (maxVal - minVal).coerceAtLeast(1f)

        // Draw Zero Line if in range
        if (minVal <= 0f && maxVal >= 0f) {
            val zeroY = paddingTop + chartHeight * (1f - (0f - minVal) / range)
            canvas.drawLine(paddingLeft, zeroY, w - paddingRight, zeroY, gridPaint)
        }

        pathPortfolio.reset()
        pathBenchmark.reset()

        val step = chartWidth / (points.size - 1).coerceAtLeast(1)

        for (i in points.indices) {
            val pt = points[i]
            val x = paddingLeft + i * step
            val yPort = paddingTop + chartHeight * (1f - (pt.portfolioReturn - minVal) / range)
            val yBench = paddingTop + chartHeight * (1f - (pt.benchmarkReturn - minVal) / range)

            if (i == 0) {
                pathPortfolio.moveTo(x, yPort)
                pathBenchmark.moveTo(x, yBench)
            } else {
                pathPortfolio.lineTo(x, yPort)
                pathBenchmark.lineTo(x, yBench)
            }
        }

        canvas.drawPath(pathBenchmark, benchmarkPaint)
        canvas.drawPath(pathPortfolio, portfolioPaint)
    }
}