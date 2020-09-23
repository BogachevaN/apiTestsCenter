package ru.cft.center.products.apitests.dto.settings

data class ChangeSettingValueRequest(
    val value: Any,
    val tenantId: Any?
)
