package ru.cft.center.products.apitests.dto.settings

import com.fasterxml.jackson.annotation.JsonProperty

enum class SettingType {
    @JsonProperty("boolean")
    Boolean,

    @JsonProperty("number")
    Number,

    @JsonProperty("string")
    String,

    @JsonProperty("date")
    Date,

    @JsonProperty("enum")
    Enum,

    @JsonProperty("array")
    Array
}