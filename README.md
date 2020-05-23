# Star Wars Planet Manager

## Description

This Micro Service can make CRUD functions (using REST format) to manager planets. Planet contain name, terrain, climate and
quantity of films that this planet already appearred before (we get this using Swapi API).

## Functionalities

1. GET BY ID : http://localhost:8080/planets/{mongoEntityID} (GET)
2. DELETE BY ID: http://localhost:8080/planets/{mongoEntityID} (DELETE)
3. GET ALL: http://localhost:8080/planets (GET) - can pass request param (name) to filter by name
4. CREATE: http://localhost:8080/planets (POST) - this method can return not found if planet does not exist on API, validating by name (this is not necessary considerating that client only send valid names getting planet names on API)

Body example

{
	"name": "Geonosis",
	"climate": "Artificial Temperature",
	"terrain": "Rock"
}

## How to make it work

* First, you need setup Java 8 Environment (JDK, JRE... )
* After, open this project in any IDE (i recommend Intellij)
* Ensure that you have Lombok plugin on IDE, as well configuration to put Lombok Annotation Processor
* Run MongoDB on localhost, creating database.

## Architecture

Principal resource and unique is a SOL

1. Uses DTO object to receive and tranfer JSONs between controller
2. Controller is responsible for communication between frontend and backend.
3. Service is responsible for business logic, that makes all fluxs
4. StarWarsClient is responsible for send requests to Swapi API

I don´t put this on Eureka Server in due of have horizontal scalability, but is very simple, just run Eureka Server and put
this MS to register on it using application.yml



