package com.example.marketmind.ui.common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {

    private val indianSymbols = DecimalFormatSymbols(Locale("en", "IN"))
    private val currencyFormat = DecimalFormat("##,##,##0.00", indianSymbols)
    private val percentFormat = DecimalFormat("+0.00%;-0.00%", indianSymbols)
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())

    fun formatPrice(value: Double?): String {
        if (value == null) return "—"
        return "₹" + currencyFormat.format(value)
    }

    fun formatIndex(value: Double?): String {
        if (value == null) return "—"
        return currencyFormat.format(value)
    }

    fun formatPercent(value: Double?): String {
        if (value == null) return "0.00%"
        val pct = value / 100.0
        return percentFormat.format(pct)
    }

    fun formatTimestamp(timestampMs: Long?): String {
        if (timestampMs == null || timestampMs <= 0) {
            return timeFormat.format(Date())
        }
        return timeFormat.format(Date(timestampMs))
    }

    fun formatDate(timestampMs: Long?): String {
        if (timestampMs == null || timestampMs <= 0) return "—"
        return dateFormat.format(Date(timestampMs))
    }
}