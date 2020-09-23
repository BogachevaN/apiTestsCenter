package ru.cft.center.products.apitests.tests.setting

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.tenant.AddTenantToProductRequest
import ru.cft.center.products.apitests.dto.tenant.CreateTenantRequest
import ru.cft.center.products.apitests.restapi.RestApiTenant

@Tag("api")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddTenantToProductTests : TestBase() {

    private var productId = "apitest"
    private lateinit var tenantId: String

    @BeforeAll
    fun createTenant() {
        val response = RestApiTenant.createTenant(SPEC, CreateTenantRequest("addTenantApiTest_" + getRandomNumber(4), "API-Тест добавление Банка к продукту", getRandomString(6)))
        assertEquals(200, response.statusCode())
        tenantId = response.jsonPath().get("id")
    }

    @Test
    fun addTenantToProductMustReturn200() {
        val response = RestApiTenant.addTenantToProduct(SPEC, productId, AddTenantToProductRequest(tenantId))
        assertEquals(204, response.statusCode())
    }

    @Test
    fun addTenantToProductWithIncorrectProductMustReturn404() {
        val response = RestApiTenant.addTenantToProduct(SPEC, "nonexistent", AddTenantToProductRequest(tenantId))
        assertEquals(404, response.statusCode())
        assertEquals("PRODUCT_NOT_FOUND", response.jsonPath().get("error"))
    }

    @Test
    fun addTenantToProductIncorrectTenantMustReturn404() {
        val response = RestApiTenant.addTenantToProduct(SPEC, productId, AddTenantToProductRequest(getRandomId()))
        assertEquals(404, response.statusCode())
        assertEquals("TENANT_NOT_FOUND", response.jsonPath().get("error"))
    }

    @Test
    fun addTenantToProductWithoutTenantIdMustReturn400() {
        val response = RestApiTenant.addTenantToProduct(SPEC, productId, AddTenantToProductRequest(null))
        assertEquals(400, response.statusCode())
        assertEquals("OBJECT_PARAMETER_MISSING", response.jsonPath().get("error"))
    }

}
