# Яндекс.Диск API — Автотесты

Проект автоматизированных тестов для REST API Яндекс.Диска: https://yandex.ru/dev/disk/rest/

## Стек

- Java 17
- JUnit 5
- REST Assured
- Maven

## Структура проекта

src/test/java/com/yandex/fintech/qa/
├── base/
│   └── BaseTest.java          # Базовая конфигурация
└── tests/
├── DiskSmokeTest.java         # Smoke-тест (GET /v1/disk)
└── DiskResourceFlowTest.java  # E2E-сценарий (PUT, GET, POST, DELETE)

## Получение тестового токена

1. Создать тестовый Яндекс-аккаунт (не личный)
2. Перейти на Полигон: https://yandex.ru/dev/disk/poligon/
3. Авторизоваться и получить OAuth-токен

## Быстрый старт

1. Клонировать репозиторий:
   git clone https://github.com/OlegKovalevJava/yandex-disk-api-tests.git
   cd yandex-disk-api-tests

2. Настроить токен:
   cp src/test/resources/config.example.properties src/test/resources/config.properties
   Вставить тестовый OAuth-токен в config.properties

3. Запустить тесты:
   mvn test

## Покрытие API

| Метод   | Эндпоинт                        | Тест                  |
|---------|---------------------------------|-----------------------|
| GET     | /v1/disk                        | DiskSmokeTest         |
| PUT     | /v1/disk/resources              | DiskResourceFlowTest  |
| GET     | /v1/disk/resources              | DiskResourceFlowTest  |
| POST    | /v1/disk/resources/upload       | DiskResourceFlowTest  |
| POST    | /v1/disk/resources/move         | DiskResourceFlowTest  |
| DELETE  | /v1/disk/resources              | DiskResourceFlowTest  |