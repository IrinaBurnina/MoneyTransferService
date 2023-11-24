# Сервис перевода денег
REST-приложение для перевода денег с карты на карту.

### 1. Запуск:
    java -jar /myapp.jar
В терминале необходимо собрать образ на основании упакованного файла (команды clean, package для сборщика проекта Maven), 
который находится в 
###### target/[MoneyTransferService-0.0.1-SNAPSHOT.jar]. 
Команда для создания образа (Docker Desktop должен 
быть запущен):
  ###### docker build -t myapp:latest -t myapp:1.0 .
Затем необходимо запустить Docker-compose.yaml. Контейнер на базе собранного образа будет запущен.
### 2. Порт:
    5500
Но в application.properties можно выбрать другой порт для тестирования в Postman.
### 3. Запросы:
POST -метод, адрес: http://localhost:5500/transfer/transfer

Данные для ввода в форму в формате Json (в тело запроса):

   {
"cardFromNumber": "2222111133334444",
"cardFromValidTill": "12/25",
"cardFromCVV": "123",
"cardToNumber": "1111222233334444",
"amount": {
"value": 200,
"currency": "RUR"
}
    }

GET -метод, адрес: http://localhost:5500/transfer/confirmOperation

Данные для ввода в форму в формате Json (в тело запроса):

{
"operationId": "1",
"code": "0000"
}

### 4. Запуск также возможен через терминал и Dockerfile

в терминале:   
docker run -itd --name mts -p 5500:5500 myapp:1.0

