package com.example.marketmind.ui.common

import android.view.HapticFeedbackConstants
import android.view.View

object HapticFeedbackUtil {
    fun performClickFeedback(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }

    fun performRefreshFeedback(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}