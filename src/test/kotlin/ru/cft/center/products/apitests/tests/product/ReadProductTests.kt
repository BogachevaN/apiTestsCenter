package ru.cft.center.products.apitests.tests.product

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.dto.product.CreateProductRequest
import ru.cft.center.products.apitests.restapi.RestApiProduct

@Tag("api")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadProductTests : TestBase() {

    private lateinit var productId: String

    @BeforeAll
    fun createProduct() {
        productId = "readProductTest#" + getRandomNumber(4)
        RestApiProduct.createProduct(SPEC, CreateProductRequest(productId, productId, productId))
    }

    @Test
    fun readProductsMustReturn200() {
        var response = RestApiProduct.readProductList(SPEC)
        assertEquals(200, response.statusCode())

        val jsonPath = response.jsonPath()
        var i = 0
        while (jsonPath.get<Any>(String.format("[%d].id", i)) != null) {
            var id = jsonPath.get<Any>(String.format("[%d].id", i))
            if (id.equals(productId)) {
                assertEquals(productId, jsonPath.get<Any>(String.format("[%d].description", i)))
                assertEquals(productId, jsonPath.get<Any>(String.format("[%d].title", i)))
            }
            i++
        }
    }

    @Test
    fun readProductByIdMustReturn200() {
        val response = RestApiProduct.readProductById(SPEC, productId)
        assertEquals(200, response.statusCode())
        val jsonPath = response.jsonPath()
        assertEquals(productId, jsonPath.get("description"))
        assertEquals(productId, jsonPath.get("id"))
        assertEquals(productId, jsonPath.get("title"))
    }

    @Test
    fun readProductByWithNotFoundIdMustReturn404() {
        val response = RestApiProduct.readProductById(SPEC, "apitest999999")
        assertEquals(404, response.statusCode())
        assertEquals("PRODUCT_NOT_FOUND", response.jsonPath().get("error"))
    }

}