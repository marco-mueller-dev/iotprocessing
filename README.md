# Iot Processing

In diesem Projekt im Bereich "Cloud Native Software Engineering" sendet eine fiktive IoT Api per Kafka
daten an einen Processor, dieser speichert die gesendeten Daten persistent auf einer Postgres Datenbank.
Im Frontend sieht man die gesendeten Temparatur Daten und kann die Rate einstellen, mit der der neue 
Daten gesendet werden. Die Anwendung ist auf der Google Cloud deployed.

## Quickstart

### Lokal
- docker compose up -d 
- warten auf Start
- im Browser unter (http://localhost:8080/iot-homepage.html) das Frontend aufrufen

### im Browser Cluster aufrufen 

-34.107.26.0:8080/iot-homepage.html aufrufen

## Komponenten

### Common

- geteilte Dto's, Reposistories

### iot-generator

- erzeugt Daten mit einer Fehlerchance auf unrealistische Temparaturen
- sendet die Daten per Kafka an den Processor

## iot-processor

- verarbeitet und speichert die Daten persistent auf Postgres

## iot-frontend

- stellt die Dtaen auf der Website dar


## Bilder
<img width="2536" height="943" alt="image" src="https://github.com/user-attachments/assets/61fd5a86-e765-4be1-a15d-6f02afac1bf1" />



<img width="680" height="418" alt="image" src="https://github.com/user-attachments/assets/0c3ac307-1e2c-43c6-a37f-3ac34bba8864" />


<img width="960" height="335" alt="image" src="https://github.com/user-attachments/assets/3a5747d7-514b-454b-a14c-e580cc260f72" />
<img width="2207" height="410" alt="image" src="https://github.com/user-attachments/assets/fe54e9c2-5ad2-451b-9db8-fe42a2d03eef" />



