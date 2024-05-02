package com.example.publicator.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class TagControllerTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 24110;
    }

    @Test
    public void basicPingTest() {
        when().request("GET", "/api/v1.0/tags").then().statusCode(200);
    }

    @Test
    public void testGetTagById() {
        Map<String, String> tag = new HashMap<>();
        tag.put("issueId", "1");
        tag.put("name", "1111");

        Response response = given()
                .contentType("application/json")
                .body(tag)
                .when().post("/api/v1.0/tags").then()
                .statusCode(201)
                .extract().response();
        System.out.println(response);
        int id = response.jsonPath().getInt("id");
        given().pathParam("id", id).
                when().get("/api/v1.0/tags/{id}").then()
                //.body("issueId", equalTo(1))
                .body("name", equalTo("1111"))
                .statusCode(200);
    }

    @Test
    public void testUpdateTagById() {
        Map<String, String> tag = new HashMap<>();
        tag.put("issueId", "1");
        tag.put("name", "1111");
        Map<String, String> newTag = new HashMap<>();
        newTag.put("issueId", "1");
        newTag.put("name", "new1111");

        Response response = given()
                .contentType("application/json")
                .body(tag)
                .when().post("/api/v1.0/tags").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id");
        newTag.put("id", Integer.toString(id));

        given()
                .contentType("application/json")
                .body(newTag)
                .when().put("/api/v1.0/tags").then()
                //.body("issueId", equalTo(1))
                .body("name", equalTo("new1111"))
                .statusCode(200);
    }

    @Test
    public void testDeleteTagById() {
        Map<String, String> tag = new HashMap<>();
        tag.put("issueId", "1");
        tag.put("name", "1111");

        Response response = given()
                .contentType("application/json")
                .body(tag)
                .when().post("/api/v1.0/tags").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id");
        given().pathParam("id", id).
                when().delete("/api/v1.0/tags/{id}").then()
                .statusCode(204);
    }

    @Test
    public void testPostTagWithBadRequest() {
        Map<String, String> tag = new HashMap<>();
        tag.put("issueId", "1");
        tag.put("name", "1");

        given()
                .contentType("application/json")
                .body(tag)
                .when().post("/api/v1.0/tags").then()
                .body("message", equalTo("Bad request"))
                .body("status", equalTo(400))
                .statusCode(400);
    }

    @Test
    public void testGetTagWithIncorrectId() {
        Map<String, String> tag = new HashMap<>();
        tag.put("issueId", "1");
        tag.put("name", "1111");

        Response response = given()
                .contentType("application/json")
                .body(tag)
                .when().post("/api/v1.0/tags").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id") + 10;
        given().pathParam("id", id).
                when().get("/api/v1.0/tags/{id}").then().
                body("message", equalTo("Tag not found"))
                .body("status", equalTo(404))
                .statusCode(404);
    }
}
