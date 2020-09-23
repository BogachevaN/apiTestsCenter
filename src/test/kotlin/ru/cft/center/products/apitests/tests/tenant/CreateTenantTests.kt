package ru.cft.center.products.apitests.tests.setting

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.tenant.CreateTenantRequest
import ru.cft.center.products.apitests.restapi.RestApiTenant

@Tag("api")
class CreateTenantTests : TestBase() {

    @Test
    fun createTenantMustReturn200() {
        var response = RestApiTenant.createTenant(SPEC, CreateTenantRequest("createTenantApiTest_" + getRandomNumber(4), "API-Тест создание Банка", getRandomString(6)))
        assertEquals(200, response.statusCode())
        val jsonPath = response.jsonPath()
        assertNotNull(jsonPath.get("id"))
        assertNotNull(jsonPath.get("name"))
        assertNotNull(jsonPath.get("title"))
        assertNotNull(jsonPath.get("esiaMnemonic"))
    }

    @Test
    fun createBankWithoutMnemonicMustReturn200() {
        var response = RestApiTenant.createTenant(SPEC, CreateTenantRequest("createTenantApiTest_" + getRandomNumber(4), "API-Тест создание Банка", null))
        assertEquals(200, response.statusCode())
        val jsonPath = response.jsonPath()
        assertNotNull(jsonPath.get("id"))
        assertNotNull(jsonPath.get("name"))
        assertNotNull(jsonPath.get("title"))
        assertNull(jsonPath.get("esiaMnemonic"))
    }

    @TestFactory
    fun createTenantWithoutRequiredFieldMustReturn400(): List<DynamicTest> {
        val tenant = listOf(
            RestApiTenant.createTenant(SPEC, CreateTenantRequest(null, "API-Тест создание Банка", getRandomString(6))),
            RestApiTenant.createTenant(SPEC, CreateTenantRequest("createTenantApiTest_" + getRandomNumber(4), null, getRandomString(6))))
        return tenant.map {
            DynamicTest.dynamicTest("API-Тест создание Банка без обязательных полей") {
                assertEquals(400, it.statusCode())
                assertEquals("OBJECT_PARAMETER_MISSING", it.jsonPath().get("error"))
            }
        }.toList()
    }

    @Test
    fun createTenantWithSameNameMustReturn409() {
        val sameNameTenant: String
        sameNameTenant = RestApiTenant.createTenant(SPEC, CreateTenantRequest("createTenantApiTest_" + getRandomNumber(4), "API-Тест создание Банка", getRandomString(6))).jsonPath().get("name")

        val response = RestApiTenant.createTenant(SPEC, CreateTenantRequest(sameNameTenant, "API-Тест создание Банка", null))
        assertEquals(409, response.statusCode())
        assertEquals("TENANT_EXISTS", response.jsonPath().get("error"))

    }

    @Test
    fun createTenantWithSameEsiaMnemonicMustReturn409() {
        val esiaMnemonic: String
        esiaMnemonic = RestApiTenant.createTenant(SPEC, CreateTenantRequest("createTenantApiTest_" + getRandomNumber(4), "API-Тест создание Банка", getRandomString(6))).jsonPath().get("esiaMnemonic")

        val response = RestApiTenant.createTenant(SPEC, CreateTenantRequest("createTenantApiTest_" + getRandomNumber(4), "Тестовый Банк", esiaMnemonic))
        assertEquals(409, response.statusCode())
        assertEquals("TENANT_EXISTS", response.jsonPath().get("error"))
    }
}
