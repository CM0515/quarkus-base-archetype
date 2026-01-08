package com.example.microservice.domain.user.rest;

import com.example.microservice.domain.user.dto.CreateUserRequest;
import com.example.microservice.domain.user.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserResourceTest {

    private Long createdUserId;

    @Test
    @Order(1)
    void testCreateUserSuccess() {
        String uniqueEmail = "test_" + System.currentTimeMillis() + "@example.com";

        CreateUserRequest request = new CreateUserRequest(
                "Test User",
                uniqueEmail,
                "1234567890",
                "Test Address",
                User.UserRole.USER
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("success", is(true))
                .body("data.name", equalTo("Test User"))
                .extract()
                .response();

        // Guardar el ID del usuario creado
        createdUserId = response.jsonPath().getLong("data.id");
    }

    @Test
    @Order(2)
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
    @Order(3)
    void testGetUserById() {
        given()
                .when()
                .get("/api/users/" + createdUserId)
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @Order(4)
    void testGetUserByIdNotFound() {
        given()
                .when()
                .get("/api/users/999999")
                .then()
                .statusCode(404)
                .body("code", equalTo("ERR_001"));
    }

    @Test
    @Order(5)
    void testUpdateUser() {
        String uniqueEmail = "updated_" + System.currentTimeMillis() + "@example.com";

        CreateUserRequest request = new CreateUserRequest(
                "Updated User",
                uniqueEmail,
                "9876543210",
                "Updated Address",
                User.UserRole.ADMIN
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/users/" + createdUserId)
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @Order(6)
    void testDeleteUser() {
        given()
                .when()
                .delete("/api/users/" + createdUserId)
                .then()
                .statusCode(204);
    }

    @Test
    @Order(7)
    void testCreateUserValidationError() {
        CreateUserRequest request = new CreateUserRequest(
                "", // Nombre vacío
                "invalid-email",
                null,
                null,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }
}
