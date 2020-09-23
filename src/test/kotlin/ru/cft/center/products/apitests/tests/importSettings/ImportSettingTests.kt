package ru.cft.center.products.apitests.tests.importSettings

import io.restassured.response.Response
import org.junit.jupiter.api.*
import ru.cft.center.products.apitests.TestBase
import ru.cft.center.products.apitests.restapi.*
import ru.cft.center.products.apitests.dto.settings.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.cft.center.products.apitests.dto.product.CreateProductRequest

@Tag("api")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImportSettingTests : TestBase() {

    private lateinit var productId: String

    private var settingsStringType = Settings.Items(1, "apiTestSettingOne", "Настройка #1", "Описание настройки 1", Settings.Field(SettingType.String, "test"))
    private var settingsNumberType = Settings.Items(2, "apiTestSettingTwo", "Настройка #2", "Описание настройки 2", Settings.Field(SettingType.Number, 630126))

    private var settingsGroupOne = Settings(1, "apiTestGroupOne", "Группа #1", "Описание Группы #1", listOf(settingsStringType))
    private var settingsGroupTwo = Settings(2, "apiTestGroupTwo", "Группа #2", "Описание Группы #2", listOf(settingsNumberType))


    @BeforeEach
    fun createProduct() {
        val response = RestApiProduct.createProduct(SPEC, CreateProductRequest("apiTestImport #${getRandomString(3)}", "API-тест импорт настроек", "Описание продукта"))
        assertEquals(200, response.statusCode())
        val jsonPath = response.jsonPath()
        productId = jsonPath.get("id")
    }

    @Test
    fun importSettingMustReturn204() {

        val requestBody = ImportSettingsRequest(listOf(settingsGroupOne, settingsGroupTwo), productId)
        val response = RestApiSetting.importSetting(SPEC, requestBody)

        assertEquals(204, response.statusCode())

        val getSettings = RestApiSetting.readSetting(SPEC, productId, null)
        assertEquals(200, getSettings.statusCode())
        assertNotNull(getSettings.body())
    }

    @Test
    fun importSettingWithoutIdMustReturn204() {

        settingsGroupOne.id = null
        settingsNumberType.id = null
        settingsGroupTwo.id = null
        settingsStringType.id = null

        val requestBody = ImportSettingsRequest(listOf(settingsGroupOne, settingsGroupTwo), productId)
        val response = RestApiSetting.importSetting(SPEC, requestBody)

        assertEquals(204, response.statusCode())

        val getSettings = RestApiSetting.readSetting(SPEC, productId, null)
        assertEquals(200, getSettings.statusCode())
        assertNotNull(getSettings.body())
    }

    @Test
    fun importSettingToNotEmptyProductMustReturn409() {

        val requestBody = ImportSettingsRequest(listOf(settingsGroupOne, settingsGroupTwo), productId)
        val addSettingsToProduct = RestApiSetting.importSetting(SPEC, requestBody)

        assertEquals(204, addSettingsToProduct.statusCode())

        val importSettingsSameTenant = ImportSettingsRequest(listOf(settingsGroupOne, settingsGroupTwo), productId)
        val response = RestApiSetting.importSetting(SPEC, importSettingsSameTenant)

        assertEquals(409, response.statusCode())
        assertEquals("IMPORT_CONDITION_NOT_MET", response.jsonPath().get("error"))
    }

    @Test
    fun importSettingToNonexistentProductMustReturn404() {

        val requestBody = ImportSettingsRequest(listOf(settingsGroupOne, settingsGroupTwo), "nonexistent")
        val response = RestApiSetting.importSetting(SPEC, requestBody)

        assertEquals(404, response.statusCode())
        assertEquals("PRODUCT_NOT_FOUND", response.jsonPath().get("error"))
    }

    @ParameterizedTest
    @MethodSource("invalidRequestWithNullRequiredFieldForImportSetting")
    fun importSettingWithoutRequiredFieldMustReturn400(setting: ImportSettingsRequest) {
        val response = RestApiSetting.importSetting(SPEC, setting)
        assertEquals(400, response.statusCode())
    }

    fun invalidRequestWithNullRequiredFieldForImportSetting(): List<ImportSettingsRequest> {
        val importedSettings = listOf(
            ImportSettingsRequest(listOf(
                Settings(
                    id = 1,
                    name = "apiTestGroupOne",
                    title = "Группа #1",
                    description = "Описание Группы #1",
                    items = listOf(Settings.Items(
                        id = 1,
                        name = "apiTestSettingOne",
                        title = "Настройка #1",
                        description = "Описание настройки 1",
                        field = Settings.Field(
                            type = SettingType.String,
                            value = "test"
                        )
                    ))
                ),
                Settings(
                    id = 2,
                    name = "apiTestGroupTwo",
                    title = "Группа #2",
                    description = "Описание Группы #2",
                    items = listOf(Settings.Items(
                        id = 2,
                        name = "apiTestSettingTwo",
                        title = "Настройка #2",
                        description = "Описание настройки 2",
                        field = Settings.Field(
                            type = SettingType.Number,
                            value = 630126
                        )
                    ))
                )
            )),
            ImportSettingsRequest(listOf(
                Settings(
                id = 1,
                name = null,
                title = "Группа #1",
                description = "Описание Группы #1",
                items = listOf(Settings.Items(
                    id = 1,
                    name = null,
                    title = "Настройка #1",
                    description = "Описание настройки 1",
                    field = Settings.Field(
                        type = SettingType.String,
                        value = "test"
                    )
                ))
            ),
                Settings(
                    id = 2,
                    name = null,
                    title = "Группа #2",
                    description = "Описание Группы #2",
                    items = listOf(Settings.Items(
                        id = 2,
                        name = null,
                        title = "Настройка #2",
                        description = "Описание настройки 2",
                        field = Settings.Field(
                            type = SettingType.Number,
                            value = 630126
                        )
                    ))
                )
            ),
                productId = "testImport"
            ),
            ImportSettingsRequest(listOf(
                Settings(
                    id = 1,
                    name = "apiTestGroupOne",
                    title = null,
                    description = "Описание Группы #1",
                    items = listOf(Settings.Items(
                        id = 1,
                        name = "apiTestSettingOne",
                        title = null,
                        description = "Описание настройки 1",
                        field = Settings.Field(
                            type = SettingType.String,
                            value = "test"
                        )
                    ))
                ),
                Settings(
                    id = 2,
                    name = "apiTestGroupTwo",
                    title = null,
                    description = "Описание Группы #2",
                    items = listOf(Settings.Items(
                        id = 2,
                        name = "apiTestSettingTwo",
                        title = null,
                        description = "Описание настройки 2",
                        field = Settings.Field(
                            type = SettingType.Number,
                            value = 630126
                        )
                    ))
                )
            ),
                productId = "testImport"
            ),
            ImportSettingsRequest(listOf(
                Settings(
                    id = 1,
                    name = "apiTestGroupOne",
                    title = "Группа #1",
                    description = "Описание Группы #1",
                    items = listOf(Settings.Items(
                        id = 1,
                        name = "apiTestSettingOne",
                        title = "Настройка #1",
                        description = null,
                        field = Settings.Field(
                            type = SettingType.String,
                            value = "test",
                            options = null,
                            overrides = listOf(Settings.Field.Tenant(
                                tenantId = null,
                                value = "Значение Настройки #1 для банка"
                            ))
                        )
                    ))
                )
            ))
        )
        return importedSettings
    }
}
