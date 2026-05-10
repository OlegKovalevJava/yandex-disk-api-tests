package com.yandex.fintech.qa.tests;

import com.yandex.fintech.qa.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Smoke-тесты для проверки доступности API Яндекс.Диска.
 * Проверяем, что сервер жив и токен валиден.
 * Эндпоинт: GET /v1/disk
 */
@Tag("smoke")
public class DiskSmokeTest extends BaseTest {

    @Test
    @DisplayName("GET /v1/disk — получение информации о Диске (smoke)")
    public void getDiskInfoShouldReturn200() {
        given()
                .spec(requestSpec)
                .when()
                .get(config.getProperty("disk.api.version") + "/disk")
                .then()
                .statusCode(200)
                .body("total_space", notNullValue())
                .body("used_space", notNullValue())
                .body("trash_size", notNullValue())
                .body("system_folders", notNullValue())
                .body("system_folders.applications", notNullValue())
                .body("system_folders.downloads", notNullValue());
    }
}
