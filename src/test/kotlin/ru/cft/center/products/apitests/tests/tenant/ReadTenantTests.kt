package ru.cft.center.products.apitests.tests.setting

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.tenant.CreateTenantRequest
import ru.cft.center.products.apitests.restapi.RestApiTenant

@Tag("api")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadTenantTests : TestBase() {

    private lateinit var tenantId: String
    private lateinit var tenantName: String

    @BeforeAll
    fun createTenant() {
        val response = RestApiTenant.createTenant(SPEC, CreateTenantRequest("getTenantApiTest_" + getRandomNumber(4), "API-Тест получение Банка", getRandomString(6)))
        assertEquals(200, response.statusCode())
        val jsonPath = response.jsonPath()
        tenantId = jsonPath.get("id")
        tenantName = jsonPath.get("name")
    }

    @Test
    fun getTenantListMustReturn200() {
        val response = RestApiTenant.getTenantList(SPEC)

        assertEquals(200, response.statusCode())
        val jsonPath = response.jsonPath()
        assertNotNull(jsonPath.get("id"))
        assertNotNull(jsonPath.get("name"))
        assertNotNull(jsonPath.get("title"))
    }

    @Test
    fun getTenantByIdMustReturn200() {
        var response = RestApiTenant.getTenantById(SPEC, tenantId)

        assertEquals(200, response.statusCode())
        val jsonPath = response.jsonPath()
        assertEquals(jsonPath.get("id"), tenantId)
        assertNotNull(jsonPath.get("name"))
        assertNotNull(jsonPath.get("title"))
    }

    @Test
    fun getTenantByIdWithNonexistentIdMustReturn404() {
        var response = RestApiTenant.getTenantById(SPEC, getRandomId())

        assertEquals(404, response.statusCode())
        assertEquals("TENANT_NOT_FOUND", response.jsonPath().get("error"))
    }

    @Test
    fun getTenantByNameMustReturn200() {
        var response = RestApiTenant.getTenantByName(SPEC, tenantName)

        assertEquals(200, response.statusCode())
        val jsonPath = response.jsonPath()
        assertNotNull(jsonPath.get("id"))
        assertEquals(jsonPath.get("name"), tenantName)
        assertNotNull(jsonPath.get("title"))
    }

    @Test
    fun getTenantByNameWithNonexistentNameMustReturn404() {
        var response = RestApiTenant.getTenantByName(SPEC, getRandomString(4))

        assertEquals(404, response.statusCode())
        assertEquals("TENANT_NOT_FOUND", response.jsonPath().get("error"))
    }

    @Test
    fun getTenantByProductIdMustReturn200() {
        var response = RestApiTenant.getTenantByProductId(SPEC, "apitest")

        assertEquals(200, response.statusCode())
        val jsonPath = response.jsonPath()
        assertNotNull(jsonPath.get("id"))
        assertNotNull(jsonPath.get("name"))
        assertNotNull(jsonPath.get("title"))
    }

    @Test
    fun getTenantByProductIdWithNonexistentIdMustReturn404() {
        var response = RestApiTenant.getTenantByProductId(SPEC, getRandomString(4))

        assertEquals(404, response.statusCode())
        assertEquals("PRODUCT_NOT_FOUND", response.jsonPath().get("error"))
    }
}

