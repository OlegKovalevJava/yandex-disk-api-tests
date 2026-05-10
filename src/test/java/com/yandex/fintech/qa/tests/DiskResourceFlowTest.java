package com.yandex.fintech.qa.tests;

import com.yandex.fintech.qa.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * End-to-End сценарий работы с ресурсами Яндекс.Диска.
 * Проверяет полный жизненный цикл: создание → проверка → загрузка → перемещение → удаление.
 * Покрывает методы: PUT, GET, POST, DELETE.
 */
@Tag("e2e")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DiskResourceFlowTest extends BaseTest {

    private static final String FOLDER_NAME = "e2e-folder-" + UUID.randomUUID();
    private static final String FILE_NAME = "e2e-file-" + UUID.randomUUID() + ".txt";
    private static final String MOVED_FILE_NAME = "e2e-moved-" + UUID.randomUUID() + ".txt";

    @Test
    @Order(1)
    @DisplayName("PUT /v1/disk/resources — создание папки (201)")
    public void createFolder() {
        given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .put(config.getProperty("disk.api.version") + "/disk/resources")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/disk/resources — проверка созданной папки (200)")
    public void checkFolder() {
        given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .get(config.getProperty("disk.api.version") + "/disk/resources")
                .then()
                .statusCode(200)
                .body("type", equalTo("dir"))
                .body("name", equalTo(FOLDER_NAME));
    }

    @Test
    @Order(3)
    @DisplayName("POST /v1/disk/resources/upload — загрузка файла в папку (201)")
    public void uploadFile() {
        String filePath = FOLDER_NAME + "/" + FILE_NAME;

        String uploadUrl = given()
                .spec(requestSpec)
                .queryParam("path", filePath)
                .when()
                .get(config.getProperty("disk.api.version") + "/disk/resources/upload")
                .then()
                .statusCode(200)
                .extract()
                .path("href");

        given()
                .spec(requestSpec)
                .body("Тестовый файл для Яндекс.Диска")
                .when()
                .put(uploadUrl)
                .then()
                .statusCode(201);
    }

    @Test
    @Order(4)
    @DisplayName("GET /v1/disk/resources — проверка загруженного файла (200)")
    public void checkUploadedFile() {
        String filePath = FOLDER_NAME + "/" + FILE_NAME;

        given()
                .spec(requestSpec)
                .queryParam("path", filePath)
                .when()
                .get(config.getProperty("disk.api.version") + "/disk/resources")
                .then()
                .statusCode(200)
                .body("type", equalTo("file"))
                .body("name", equalTo(FILE_NAME))
                .body("size", greaterThan(0));
    }

    @Test
    @Order(5)
    @DisplayName("POST /v1/disk/resources/move — перемещение файла (201)")
    public void moveFile() {
        String fromPath = FOLDER_NAME + "/" + FILE_NAME;

        given()
                .spec(requestSpec)
                .queryParam("from", fromPath)
                .queryParam("path", MOVED_FILE_NAME)
                .when()
                .post(config.getProperty("disk.api.version") + "/disk/resources/move")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(6)
    @DisplayName("GET /v1/disk/resources — проверка перемещённого файла (200 и 404)")
    public void checkMovedFile() {
        given()
                .spec(requestSpec)
                .queryParam("path", MOVED_FILE_NAME)
                .when()
                .get(config.getProperty("disk.api.version") + "/disk/resources")
                .then()
                .statusCode(200)
                .body("name", equalTo(MOVED_FILE_NAME));

        String oldPath = FOLDER_NAME + "/" + FILE_NAME;
        given()
                .spec(requestSpec)
                .queryParam("path", oldPath)
                .when()
                .get(config.getProperty("disk.api.version") + "/disk/resources")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    @DisplayName("DELETE /v1/disk/resources — удаление файла и папки (202 или 204)")
    public void deleteResources() {
        given()
                .spec(requestSpec)
                .queryParam("path", MOVED_FILE_NAME)
                .when()
                .delete(config.getProperty("disk.api.version") + "/disk/resources")
                .then()
                .statusCode(anyOf(is(202), is(204)));

        given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .delete(config.getProperty("disk.api.version") + "/disk/resources")
                .then()
                .statusCode(anyOf(is(202), is(204)));
    }

    @Test
    @Order(8)
    @DisplayName("GET /v1/disk/resources — проверка удаления (404)")
    public void checkDeletion() {
        given()
                .spec(requestSpec)
                .queryParam("path", MOVED_FILE_NAME)
                .when()
                .get(config.getProperty("disk.api.version") + "/disk/resources")
                .then()
                .statusCode(404);

        given()
                .spec(requestSpec)
                .queryParam("path", FOLDER_NAME)
                .when()
                .get(config.getProperty("disk.api.version") + "/disk/resources")
                .then()
                .statusCode(404);
    }
}
