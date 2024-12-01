# Homnework 3

This project implements an **LLMServer** service, which provides an API endpoint for generating text. The project uses Akka HTTP for the server and includes tests for its routes and functionality using ScalaTest.

## Table of Contents
- [System Architecture](#system-architecture)
- [Project Overview](#project-overview)
- [Requirements](#requirements)
- [Installation and Setup](#installation-and-setup)
- [Running the Tests](#running-the-tests)
- [Usage](#usage)

## System Architecture

The system consists of the following components:

- **Akka HTTP Server**: The primary component of the system, responsible for routing HTTP requests and generating responses.
- **Model Logic**: This component simulates a simple text generation model (for example, based on pre-defined templates or algorithms).
- **Route Handling**: Defines the API endpoints and ensures that requests are correctly processed and responses are formatted as expected.
- **Testing**: Unit and integration tests for ensuring that the API functions as expected under various conditions.
## Project Overview

The **LLMServer** provides a service that exposes an API endpoint `/generate` which accepts text-based queries and returns generated responses. The server is built using **Akka HTTP** and **ScalaTest** for unit testing. The project is designed to demonstrate knowledge of distributed systems, Akka HTTP server implementation, and functional testing.

## Requirements

The project requires the following tools:
- **Java 21+**
- **Scala 2.12.x**
- **Akka HTTP** (version `10.2.x`)
- **Akka TestKit** (version `2.6.x`)
- **sbt** (Scala build tool)
- **ScalaTest** for testing

#### Ensure the following dependencies are included in the `build.sbt` file:

- `akka-http` version `10.2.x`
- `akka-testkit` version `2.6.x`
- `akka-http-testkit` version `10.2.x`
- `scala-test` version `3.1.x`

### Dependencies:
- `akka-http` version `10.2.x`
- `akka-testkit` version `2.6.x`
- `akka-http-testkit` version `10.2.x`
- `scala-test` for testing

## Installation and Setup

### Make sure to run LLMSever first before running the client

Make sure sbt (Scala Build Tool) is installed on your system. If not, you can download it from here.

Install Dependencies
Once you've cloned the repository, navigate to the project directory and run:

```
sbt update
This will download the required dependencies for the project.
```

### Clone the repository

First, clone the repository to your local machine:

```
git clone https://github.com/yourusername/LLMServer.git
cd LLMServer
```


## Running the Server
Starting the Server
Once the project is set up, you can start the server using sbt. In the project directory, run:

```
sbt run
```

This will start the server locally on localhost at port 8080 (by default). You should see output indicating the server has started and is listening for requests.
## Test Coverage and Output
Tests will be run for various components of the server, including:

### Run below

```
sbt test
```
Route handling.
Input validation.
Server error simulation.
Upon successful test execution, sbt will provide a summary of the passed and failed tests.

## Usage
Once the setup is complete and all dependencies are resolved, you can start the server.

Running the Server Locally
To run the server locally, use the following command:

bash
Copy code
sbt run
This will start the server on localhost at port 8080 by default.