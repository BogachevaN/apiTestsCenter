package ru.cft.center.products.apitests.tests.tenant

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.tenant.CreateTenantRequest
import ru.cft.center.products.apitests.restapi.RestApiTenant

@Tag("api")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChangeTenantTests : TestBase() {

    private lateinit var tenant: String
    private lateinit var tenantId: String
    private lateinit var helperTenant: String

    @BeforeAll
    fun createTenant() {
        tenant = "changeTenant" + getRandomNumber(4)
        var response = RestApiTenant.createTenant(SPEC, CreateTenantRequest(tenant, tenant, tenant))
        tenantId = response.jsonPath().get("id")
        helperTenant = "changeTenant" + getRandomNumber(4)
        response = RestApiTenant.createTenant(SPEC, CreateTenantRequest(helperTenant, helperTenant, helperTenant))
        assertEquals(200, response.statusCode())
    }

    @Test
    fun changeTenantMustReturn204() {
        val esiaMnemonic = "new" + getRandomNumber(4)
        val newName = "changeTenant" + getRandomNumber(4)
        val response = RestApiTenant.changeTenant(SPEC, CreateTenantRequest(newName, helperTenant, esiaMnemonic), tenantId)
        assertEquals(204, response.statusCode())

        val jsonPath = RestApiTenant.getTenantById(SPEC, tenantId).jsonPath()
        assertEquals(newName, jsonPath.get("name"))
        assertEquals(helperTenant, jsonPath.get("title"))
        assertEquals(esiaMnemonic, jsonPath.get("esiaMnemonic"))
    }

    @Test
    fun changeTenantWithNotFoundIdMustReturn404() {
        val esiaMnemonic = "new" + getRandomNumber(4)
        val response = RestApiTenant.changeTenant(SPEC, CreateTenantRequest(helperTenant, helperTenant, esiaMnemonic), tenantId + 999)
        assertEquals(404, response.statusCode())
        assertEquals("TENANT_NOT_FOUND", response.jsonPath().get("error"))
    }

    @Test
    fun changeTenantWithoutNameMustReturn400() {
        val esiaMnemonic = "new" + getRandomNumber(4)
        val response = RestApiTenant.changeTenant(SPEC, CreateTenantRequest(null, helperTenant, esiaMnemonic), tenantId)
        assertEquals(400, response.statusCode())
        assertEquals("OBJECT_PARAMETER_MISSING", response.jsonPath().get("error"))
    }

    @Test
    fun changeTenantWithoutTitleMustReturn400() {
        val esiaMnemonic = "new" + getRandomNumber(4)
        val response = RestApiTenant.changeTenant(SPEC, CreateTenantRequest(helperTenant, null, esiaMnemonic), tenantId)
        assertEquals(400, response.statusCode())
        assertEquals("OBJECT_PARAMETER_MISSING", response.jsonPath().get("error"))
    }

    @Test
    fun changeTenantWithExistEsiaMnemonicMustReturn409() {
        val response = RestApiTenant.changeTenant(SPEC, CreateTenantRequest(helperTenant, helperTenant, helperTenant), tenantId)
        assertEquals(409, response.statusCode())
        assertEquals("TENANT_EXISTS", response.jsonPath().get("error"))
    }

    @ParameterizedTest
    @MethodSource("incorrectDataForChangeTenant")
    fun changeTenantWithIncorrectDataMustReturn400(tenant: CreateTenantRequest){
        val response = RestApiTenant.changeTenant(SPEC, tenant, tenantId)
        assertEquals(400, response.statusCode())
    }

    fun incorrectDataForChangeTenant(): List<CreateTenantRequest> {
        val tenant = "changeTenant" + getRandomNumber(3)
        return listOf(
            CreateTenantRequest(tenant, tenant, tenant + "OuEkWnvW464mzYN3J"),
            CreateTenantRequest("zRLGtjoR5HFj3COrw2wSe0Y7RivPE2IClgeeSU0qUa2rayudaTwmZLWvSJG0CaLdY", tenant, tenant),
            CreateTenantRequest(tenant, "zRLGtjoR5HFj3COrw2wSe0Y7RivPE2IClgeeSU0qUa2rayudaTwmZLWvSJG0CaLdY", tenant))
    }
}