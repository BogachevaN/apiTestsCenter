package ru.cft.center.products.apitests.dto.settings

data class ImportSettingsRequest(
    val settings: List<Settings>,
    val productId: String? = null
)