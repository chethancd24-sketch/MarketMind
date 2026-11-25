package com.example.marketmind.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.example.marketmind.R

class RiskGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var riskProbability: Float = 0f // 0 to 100
    private var animatedProbability: Float = 0f
    private var isDataLoaded: Boolean = false

    private val arcBounds = RectF()
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    private var lowColor: Int = Color.parseColor("#10B981")
    private var moderateColor: Int = Color.parseColor("#F59E0B")
    private var highColor: Int = Color.parseColor("#EF4444")
    private var criticalColor: Int = Color.parseColor("#8B5CF6")
    private var trackColor: Int = Color.parseColor("#1E293B")
    private var textPrimaryColor: Int = Color.parseColor("#F9FAFB")
    private var textSecondaryColor: Int = Color.parseColor("#9CA3AF")

    private var animator: ValueAnimator? = null

    init {
        lowColor = ContextCompat.getColor(context, R.color.risk_low)
        moderateColor = ContextCompat.getColor(context, R.color.risk_moderate)
        highColor = ContextCompat.getColor(context, R.color.risk_high)
        criticalColor = ContextCompat.getColor(context, R.color.risk_critical)
        trackColor = ContextCompat.getColor(context, R.color.stroke_dark)
        textPrimaryColor = ContextCompat.getColor(context, R.color.text_primary_dark)
        textSecondaryColor = ContextCompat.getColor(context, R.color.text_secondary_dark)
    }

    fun setRiskProbability(probability: Float?, animate: Boolean = true) {
        if (probability == null) {
            isDataLoaded = false
            invalidate()
            return
        }
        val target = probability.coerceIn(0f, 100f)
        isDataLoaded = true
        riskProbability = target

        animator?.cancel()
        if (animate) {
            animator = ValueAnimator.ofFloat(animatedProbability, target).apply {
                duration = 800
                interpolator = DecelerateInterpolator()
                addUpdateListener { anim ->
                    animatedProbability = anim.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            animatedProbability = target
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        if (width <= 0 || height <= 0) return

        val strokeWidth = width * 0.08f
        val padding = strokeWidth / 2f + 16f
        arcBounds.set(padding, padding, width - padding, height * 1.3f - padding)

        val startAngle = 180f
        val sweepAngle = 180f

        // Draw Background Track
        strokePaint.color = trackColor
        strokePaint.strokeWidth = strokeWidth
        canvas.drawArc(arcBounds, startAngle, sweepAngle, false, strokePaint)

        if (!isDataLoaded) {
            textPaint.color = textSecondaryColor
            textPaint.textSize = width * 0.08f
            canvas.drawText(context.getString(R.string.risk_no_data), width / 2f, height * 0.65f, textPaint)
            return
        }

        // Color according to risk category
        val currentColor = when {
            animatedProbability < 25f -> lowColor
            animatedProbability < 50f -> moderateColor
            animatedProbability < 75f -> highColor
            else -> criticalColor
        }

        // Draw Filled Arc
        strokePaint.color = currentColor
        val currentSweep = (animatedProbability / 100f) * sweepAngle
        canvas.drawArc(arcBounds, startAngle, currentSweep, false, strokePaint)

        // Draw Text inside Gauge
        textPaint.color = textPrimaryColor
        textPaint.textSize = width * 0.16f
        val valueText = String.format(java.util.Locale.US, "%.1f%%", animatedProbability)
        canvas.drawText(valueText, width / 2f, height * 0.60f, textPaint)

        labelPaint.color = currentColor
        labelPaint.textSize = width * 0.07f
        labelPaint.isFakeBoldText = true
        val categoryLabel = when {
            animatedProbability < 25f -> context.getString(R.string.risk_level_low)
            animatedProbability < 50f -> context.getString(R.string.risk_level_moderate)
            animatedProbability < 75f -> context.getString(R.string.risk_level_high)
            else -> context.getString(R.string.risk_level_critical)
        }
        canvas.drawText(categoryLabel, width / 2f, height * 0.76f, labelPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}