package com.example.marketmind.ui.portfolio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketmind.data.repository.MarketRepository
import com.example.marketmind.data.repository.MarketRepositoryImpl
import com.example.marketmind.domain.model.PortfolioRecommendation
import com.example.marketmind.ui.common.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MarketRepository = MarketRepositoryImpl(application)

    private val _portfolioState = MutableStateFlow<UiState<PortfolioRecommendation>>(UiState.Loading)
    val portfolioState: StateFlow<UiState<PortfolioRecommendation>> = _portfolioState.asStateFlow()

    private val _isRationaleExpanded = MutableStateFlow(false)
    val isRationaleExpanded: StateFlow<Boolean> = _isRationaleExpanded.asStateFlow()

    private var refreshJob: Job? = null

    init {
        loadData()
    }

    fun refreshData() {
        if (refreshJob?.isActive == true) return
        loadData()
    }

    fun toggleRationaleExpanded() {
        _isRationaleExpanded.value = !_isRationaleExpanded.value
    }

    private fun loadData() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            repository.getPortfolioRecommendation().collect { state ->
                _portfolioState.value = state
            }
        }
    }
}