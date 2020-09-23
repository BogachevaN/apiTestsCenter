package ru.cft.center.products.apitests.restapi

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import ru.cft.center.products.apitests.helpers.EndPoints
import ru.cft.center.products.apitests.dto.tenant.AddTenantToProductRequest
import ru.cft.center.products.apitests.dto.tenant.CreateTenantRequest

object RestApiTenant {
    fun createTenant(spec: RequestSpecification, body: CreateTenantRequest?) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .body(body)
            .post(EndPoints.CREATE_TENANT)!!

    fun getTenantList(spec: RequestSpecification) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .get(EndPoints.READ_TENANT_LIST)!!

    fun getTenantById(spec: RequestSpecification, TenantId: String) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .get(EndPoints.READ_TENANT_BY_ID, TenantId)!!

    fun getTenantByName(spec: RequestSpecification, name: String) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .get(EndPoints.READ_TENANT_BY_NAME, name)!!

    fun addTenantToProduct(spec: RequestSpecification, productId: String, body: AddTenantToProductRequest) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .body(body)
            .post(EndPoints.ADD_TENANT_TO_PRODUCT, productId)!!

    fun getTenantByProductId(spec: RequestSpecification, productId: String) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .get(EndPoints.READ_TENANT_BY_PRODUCT_ID, productId)!!

    fun readProductsByTenantId(spec: RequestSpecification, tenantId: Any?) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .get(EndPoints.READ_PRODUCTS_BY_TENANT_ID, tenantId)!!

    fun changeTenant(spec: RequestSpecification, body: CreateTenantRequest?, tenantId: Any?) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .body(body)
            .put(EndPoints.CHANGE_TENANT, tenantId)!!
}
