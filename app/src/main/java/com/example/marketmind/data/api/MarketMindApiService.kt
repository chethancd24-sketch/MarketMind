package com.example.marketmind.data.api

import com.example.marketmind.data.dto.*
import retrofit2.Response
import retrofit2.http.GET

interface MarketMindApiService {

    @GET("health")
    suspend fun getHealth(): Response<HealthDto>

    @GET("predict-risk")
    suspend fun getPredictRisk(): Response<RiskDto>

    @GET("market-regime")
    suspend fun getMarketRegime(): Response<RegimeDto>

    @GET("portfolio/recommend")
    suspend fun getPortfolioRecommend(): Response<PortfolioRecommendationDto>

    @GET("market-overview")
    suspend fun getMarketOverview(): Response<MarketOverviewDto>

    @GET("analytics")
    suspend fun getAnalytics(): Response<AnalyticsDto>
}