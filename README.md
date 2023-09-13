# Explore with me
Приложение — афиша. В этой афише можно предложить какое-либо событие от выставки до похода в кино и собрать компанию для участия в нём

***Стек:Java 11, Spring Boot, Spring Data JPA, PostgreSQL, Docker, Maven***
## Архитектура
Сервисы:
1. ewm-stat-service - сервис собирающий и хранящий статистику по публичным просмотрам событий.
2. ewm-service - основной сервис логики приложения.
## Модель данных
Жизненный цикл события:
1. Создание.
2. Ожидание публикации. В статус ожидания публикации событие переходит сразу после создания.
3. Публикация. В это состояние событие переводит администратор.
4. Отмена публикации. В это состояние событие переходит в двух случаях. Первый — если администратор решил, что его нельзя публиковать. Второй — когда инициатор события решил отменить его на этапе ожидания публикации.
## Схема БД
#### DB Diagram ewm-service
![DB Diagram ewm-service](https://github.com/CyberCoHuK/java-explore-with-me/assets/108213849/3483f7fe-7e21-4d21-a960-0558f7a0767d)
#### DB Diagram ewm-stat-service
![DB Diagram ewm-stat-service](https://github.com/CyberCoHuK/java-explore-with-me/assets/108213849/a98ddb17-ab09-4c74-bd15-d48a2be8e4a4)

## Установка и запуск проекта
Необходимо настроенная система виртуализации, установленный [Docker Desktop](https://www.docker.com/products/docker-desktop/)

1. Клонируйте репозиторий проекта на свою локальную машину:
   ```
   git clone https://github.com/CyberCoHuK/java-explore-with-me.git
   ```
2. Запустите командную строку и перейдите в корень директории с проектом.
3. Соберите проект 
   ```
   mvn clean package
   ```
4. Введите следующую команду, которая подготовит и запустит приложение на вашей локальной машине
   ```
   $  docker-compose up
   ```
5. Приложение будет запущено на порту 8080. Вы можете открыть свой веб-браузер и перейти по адресу `http://localhost:8080`, чтобы получить доступ к приложению ExploreWithMe.

## Спецификация API
1. Спецификация [ewm-stat-service](https://editor.swagger.io/?url=https://raw.githubusercontent.com/CyberCoHuK/java-explore-with-me/main/ewm-stats-service-spec.json)
2. Спецификация [ewm-service](https://editor.swagger.io/?url=https://raw.githubusercontent.com/CyberCoHuK/java-explore-with-me/main/ewm-main-service-spec.json) 
## Private Endpoint for feature:

PATCH: http://localhost:8080/users/{userId}/events/{eventId}/rate/{rateId}
Изменение оценки события

POST: http://localhost:8080/users/{userId}/events/{eventId}/rate
Создание оценки события

DELETE: http://localhost:8080/users/{userId}/events/{eventId}/rate/{rateId}
Удаление оценки события

## Public Endpoint for feature:

GET: http://localhost:8080//rating/users
Рейтинг пользователей (только с оценками)

GET: http://localhost:8080//rating/events
Рейтинг событий (только с оценками)
