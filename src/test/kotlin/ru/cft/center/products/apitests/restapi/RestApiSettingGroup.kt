package ru.cft.center.products.apitests.restapi

import io.restassured.RestAssured.given
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import io.restassured.http.ContentType.JSON
import ru.cft.center.products.apitests.helpers.EndPoints
import ru.cft.center.products.apitests.dto.CreateSettingGroupRequest

object RestApiSettingGroup {
    fun createSettingGroup(spec: RequestSpecification, body: CreateSettingGroupRequest) =
        given()
            .spec(spec)
            .contentType(JSON)
            .body(body)
            .post(EndPoints.CREATE_SETTING_GROUP)!!
}
