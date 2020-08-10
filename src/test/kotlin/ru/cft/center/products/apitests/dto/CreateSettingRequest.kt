package ru.cft.center.products.apitests.dto

data class CreateSettingRequest(
    val productId: String?,
    val type: String?,
    val name: String?,
    val title: String?,
    val value: Any?,
    val groupId: Any? = null,
    val description: String? = null,
    val options: List<SettingOption>? = null
)
