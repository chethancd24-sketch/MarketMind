package com.example.marketmind.ui.common

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T, val isStale: Boolean = false, val asOfTimestamp: String? = null) : UiState<T>()
    object Empty : UiState<Nothing>()
    data class Error(val type: ErrorType, val message: String, val cachedData: Any? = null) : UiState<Nothing>()
}