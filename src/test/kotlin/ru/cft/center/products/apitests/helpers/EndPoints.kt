package ru.cft.center.products.apitests.helpers

object EndPoints {
    const val READ_PRODUCT_LIST = "/api/v1/product"
    const val CREATE_SETTING_GROUP = "/api/v1/admin/setting-group"
    const val CREATE_SETTING = "/api/v1/admin/setting"
    const val CHANGE_SETTING = "/api/v1/admin/setting/{id}/value"
    const val READ_SETTING = "/api/v1/admin/setting"
    const val READ_SETTING_LIBRARY = "/api/v1/setting"
    const val IMPORT_SETTINGS_TO_PRODUCT = "/api/v1/admin/setting/import"
    const val CREATE_TENANT = "/api/v1/tenant"
    const val READ_TENANT_LIST = "/api/v1/tenant"
    const val READ_TENANT_BY_ID = "/api/v1/tenant/{id}"
    const val READ_TENANT_BY_NAME = "/api/v1/tenant/name/{name}"
    const val ADD_TENANT_TO_PRODUCT = "/api/v1/product/{id}/add-tenant"
    const val READ_TENANT_BY_PRODUCT_ID = "/api/v1/product/{id}/tenants"
    const val READ_PRODUCTS_BY_TENANT_ID = "/api/v1/tenant/{id}/products"
    const val CREATE_PRODUCT = "/api/v1/product"
    const val READ_PRODUCT_BY_ID = "/api/v1/product/{id}"
    const val CHANGE_TENANT = "/api/v1/tenant/{id}"
}