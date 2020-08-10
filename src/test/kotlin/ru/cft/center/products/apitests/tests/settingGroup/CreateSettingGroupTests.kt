package ru.cft.center.products.apitests.tests.settingGroup

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestFactory
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.CreateSettingGroupRequest
import ru.cft.center.products.apitests.restapi.RestApiSettingGroup

@Tag("api")
class CreateSettingGroupTests : TestBase() {
    lateinit var mainGroupId: Any

    @BeforeEach
    fun createMainGroup() {
        mainGroupId = RestApiSettingGroup
            .createSettingGroup(SPEC, CreateSettingGroupRequest("apitest",
                "testapi_firstGroupOfFirstLevel", "Главная группа", null,
                "Первая группа первого уровня")).jsonPath().get("id")
    }

    @TestFactory
    fun createSettingGroupWithCorrectDataMustReturn200(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSettingGroup.createSettingGroup(SPEC, CreateSettingGroupRequest("apitest",
                "testapi_firstChildGroup", "Первая дочерняя группа $mainGroupId", mainGroupId,
                "Первая дочерняя группа $mainGroupId")),
            RestApiSettingGroup.createSettingGroup(SPEC, CreateSettingGroupRequest("apitest",
                "testapi_firstChildGroup", "Вторая дочерняя группа $mainGroupId", mainGroupId))
        )

        var number = 0
        return settingGroups.map {
            dynamicTest("Позитивный тест создания группы настроек №${++number}") {
                assertEquals(200, it.statusCode())
                assertNotNull(it.jsonPath().get("id"))
            }
        }.toList()
    }

    @TestFactory
    fun createSettingGroupWithoutRequiredFieldMustReturn400(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSettingGroup.createSettingGroup(SPEC, CreateSettingGroupRequest(null,
                "firstChildGroup", "testapi_Первая дочерняя группа $mainGroupId", mainGroupId,
                "Первая дочерняя группа $mainGroupId")),
            RestApiSettingGroup.createSettingGroup(SPEC, CreateSettingGroupRequest("apitest", null,
                "testapi_Первая дочерняя группа $mainGroupId", mainGroupId,
                "Первая дочерняя группа $mainGroupId")),
            RestApiSettingGroup.createSettingGroup(SPEC, CreateSettingGroupRequest("apitest",
                "firstChildGroup", null, mainGroupId,
                "Первая дочерняя группа $mainGroupId")),
            RestApiSettingGroup.createSettingGroup(SPEC, CreateSettingGroupRequest(null,
                "firstChildGroup", "testapi_Первая дочерняя группа $mainGroupId", null,
                "Первая дочерняя группа $mainGroupId"))
        )

        var number = 0
        return settingGroups.map {
            dynamicTest("Тест проверки обязательных полей при создании группы настроек №${++number}") {
                assertEquals(400, it.statusCode())
                assertEquals("OBJECT_PARAMETER_MISSING", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun createSettingGroupWithIncorrectDataMustReturn400(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSettingGroup.createSettingGroup(SPEC, CreateSettingGroupRequest("apitest",
                "firstChildGroup", "testapi_Первая дочерняя группа $mainGroupId",
                "mainGroupId", "Первая дочерняя группа $mainGroupId"))
        )

        var number = 0
        return settingGroups.map {
            dynamicTest("Тест на создание группы настроек с некорректными данными №${++number}") {
                assertEquals(400, it.statusCode())
                assertEquals("INVALID_OBJECT_PARAMETER", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun createSettingGroupWithNotFoundParentGroupMustReturn404(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSettingGroup.createSettingGroup(SPEC, CreateSettingGroupRequest("apitest",
                "firstChildGroup", "testapi_Первая дочерняя группа $mainGroupId", 999999999,
                "Первая дочерняя группа $mainGroupId"))
        )

        var number = 0
        return settingGroups.map {
            dynamicTest("Тест создания группы настроек с несуществующей группой №${++number}") {
                assertEquals(404, it.statusCode())
                assertEquals("SETTING_GROUP_NOT_FOUND", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @TestFactory
    fun createSettingGroupWithNotFoundProductMustReturn404(): List<DynamicTest> {
        val settingGroups = listOf(
            RestApiSettingGroup.createSettingGroup(SPEC, CreateSettingGroupRequest("apitest123",
                "firstChildGroup",
                "testapi_Первая дочерняя группа $mainGroupId", mainGroupId,
                "Первая дочерняя группа $mainGroupId"))
        )

        var number = 0
        return settingGroups.map {
            dynamicTest("Тест создания группы настроек с несуществующим продуктом №${++number}") {
                assertEquals(404, it.statusCode())
                assertEquals("PRODUCT_NOT_FOUND", it.jsonPath().get("error"))
            }
        }.toList()
    }
}


