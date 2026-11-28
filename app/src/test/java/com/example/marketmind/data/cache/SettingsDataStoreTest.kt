package com.example.marketmind.data.cache

import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsDataStoreTest {

    @Test
    fun resolveBaseUrl_usesUsbForwardingForPhysicalDebugDevice() {
        assertEquals(
            SettingsDataStore.USB_REVERSED_BASE_URL,
            SettingsDataStore.resolveBaseUrl(
                savedUrl = SettingsDataStore.DEFAULT_BASE_URL,
                isDebuggable = true,
                isEmulator = false
            )
        )
    }

    @Test
    fun resolveBaseUrl_keepsEmulatorDefaultOnEmulator() {
        assertEquals(
            SettingsDataStore.DEFAULT_BASE_URL,
            SettingsDataStore.resolveBaseUrl(
                savedUrl = SettingsDataStore.DEFAULT_BASE_URL,
                isDebuggable = true,
                isEmulator = true
            )
        )
    }

    @Test
    fun resolveBaseUrl_preservesCustomUrl() {
        val customUrl = "http://192.168.1.20:8000/"
        assertEquals(
            customUrl,
            SettingsDataStore.resolveBaseUrl(
                savedUrl = customUrl,
                isDebuggable = true,
                isEmulator = false
            )
        )
    }
}
