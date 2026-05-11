# Яндекс.Диск API — Автотесты

Проект автоматизированных тестов для REST API [Яндекс.Диска](https://yandex.ru/dev/disk/rest/).

## Стек

- Java 17
- JUnit 5
- REST Assured
- Maven

## Получение тестового токена

1. Создать тестовый Яндекс-аккаунт (не личный)
2. Перейти на [Полигон](https://yandex.ru/dev/disk/poligon/)
3. Авторизоваться и получить OAuth-токен

## Быстрый старт

1. Клонировать репозиторий [yandex-disk-api-tests](https://github.com/OlegKovalevJava/yandex-disk-api-tests.git)
2. В файле `src/test/resources/config.properties` заменить `YOUR_OAUTH_TOKEN_HERE` на тестовый OAuth-токен
3. Запустить тесты через IDE или командой `mvn test`

## Покрытие API

### DiskSmokeTest — проверка доступности API

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| GET   | /v1/disk | Информация о Диске |

### DiskResourceFlowTest — полный CRUD-цикл

| Метод   | Эндпоинт                        | Описание                    |
|---------|---------------------------------|-----------------------------|
| PUT     | /v1/disk/resources              | Создание папки              |
| GET     | /v1/disk/resources              | Проверка созданной папки    |
| POST    | /v1/disk/resources/upload       | Загрузка файла в папку      |
| GET     | /v1/disk/resources              | Проверка загруженного файла |
| POST    | /v1/disk/resources/move         | Перемещение файла           |
| GET     | /v1/disk/resources              | Проверка перемещения        |
| DELETE  | /v1/disk/resources              | Удаление файла и папки      |
| GET     | /v1/disk/resources              | Проверка удаления           |