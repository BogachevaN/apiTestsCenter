package ru.cft.center.products.apitests

import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.specification.RequestSpecification
import java.util.*

open class TestBase {

    val DEFAULT_URL_PRODUCTS = "http://center-products-master.front.ftc.ru"
    val SPEC = getRequestSpecification(true)

    private fun getRequestSpecification(logBody: Boolean): RequestSpecification {
        val specBuilder = RequestSpecBuilder()
            .setBaseUri(getUrlProductService())
            .log(LogDetail.METHOD)
            .log(LogDetail.URI)
            .log(LogDetail.HEADERS)

        if (logBody) {
            specBuilder.log(LogDetail.BODY)
        }

        return specBuilder.build()
    }

    private fun getUrlProductService(): String {
        val envValue = System.getenv("URL_PRODUCTS")
        return envValue ?: DEFAULT_URL_PRODUCTS
    }

    fun getRandomString(length: Int): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getRandomNumber(length: Int): String {
        val allowedChars = "0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getRandomId(): String {
        val randomId = UUID.randomUUID()
        return randomId.toString()
    }
}
