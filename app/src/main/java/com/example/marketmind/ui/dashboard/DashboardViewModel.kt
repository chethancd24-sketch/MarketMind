package com.example.marketmind.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketmind.data.repository.MarketRepository
import com.example.marketmind.data.repository.MarketRepositoryImpl
import com.example.marketmind.domain.model.MarketAsset
import com.example.marketmind.domain.model.RegimeData
import com.example.marketmind.domain.model.RiskData
import com.example.marketmind.ui.common.NetworkMonitor
import com.example.marketmind.ui.common.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MarketRepository = MarketRepositoryImpl(application)
    private val networkMonitor = NetworkMonitor(application)

    private val _riskState = MutableStateFlow<UiState<RiskData>>(UiState.Loading)
    val riskState: StateFlow<UiState<RiskData>> = _riskState.asStateFlow()

    private val _regimeState = MutableStateFlow<UiState<RegimeData>>(UiState.Loading)
    val regimeState: StateFlow<UiState<RegimeData>> = _regimeState.asStateFlow()

    private val _overviewState = MutableStateFlow<UiState<List<MarketAsset>>>(UiState.Loading)
    val overviewState: StateFlow<UiState<List<MarketAsset>>> = _overviewState.asStateFlow()

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), networkMonitor.isCurrentlyOnline())

    private var refreshJob: Job? = null
    private var lastRefreshTime = 0L

    init {
        loadData()
    }

    fun refreshData() {
        // Debounce fast repeated taps (1 second minimum)
        val now = System.currentTimeMillis()
        if (now - lastRefreshTime < 1000 || refreshJob?.isActive == true) return
        lastRefreshTime = now
        loadData()
    }

    private fun loadData() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            // Launch parallel loads
            launch { repository.getRiskData().collect { _riskState.value = it } }
            launch { repository.getRegimeData().collect { _regimeState.value = it } }
            launch { repository.getMarketOverview().collect { _overviewState.value = it } }
        }
    }
}