package com.example.publicator.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreatorControllerTest {
    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 24110;
    }

    @Test
    public void basicPingTest() {
        when().request("GET", "/api/v1.0/creators").then().statusCode(200);
    }

    @Test
    public void testGetCreatorById() {
        Map<String, String> creator = new HashMap<>();
        creator.put("login", "lion1");
        creator.put("password", "11111111");
        creator.put("firstname", "11");
        creator.put("lastname", "11");

        Response response = given()
                .contentType("application/json")
                .body(creator)
                .when().post("/api/v1.0/creators").then()
                .statusCode(201)
                .extract().response();
        System.out.println(response);
        int id = response.jsonPath().getInt("id");
        given().pathParam("id", id).
                when().get("/api/v1.0/creators/{id}").then()
                .body("login", equalTo("lion1"))
                .body("password", equalTo("11111111"))
                .body("firstname", equalTo("11"))
                .body("lastname", equalTo("11"))
                .statusCode(200);
    }

    @Test
    public void testUpdateCreatorById() {
        Map<String, String> creator = new HashMap<>();
        creator.put("login", "lion11");
        creator.put("password", "11111111");
        creator.put("firstname", "11");
        creator.put("lastname", "11");
        Map<String, String> newCreator = new HashMap<>();
        newCreator.put("login", "new11");
        newCreator.put("password", "new11111111");
        newCreator.put("firstname", "new11");
        newCreator.put("lastname", "new11");

        Response response = given()
                .contentType("application/json")
                .body(creator)
                .when().post("/api/v1.0/creators").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id");
        newCreator.put("id", Integer.toString(id));

        given()
                .contentType("application/json")
                .body(newCreator)
                .when().put("/api/v1.0/creators").then()
                .body("login", equalTo("new11"))
                .body("password", equalTo("new11111111"))
                .body("firstname", equalTo("new11"))
                .body("lastname", equalTo("new11"))
                .statusCode(200);
    }

    @Test
    public void testDeleteCreatorById() {
        Map<String, String> creator = new HashMap<>();
        creator.put("login", "lion111");
        creator.put("password", "11111111");
        creator.put("firstname", "11");
        creator.put("lastname", "11");


        Response response = given()
                .contentType("application/json")
                .body(creator)
                .when().post("/api/v1.0/creators").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id");
        given().pathParam("id", id).
                when().delete("/api/v1.0/creators/{id}").then()
                .statusCode(204);
    }

    @Test
    public void testPostCreatorWithBadRequest() {
        Map<String, String> creator = new HashMap<>();
        creator.put("login", "1");
        creator.put("password", "1234567");
        creator.put("firstname", "1");
        creator.put("lastname", "1");

        given()
                .contentType("application/json")
                .body(creator)
                .when().post("/api/v1.0/creators").then()
                .body("message", equalTo("Bad request"))
                .body("status", equalTo(400))
                .statusCode(400);
    }

    @Test
    public void testGetCreatorWithIncorrectId() {
        Map<String, String> creator = new HashMap<>();
        creator.put("login", "lion1111");
        creator.put("password", "11111111");
        creator.put("firstname", "11");
        creator.put("lastname", "11");


        Response response = given()
                .contentType("application/json")
                .body(creator)
                .when().post("/api/v1.0/creators").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id") + 10;
        given().pathParam("id", id).
                when().get("/api/v1.0/creators/{id}").then().
                body("message", equalTo("Creator not found")).
                body("status", equalTo(404))
                .statusCode(404);
    }
}
