package ru.cft.center.products.apitests.restapi

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import ru.cft.center.products.apitests.helpers.EndPoints
import ru.cft.center.products.apitests.dto.settings.*

object RestApiSetting {
    fun createSetting(spec: RequestSpecification, body: CreateSettingRequest) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .body(body)
            .post(EndPoints.CREATE_SETTING)!!

    fun readSetting(spec: RequestSpecification, productId: Any?, tenantId: Any?): Response {
        return if (productId != null) {
            given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .param("productId", productId)
                .param("tenantId", tenantId)
                .get(EndPoints.READ_SETTING)!!
        } else {
            given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .get(EndPoints.READ_SETTING)!!
        }
    }

    fun readSettingForLibrary(spec: RequestSpecification, productId: Any?): Response {
        return if (productId != null) {
            given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .param("productId", productId)
                .get(EndPoints.READ_SETTING_LIBRARY)!!
        } else {
            given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .get(EndPoints.READ_SETTING_LIBRARY)!!
        }
    }

    fun changeSetting(spec: RequestSpecification, body: ChangeSettingValueRequest, settingId: Any?) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .body(body)
            .put(EndPoints.CHANGE_SETTING, settingId)!!

    fun importSetting(spec: RequestSpecification, body: ImportSettingsRequest) =
        given()
            .spec(spec)
            .contentType(ContentType.JSON)
            .body(body)
            .post(EndPoints.IMPORT_SETTINGS_TO_PRODUCT)!!
}
