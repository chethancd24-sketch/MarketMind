package com.example.marketmind.ui.common

enum class ErrorType {
    NO_INTERNET,
    TIMEOUT,
    BACKEND_UNAVAILABLE, // 5xx or connection refused
    API_ERROR,           // 4xx with status code/message
    PARSE_ERROR,         // unexpected JSON format
    EMPTY                // no data returned
}