# movies-actors
A backend application for movies and actors management via REST API.

## Requirements
1. Java 11 (or newer)
2. Maven 3.2.1 (or newer)

## Usage
1. Build the application using maven:
    ```bash
    mvn clean package
    ```
2. Run the application:
    ```batch
    java -jar target/movies-actors-1.0.1.jar
    ```
    
## Some REST API examples
1. List movies with pagination support
```url
http://localhost:8081/v1/movies?limit=1&offset=1
```
2. Movies search
```url
http://localhost:8081/v1/movies/search?keyword=pacino
```
