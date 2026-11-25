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

data class RegimeSegment(val regimeName: String, val durationPercent: Float, val color: Int)

class RegimeTimelineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var segments: List<RegimeSegment> = emptyList()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#9CA3AF")
        textSize = 28f
    }

    private val rectF = RectF()

    init {
        textPaint.color = ContextCompat.getColor(context, R.color.text_secondary_dark)
    }

    fun setSegments(data: List<RegimeSegment>?) {
        segments = data ?: emptyList()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0 || h <= 0) return

        val barHeight = 24f
        val top = (h - barHeight) / 2f
        val bottom = top + barHeight

        if (segments.isEmpty()) {
            paint.color = Color.parseColor("#253148")
            rectF.set(0f, top, w, bottom)
            canvas.drawRoundRect(rectF, 12f, 12f, paint)
            return
        }

        var currentX = 0f
        val total = segments.sumOf { it.durationPercent.toDouble() }.toFloat().coerceAtLeast(1f)

        for (seg in segments) {
            val segWidth = (seg.durationPercent / total) * w
            rectF.set(currentX, top, currentX + segWidth, bottom)
            paint.color = seg.color
            canvas.drawRoundRect(rectF, 6f, 6f, paint)
            currentX += segWidth
        }
    }
}