package ru.cft.center.products.apitests.dto.tenant

data class CreateTenantRequest(
    val name: String? = null,
    val title: String? = null,
    val esiaMnemonic: String? = null
)