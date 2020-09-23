package ru.cft.center.products.apitests.tests.setting

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.restapi.RestApiTenant

@Tag("api")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadProductsByTenantIdTests : TestBase() {

    private lateinit var tenantId: String

    @BeforeAll
    fun getTenantId() {
        val response = RestApiTenant.getTenantByName(SPEC, "TestBank")
        assertEquals(200, response.statusCode())
        tenantId = response.jsonPath().get("id")
    }

    @Test
    fun readProductsByExistingTenantIdMustReturn200AndProducts() {
        var products = RestApiTenant.readProductsByTenantId(SPEC, tenantId)
        assertEquals(200, products.statusCode)
        assertNotNull(products.jsonPath().getList<Any>("$").size)
    }

    @Test
    fun readProductsByNotExistingTenantIdMustReturn404() {
        var response = RestApiTenant.readProductsByTenantId(SPEC, getRandomId())
        assertEquals(404, response.statusCode)
        assertEquals("TENANT_NOT_FOUND", response.jsonPath().get("error"))
    }

    @Test
    fun readProductsByIncorrectTenantIdMustReturn400() {
        var response = RestApiTenant.readProductsByTenantId(SPEC, getRandomNumber(10))
        assertEquals(400, response.statusCode)
        assertEquals("Bad Request", response.jsonPath().get("error"))
    }

}
