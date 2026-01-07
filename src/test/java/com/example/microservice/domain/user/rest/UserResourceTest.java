package com.example.microservice.domain.user.rest;

import com.example.microservice.domain.user.dto.CreateUserRequest;
import com.example.microservice.domain.user.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
class UserResourceTest {

    @Test
    void testCreateUserSuccess() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .phone("1234567890")
                .address("Test Address")
                .role(User.UserRole.USER)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("success", is(true))
                .body("data.name", equalTo("Test User"))
                .body("data.email", equalTo("test@example.com"));
    }

    @Test
    void testGetAllUsers() {
        given()
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("data", instanceOf(Iterable.class));
    }

    @Test
    void testGetUserById() {
        given()
                .when()
                .get("/api/users/1")
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    void testGetUserByIdNotFound() {
        given()
                .when()
                .get("/api/users/999999")
                .then()
                .statusCode(404)
                .body("code", equalTo("ERR_001"));
    }

    @Test
    void testUpdateUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("Updated User")
                .email("updated@example.com")
                .phone("9876543210")
                .address("Updated Address")
                .role(User.UserRole.ADMIN)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/users/1")
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    void testDeleteUser() {
        given()
                .when()
                .delete("/api/users/1")
                .then()
                .statusCode(204);
    }

    @Test
    void testCreateUserValidationError() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("") // Nombre vacío
                .email("invalid-email")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }
}
