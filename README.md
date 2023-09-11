# java-explore-with-me
Template repository for ExploreWithMe project.

https://github.com/CyberCoHuK/java-explore-with-me/pull/12

# Private Endpoint for feature:

PATCH: http://localhost:8080/users/{userId}/events/{eventId}/rate/{rateId}
Изменение оценки события

POST: http://localhost:8080/users/{userId}/events/{eventId}/rate
Создание оценки события

DELETE: http://localhost:8080/users/{userId}/events/{eventId}/rate/{rateId}
Удаление оценки события

# Public Endpoint for feature:

GET: http://localhost:8080//rating/users
Рейтинг пользователей (только с оценками)

GET: http://localhost:8080//rating/events
Рейтинг событий (только с оценками)
