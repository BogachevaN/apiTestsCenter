package ru.cft.center.products.apitests.tests.setting

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.ChangeSettingValueRequest
import ru.cft.center.products.apitests.dto.CreateSettingRequest
import ru.cft.center.products.apitests.dto.SettingOption
import ru.cft.center.products.apitests.restapi.RestApiSetting

@Tag("api")
class ChangeSettingTests : TestBase() {
    lateinit var settingNumberId: Any
    lateinit var settingArrayId: Any

    @BeforeEach
    fun createGroup() {
        settingNumberId = RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "number",
            "testapi_numberSetting", "Настройка с указанием числа", 120)).jsonPath().get("id")
        settingArrayId = RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "array",
            "testapi_arraySetting", "Массив", arrayOf("fullname", "phone"), null,
            "Выберите нужные значения", listOf(SettingOption("fullname", "Полное имя"),
            SettingOption("phone", "Телефон"), SettingOption("email",
            "Электронный адрес")))).jsonPath().get("id")
    }

    @Test
    fun changeSettingMustReturn204() {
        var response = RestApiSetting.changeSetting(SPEC, ChangeSettingValueRequest(125), settingNumberId)
        assertEquals(204, response.statusCode())
    }

    @Test
    fun changeNotFoundSettingMustReturn404() {
        var response = RestApiSetting.changeSetting(SPEC, ChangeSettingValueRequest(125), 999999999)
        assertEquals(404, response.statusCode())
    }

    @Test
    fun changeSettingWithIncorrectValueMustReturn400() {
        var response = RestApiSetting.changeSetting(SPEC, ChangeSettingValueRequest("fdhghdjhg"), settingNumberId)
        assertEquals(400, response.statusCode())
        assertEquals("INVALID_SETTING", response.jsonPath().get("error"))
    }

    @Test
    fun changeSettingWithIncorrectValueForTypeMustReturn400() {
        var response = RestApiSetting.changeSetting(SPEC, ChangeSettingValueRequest("fdhghdjhg"), settingArrayId)
        assertEquals(400, response.statusCode())
        assertEquals("INVALID_SETTING", response.jsonPath().get("error"))
    }
}
