package ru.cft.center.products.apitests.tests.setting

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.group.CreateSettingGroupRequest
import ru.cft.center.products.apitests.dto.settings.CreateSettingRequest
import ru.cft.center.products.apitests.restapi.RestApiSetting
import ru.cft.center.products.apitests.restapi.RestApiSettingGroup
import ru.cft.center.products.apitests.restapi.RestApiTenant

@Tag("api")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadSettingsTests : TestBase() {

    private var groupId: Int = 0
    private lateinit var settingId: Any
    private lateinit var settingName: String
    private lateinit var tenantId: String
    private val productId = "apitest"

    @BeforeAll
    fun createGroup() {
        settingName = "testapi_stringSetting" + getRandomString(5)
        groupId = RestApiSettingGroup
            .createSettingGroup(SPEC, CreateSettingGroupRequest(productId, "firstGroupOfFirstLevel",
                "testapi_Тестовая настройка", null, "Тестовая группа первого уровня"))
            .jsonPath().get("id")
        settingId = RestApiSetting.createSetting(SPEC, CreateSettingRequest(productId, "string",
            settingName, "Строка", "Строка", groupId, "Настройка с указанием строки"))
            .jsonPath().get("id")
        tenantId = RestApiTenant.getTenantByName(SPEC, "TestBank").jsonPath().get("id")
    }

    @TestFactory
    fun readSettingsMustReturn200(): List<DynamicTest> {
        val settingGroups = listOf(RestApiSetting.readSetting(SPEC, productId, null))

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест получения списка настроек, положительный") {
                assertEquals(200, it.statusCode())
                var i = 0
                var result = false
                while (it.jsonPath().get<Any>(String.format("[%d].id", i)) != null) {
                    var id = it.jsonPath().get<Any>(String.format("[%d].id", i))
                    if (id != null && id!!.equals(groupId)) {
                        assertEquals(settingId, it.jsonPath().get<Any>(String.format("[%d].items[0].id", i)))
                        assertEquals("string", it.jsonPath()
                            .get<Any>(String.format("[%d].items[0].field.type", i)))
                        assertEquals("Строка", it.jsonPath()
                            .get<Any>(String.format("[%d].items[0].field.value", i)))
                        assertEquals(settingName, it.jsonPath().get<Any>(String.format("[%d].items[0].name", i)))
                        result = true
                    }
                    i++
                }
                assertTrue(result)
            }
        }.toList()
    }

    @TestFactory
    fun readSettingsWithoutProductIdMustReturn400(): List<DynamicTest> {
        val settingGroups = listOf(RestApiSetting.readSetting(SPEC, null, null))

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест получения настроек без productId") {
                assertEquals(400, it.statusCode())
                assertEquals("REQUEST_PARAMETER_MISSING", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun readSettingsWithNotFoundProductIdMustReturn404(): List<DynamicTest> {
        val settingGroups = listOf(RestApiSetting.readSetting(SPEC, "apitest123", null))

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест получения настроек без productId") {
                assertEquals(404, it.statusCode())
                assertEquals("PRODUCT_NOT_FOUND", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun readSettingsForLibraryMustReturn200(): List<DynamicTest> {
        val settingGroups = listOf(RestApiSetting.readSettingForLibrary(SPEC, productId))

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест получения списка настроек для библиотеки, положительный") {
                assertEquals(200, it.statusCode())
                var i = 0
                var result = false
                while (it.jsonPath().get<Any>(String.format("settings[%d].name", i)) != null) {
                    if (it.jsonPath().get<Any>(String.format("settings[%d].name", i))
                        == "firstGroupOfFirstLevel.$settingName") {
                        result = true
                    }
                    i++
                }
                assertTrue(result)
            }
        }.toList()
    }

    @TestFactory
    fun readSettingsForLibraryWithoutProductIdMustReturn400(): List<DynamicTest> {
        val settingGroups = listOf(RestApiSetting.readSettingForLibrary(SPEC, null))

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест получения настроек для библиотеки без productId") {
                assertEquals(400, it.statusCode())
                assertEquals("REQUEST_PARAMETER_MISSING", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun readSettingsForLibraryWithNotFoundProductIdProductIdMustReturn404(): List<DynamicTest> {
        val settingGroups = listOf(RestApiSetting.readSettingForLibrary(SPEC, "apitest123"))

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест получения настроек для библиотеки без productId") {
                assertEquals(404, it.statusCode())
                assertEquals("PRODUCT_NOT_FOUND", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @Test
    fun readSettingWithTenantIdMustReturn200() {
        val response = RestApiSetting.readSetting(SPEC, productId, tenantId)
        assertEquals(200, response.statusCode)
    }

    @Test
    fun readSettingWithNotFoundTenantIdMustReturn404() {
        val response = RestApiSetting.readSetting(SPEC, productId, "3b46e6aa-406e-4290-a890-9bdabf3b1e1d")
        assertEquals(404, response.statusCode())
        assertEquals("TENANT_NOT_FOUND", response.jsonPath().get("error"))
    }

    @Test
    fun readSettingWithIncorrectTenantIdMustReturn400() {
        val response = RestApiSetting.readSetting(SPEC, productId, "12345")
        assertEquals(400, response.statusCode())
    }
}
