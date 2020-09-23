package ru.cft.center.products.apitests.dto.settings

data class Settings(
    var id: Int? = null,
    var name: String? = null,
    val title: String? = null,
    val description: String? = null,
    val items: List<Items>
) {
    data class Items(
        var id: Int? = null,
        var name: String? = null,
        val title: String? = null,
        val description: String? = null,
        val field: Field
    )

    data class Field(
        val type: SettingType,
        val value: Any,
        val options: List<Option>? = null,
        val overrides: List<Tenant>? = null
    ) {
        data class Option(
            val value: String,
            val description: String? = null
        )
        data class Tenant(
            val tenantId: String? = null,
            val value: Any
        )
    }
}