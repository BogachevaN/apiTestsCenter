package ru.cft.center.products.apitests.tests.setting

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestFactory
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.CreateSettingGroupRequest
import ru.cft.center.products.apitests.dto.CreateSettingRequest
import ru.cft.center.products.apitests.restapi.RestApiSetting
import ru.cft.center.products.apitests.restapi.RestApiSettingGroup

@Tag("api")
class ReadSettingsTests : TestBase() {

    var groupId: Int = 0
    lateinit var settingId: Any
    lateinit var settingName: String

    @BeforeEach
    fun createGroup() {
        settingName = "testapi_stringSetting" + getRandomString(5)
        groupId = RestApiSettingGroup
            .createSettingGroup(SPEC, CreateSettingGroupRequest("apitest", "firstGroupOfFirstLevel",
                "testapi_Тестовая настройка", null, "Тестовая группа первого уровня"))
            .jsonPath().get("id")
        settingId = RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "string",
            settingName, "Строка", "Строка", groupId, "Настройка с указанием строки"))
            .jsonPath().get("id")
    }

    @TestFactory
    fun readSettingsMustReturn200(): List<DynamicTest> {
        val settingGroups = listOf(RestApiSetting.readSetting(SPEC, "apitest"))

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест получения списка настроек, положительный") {
                assertEquals(200, it.statusCode())
                var i = 0
                var result = false
                while (it.jsonPath().get<Any>(String.format("[%d].id", i)) != null) {
                    val id = it.jsonPath().get<Any>(String.format("[%d].id", i))
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
        val settingGroups = listOf(RestApiSetting.readSetting(SPEC, null))

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест получения настроек без productId") {
                assertEquals(400, it.statusCode())
                //assertEquals("MISSING_PARAMETER", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun readSettingsWithNotFoundProductIdProductIdMustReturn404(): List<DynamicTest> {
        val settingGroups = listOf(RestApiSetting.readSetting(SPEC, "apitest123"))

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест получения настроек без productId") {
                assertEquals(404, it.statusCode())
                assertEquals("PRODUCT_NOT_FOUND", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun readSettingsForLibraryMustReturn200(): List<DynamicTest> {
        val settingGroups = listOf(RestApiSetting.readSettingForLibrary(SPEC, "apitest"))

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
}
