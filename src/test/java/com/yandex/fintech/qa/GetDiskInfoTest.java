package com.yandex.fintech.qa;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class GetDiskInfoTest {

    private static Properties config;
    private static RequestSpecification requestSpec;

    @BeforeAll
    public static void setUp() {
        config = new Properties();
        try (InputStream input = GetDiskInfoTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Не удалось найти файл config.properties");
            }
            config.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить конфигурацию из config.properties", e);
        }

        RestAssured.baseURI = config.getProperty("base.url");
        if (RestAssured.baseURI == null) {
            throw new RuntimeException("Не указан base.url в config.properties");
        }

        String token = config.getProperty("token");
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Не указан токен в config.properties");
        }

        requestSpec = given()
                .header("Authorization", "OAuth " + token)
                .contentType("application/json");
    }

    @Test
    @DisplayName("GET /v1/disk — получение информации о Диске: статус 200")
    public void getDiskInfoShouldReturn200() {
        given()
                .spec(requestSpec)
                .when()
                .get(config.getProperty("disk.api.version") + "/disk")
                .then()
                .statusCode(200);
    }
}
