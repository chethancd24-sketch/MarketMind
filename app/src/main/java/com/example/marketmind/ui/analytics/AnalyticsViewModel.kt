package com.example.marketmind.ui.analytics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketmind.data.repository.MarketRepository
import com.example.marketmind.data.repository.MarketRepositoryImpl
import com.example.marketmind.domain.model.AnalyticsData
import com.example.marketmind.ui.common.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MarketRepository = MarketRepositoryImpl(application)

    private val _analyticsState = MutableStateFlow<UiState<AnalyticsData>>(UiState.Loading)
    val analyticsState: StateFlow<UiState<AnalyticsData>> = _analyticsState.asStateFlow()

    private val _selectedPeriod = MutableStateFlow("1M")
    val selectedPeriod: StateFlow<String> = _selectedPeriod.asStateFlow()

    private var refreshJob: Job? = null

    init {
        loadData()
    }

    fun refreshData() {
        if (refreshJob?.isActive == true) return
        loadData()
    }

    fun selectPeriod(period: String) {
        _selectedPeriod.value = period
        // Future: could filter chart points by period if provided by backend
    }

    private fun loadData() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            repository.getAnalytics().collect { state ->
                _analyticsState.value = state
            }
        }
    }
}