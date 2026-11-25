package com.example.marketmind.data.repository

import com.example.marketmind.domain.model.*
import com.example.marketmind.ui.common.UiState
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    fun getRiskData(): Flow<UiState<RiskData>>
    fun getRegimeData(): Flow<UiState<RegimeData>>
    fun getMarketOverview(): Flow<UiState<List<MarketAsset>>>
    fun getPortfolioRecommendation(): Flow<UiState<PortfolioRecommendation>>
    fun getAnalytics(): Flow<UiState<AnalyticsData>>
    suspend fun testConnection(baseUrl: String): Pair<Boolean, String>
}