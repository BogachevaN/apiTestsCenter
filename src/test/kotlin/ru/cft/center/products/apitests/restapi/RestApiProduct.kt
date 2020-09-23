package ru.cft.center.products.apitests.restapi

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import ru.cft.center.products.apitests.dto.product.CreateProductRequest
import ru.cft.center.products.apitests.helpers.EndPoints

object RestApiProduct {

    fun createProduct(spec: RequestSpecification, body: CreateProductRequest) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .body(body)
            .post(EndPoints.CREATE_PRODUCT)!!

    fun readProductList(spec: RequestSpecification) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .get(EndPoints.READ_PRODUCT_LIST)

    fun readProductById(spec: RequestSpecification, productId: Any?) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .get(EndPoints.READ_PRODUCT_BY_ID, productId)
}
