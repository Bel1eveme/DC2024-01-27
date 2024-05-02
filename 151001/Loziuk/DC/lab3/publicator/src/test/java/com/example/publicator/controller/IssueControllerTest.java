package com.example.publicator.controller;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IssueControllerTest {
    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 24110;
    }

    @Test
    public void basicPingTest() {
        when().request("GET", "/api/v1.0/issues").then().statusCode(200);
    }

    @Test
    public void testGetIssueById() {
        Map<String, String> issue = new HashMap<>();
        issue.put("creatorId", "1");
        issue.put("title", "1111");
        issue.put("content", "1111");

        Response response = given()
                .contentType("application/json")
                .body(issue)
                .when().post("/api/v1.0/issues").then()
                .statusCode(201)
                .extract().response();
        System.out.println(response);
        int id = response.jsonPath().getInt("id");
        given().pathParam("id", id).
                when().get("/api/v1.0/issues/{id}").then()
                .body("creatorId", equalTo(1))
                .body("title", equalTo("1111"))
                .body("content", equalTo("1111"))
                .statusCode(200);
    }

    @Test
    public void testUpdateIssueById() {
        Map<String, String> issue = new HashMap<>();
        issue.put("creatorId", "1");
        issue.put("title", "1112");
        issue.put("content", "1111");
        Map<String, String> newIssue = new HashMap<>();
        newIssue.put("creatorId", "1");
        newIssue.put("title", "new1112");
        newIssue.put("content", "new1111");
        Response response = given()
                .contentType("application/json")
                .body(issue)
                .when().post("/api/v1.0/issues").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id");
        newIssue.put("id", Integer.toString(id));

        given()
                .contentType("application/json")
                .body(newIssue)
                .when().put("/api/v1.0/issues").then()
                .body("creatorId", equalTo(1))
                .body("title", equalTo("new1112"))
                .body("content", equalTo("new1111"))
                .statusCode(200);
    }

    @Test
    public void testDeleteIssueById() {
        Map<String, String> issue = new HashMap<>();
        issue.put("creatorId", "1");
        issue.put("title", "1113");
        issue.put("content", "1111");

        Response response = given()
                .contentType("application/json")
                .body(issue)
                .when().post("/api/v1.0/issues").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id");
        given().pathParam("id", id).
                when().delete("/api/v1.0/issues/{id}").then()
                .statusCode(204);
    }

    @Test
    public void testPostIssueWithBadRequest() {
        Map<String, String> issue = new HashMap<>();
        issue.put("creatorId", "1");
        issue.put("title", "1");
        issue.put("content", "1");

        given()
                .contentType("application/json")
                .body(issue)
                .when().post("/api/v1.0/issues").then()
                .body("message", equalTo("Bad request"))
                .body("status", equalTo(400))
                .statusCode(400);
    }

    @Test
    public void testGetIssueWithIncorrectId() {
        Map<String, String> issue = new HashMap<>();
        issue.put("creatorId", "1");
        issue.put("title", "1114");
        issue.put("content", "1111");

        Response response = given()
                .contentType("application/json")
                .body(issue)
                .when().post("/api/v1.0/issues").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id") + 10;
        given().pathParam("id", id).
                when().get("/api/v1.0/issues/{id}").then().
                body("message", equalTo("Issue not found"))
                .body("status", equalTo(404))
                .statusCode(404);
    }
}
