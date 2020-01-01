# Запуск сервера с использованием порта 8080

```
gradlew startServerAt8080
````

# Примеры запросов

```
Запрос: http://localhost:8080/?year=2020
Ответ: {"errorCode":200,"dataMessage":"14/09/20"}
```

```
Запрос: http://localhost:8080?yer=2020
Ответ: {"errorCode":400,"dataMessage":"invalid parameter"}
```

```
Запрос: http://localhost:8080/?year=202t
Ответ: {"errorCode":400,"dataMessage":"invalid year"}
```
