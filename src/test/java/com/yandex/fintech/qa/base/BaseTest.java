package com.yandex.fintech.qa.base;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;

/**
 * Базовый класс для всех тестов API Яндекс.Диска.
 * Загружает конфигурацию, настраивает базовый URL и общую спецификацию запросов.
 */
public class BaseTest {

    protected static final Properties config = new Properties();
    protected static RequestSpecification requestSpec;

    @BeforeAll
    public static void setUp() {
        try (InputStream input = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
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
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
