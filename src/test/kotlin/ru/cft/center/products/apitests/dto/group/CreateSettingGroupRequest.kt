package ru.cft.center.products.apitests.dto.group

class CreateSettingGroupRequest(
    val productId: String?,
    val name: String?,
    val title: String?,
    val parentId: Any? = null,
    val description: String? = null
)
