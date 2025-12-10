# TKDB

## Overview

TKDB is a simple, file-backed, in-memory key-value document database built with Java and Spring Boot. It provides a RESTful API for storing and retrieving JSON documents, demonstrated through a basic user management system. The application also includes a simple web interface for interacting with the API.

## Features

*   In-memory data storage using a thread-safe `ConcurrentHashMap`.
*   REST API to create, retrieve, and list user documents.
*   Manual persistence of the in-memory database to a local file (`my_local_data.db`).
*   Functionality to reload the database from the file, overwriting in-memory data.
*   Simple static HTML frontend to interact with all API features.
*   Built with Spring Boot for easy setup and execution.

## How to Run

1.  Make sure you have a Java Development Kit (JDK) and Maven installed.
2.  Navigate to the project's root directory in your terminal.
3.  Run the application using the Spring Boot Maven plugin:
    ```sh
    mvn spring-boot:run
    ```
4.  The application will start and be accessible at `http://localhost:8080`.

## How to Use

Once the application is running, you can access the web interface at `http://localhost:8080`.

*   **Home Page (`/index.html`)**: The main page provides buttons to list all users, save the database to disk, and load the database from disk.
*   **Save a User (`/save_user.html`)**: A form to create or update a user with a specific ID and JSON data.
*   **Get a User (`/get_user.html`)**: A form to retrieve a specific user by their ID.

## API Endpoints

| Method | Path                  | Description                                        |
|--------|-----------------------|----------------------------------------------------|
| `GET`  | `/api/users`          | Retrieves a map of all users.                      |
| `GET`  | `/api/users/{userId}` | Retrieves the JSON data for a specific user.       |
| `PUT`  | `/api/users/{userId}` | Creates or updates a user with the provided JSON.  |
| `POST` | `/api/users/admin/save` | Saves the entire in-memory database to disk.       |
| `POST` | `/api/users/admin/load` | Loads the database from disk into memory.          |