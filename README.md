# Iot Processing

In diesem Projekt im Bereich "Cloud Native Software Engineering" sendet eine fiktive IoT Api per Kafka
daten an einen Processor, dieser speicehrt die gesendeten Daten persistent auf einer Postgres Datenbank.
Im Frontend sieht man die gesendeten Temparatur Daten und kann die Rate einstellen, mit der der neue 
Daten gesendet werden.

## Quickstart

-docker compose up -d
-warten auf Start
-im Browser unter (http://localhost:8080/iot-homepage.html) das Frontend aufrufen

## Komponenten

### Common

-geteilte Dto's, Reposistories

### iot-generator

-erzeugt Daten mit einer Fehlerchance auf unrealistische Temparaturen
-senet die Daten per Kafka an den Processor

## iot-processor

-verarbeitet und speichert die Daten persistent auf Postgres

## iot-frontend

-stellt die Dtaen auf der Website dar


## Bilder
<img width="1912" height="906" alt="image" src="https://github.com/user-attachments/assets/1e75010f-7acb-43a7-a704-c643b3b1690f" />
<img width="680" height="418" alt="image" src="https://github.com/user-attachments/assets/0c3ac307-1e2c-43c6-a37f-3ac34bba8864" />
<img width="960" height="335" alt="image" src="https://github.com/user-attachments/assets/3a5747d7-514b-454b-a14c-e580cc260f72" />



