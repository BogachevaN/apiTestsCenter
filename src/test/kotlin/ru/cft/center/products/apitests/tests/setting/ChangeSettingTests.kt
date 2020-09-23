package ru.cft.center.products.apitests.tests.setting

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.*
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.settings.ChangeSettingValueRequest
import ru.cft.center.products.apitests.dto.settings.CreateSettingRequest
import ru.cft.center.products.apitests.dto.settings.SettingOption
import ru.cft.center.products.apitests.restapi.RestApiSetting
import ru.cft.center.products.apitests.restapi.RestApiTenant

@Tag("api")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChangeSettingTests : TestBase() {
    private lateinit var settingNumberId: Any
    private lateinit var settingArrayId: Any
    private lateinit var tenantId: String
    private val productId = "apitest"

    @BeforeAll
    fun createGroup() {
        settingNumberId = RestApiSetting.createSetting(SPEC, CreateSettingRequest(productId, "number",
            "testapi_numberSetting", "Настройка с указанием числа", 120)).jsonPath().get("id")
        settingArrayId = RestApiSetting.createSetting(SPEC, CreateSettingRequest(productId, "array",
            "testapi_arraySetting", "Массив", arrayOf("fullname", "phone"), null,
            "Выберите нужные значения", listOf(SettingOption("fullname", "Полное имя"),
            SettingOption("phone", "Телефон"), SettingOption("email",
            "Электронный адрес")))).jsonPath().get("id")
        tenantId = RestApiTenant.getTenantByName(SPEC, "TestBank").jsonPath().get("id")
    }

    @Test
    fun changeSettingMustReturn204() {
        var response = RestApiSetting.changeSetting(SPEC,
            ChangeSettingValueRequest(125, null), settingNumberId)
        assertEquals(204, response.statusCode())
    }

    @Test
    fun changeNotFoundSettingMustReturn404() {
        var response = RestApiSetting.changeSetting(SPEC,
            ChangeSettingValueRequest(125, null), 999999999)
        assertEquals(404, response.statusCode())
    }

    @Test
    fun changeSettingWithIncorrectValueMustReturn400() {
        var response = RestApiSetting.changeSetting(SPEC,
            ChangeSettingValueRequest("fdhghdjhg", null), settingNumberId)
        assertEquals(400, response.statusCode())
        assertEquals("INVALID_SETTING", response.jsonPath().get("error"))
    }

    @Test
    fun changeSettingWithIncorrectValueForTypeMustReturn400() {
        var response = RestApiSetting.changeSetting(SPEC,
            ChangeSettingValueRequest("fdhghdjhg", null), settingArrayId)
        assertEquals(400, response.statusCode())
        assertEquals("INVALID_SETTING", response.jsonPath().get("error"))
    }

    @Test
    fun changeSettingWithTenantIdMustReturn204() {
        val response = RestApiSetting.changeSetting(SPEC,
            ChangeSettingValueRequest(345, tenantId), settingNumberId)
        assertEquals(204, response.statusCode())
    }

    @Test
    fun changeSettingWithNotFoundTenantIdMustReturn404() {
        val response = RestApiSetting.changeSetting(SPEC,
            ChangeSettingValueRequest(345, "3b46e6aa-406e-4290-a890-9bdabf3b1e1d"), settingNumberId)
        assertEquals(404, response.statusCode())
        assertEquals("TENANT_NOT_FOUND", response.jsonPath().get("error"))
    }

    @Test
    fun changeSettingWithIncorrectTenantIdMustReturn400() {
        val response = RestApiSetting.changeSetting(SPEC,
            ChangeSettingValueRequest(345, "12345"), settingNumberId)
        assertEquals(400, response.statusCode())
    }
}