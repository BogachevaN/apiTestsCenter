package ru.cft.center.products.apitests.tests.setting

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestFactory
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.CreateSettingGroupRequest
import ru.cft.center.products.apitests.dto.CreateSettingRequest
import ru.cft.center.products.apitests.dto.SettingOption
import ru.cft.center.products.apitests.restapi.RestApiSetting
import ru.cft.center.products.apitests.restapi.RestApiSettingGroup

@Tag("api")
class CreateSettingTests : TestBase() {
    lateinit var groupId: Any

    @BeforeEach
    fun createGroup() {
        groupId = RestApiSettingGroup
            .createSettingGroup(SPEC, CreateSettingGroupRequest("apitest", "firstGroupOfFirstLevel",
                "testapi_Тестовая настройка", null, "Тестовая группа первого уровня"))
            .jsonPath().get("id")
    }

    @TestFactory
    fun createSetingWithCorrectDataMustReturn200(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "enum",
                "testapi_Environment", "Окружение", "Test", null,
                "Необходимо выбрать один вариант из списка", listOf(SettingOption("Test",
                "Тестовое"), SettingOption("Develop", "Разработка"),
                SettingOption("Prod", "Продакшн")))),
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "number",
                "testapi_numberSetting", "Настройка с указанием числа", 120, groupId)),
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "boolean",
                "testapi_booleanSetting", "Логическая настройка", false, groupId,
                "Выберите да или нет")),
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "date",
                "testapi_dateSetting", "Дата", "2020-08-01", groupId,
                "Выберите дату")),
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "string",
                "testapi_stringSetting", "Строка", "Строка", groupId,
                "Настройка с указанием строки")),
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "array",
                "testapi_arraySetting", "Массив", arrayOf("fullname", "phone"), null,
                "Выберите нужные значения", listOf(SettingOption("fullname",
                "Полное имя"), SettingOption("phone", "Телефон"),
                SettingOption("email", "Электронный адрес"))))
        )

        var number = 0
        return settingGroups.map {
            DynamicTest.dynamicTest("Позитивный тест создания настройки №${++number}") {
                assertEquals(200, it.statusCode())
                assertNotNull(it.jsonPath().get("id"))
            }
        }.toList()
    }

    @TestFactory
    fun createSettingGroupWithoutRequiredFieldMustReturn400(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSetting.createSetting(SPEC, CreateSettingRequest(null, "boolean",
                "testapi_booleanSetting", "Логическая настройка", false, groupId,
                "Выберите да или нет")),
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", null,
                "testapi_numberSetting", "Настройка с указанием числа", 120, groupId)),
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "number", null,
                "Настройка с указанием числа", 120, groupId)),
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "number",
                "testapi_numberSetting", null, 120, groupId)),
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "number",
                "testapi_numberSetting", "Настройка с указанием числа", null, groupId))

        )

        var number = 0
        return settingGroups.map {
            DynamicTest.dynamicTest("Тест обязательных полей при создании настройки №${++number}") {
                assertEquals(400, it.statusCode())
                assertEquals("OBJECT_PARAMETER_MISSING", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun createSettingGroupWithIncorrectDataMustReturn400(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "boolean",
                "testapi_booleanSetting", "Логическая настройка", false, "groupId",
                "Выберите да или нет"))
        )

        var number = 0
        return settingGroups.map {
            DynamicTest.dynamicTest("Тест создания настройки с некорректными данными №${++number}") {
                assertEquals(400, it.statusCode())
                assertEquals("INVALID_OBJECT_PARAMETER", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun createSettingWithNotFoundGroupMustReturn404(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest", "date",
                "testapi_dateSetting", "Дата", "2020-08-01", 999999999,
                "Выберите дату"))
        )

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест создания настройки с несуществующей группой") {
                assertEquals(404, it.statusCode())
                assertEquals("SETTING_GROUP_NOT_FOUND", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun createSettingWithNotFoundProductMustReturn404(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSetting.createSetting(SPEC, CreateSettingRequest("apitest123", "date",
                "testapi_dateSetting", "Дата", "2020-08-01", groupId, "Выберите дату"))
        )

        return settingGroups.map {
            DynamicTest.dynamicTest("Тест создания настройки с несуществующим продуктом") {
                assertEquals(404, it.statusCode())
                assertEquals("PRODUCT_NOT_FOUND", it.jsonPath().get("error"))
            }
        }.toList()
    }
}
