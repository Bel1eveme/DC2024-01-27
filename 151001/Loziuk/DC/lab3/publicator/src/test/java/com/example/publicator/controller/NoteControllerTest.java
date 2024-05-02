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

public class NoteControllerTest {
    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 24110;
    }

    @Test
    public void basicPingTest() {
        when().request("GET", "/api/v1.0/notes").then().statusCode(200);
    }

    @Test
    public void testGetNoteById() {
        Map<String, String> note = new HashMap<>();
        note.put("issueId", "1");
        note.put("id", "1");
        note.put("content", "1111");

        Response response = given()
                .contentType("application/json")
                .body(note)
                .header("Accept-Language", "RU")
                .when().post("/api/v1.0/notes").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id");
        given().pathParam("id", id).
                when().get("/api/v1.0/notes/{id}").then().
                body("issueId", equalTo(1))
                .body("content", equalTo("1111")).statusCode(200);
    }

    @Test
    public void testUpdateNoteById() {
        Map<String, String> note = new HashMap<>();
        note.put("issueId", "1");
        note.put("content", "1111");
        Map<String, String> newNote = new HashMap<>();
        newNote.put("id", "0");
        newNote.put("issueId", "1");
        newNote.put("content", "new1111");

        Response response = given()
                .contentType("application/json")
                .body(note)
                .header("Accept-Language", "RU")
                .when().post("/api/v1.0/notes").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id");
        newNote.put("id", Integer.toString(id));

        given()
                .contentType("application/json")
                .body(newNote)
                .header("Accept-Language", "RU")
                .when().put("/api/v1.0/notes")
                .then()
                .body("issueId", equalTo(1))
                .body("content", equalTo("new1111")).statusCode(200);
    }

    @Test
    public void testDeleteNoteById() {
        Map<String, String> note = new HashMap<>();
        note.put("issueId", "1");
        note.put("content", "1111");

        Response response = given()
                .contentType("application/json")
                .body(note)
                .header("Accept-Language", "RU")
                .when().post("/api/v1.0/notes").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id");
        given().pathParam("id", id).
                when().delete("/api/v1.0/notes/{id}").then()
                .statusCode(204);
    }

    @Test
    public void testPostNoteWithBadRequest() {
        Map<String, String> note = new HashMap<>();
        note.put("issueId", "1");
        note.put("content", "1");

        given()
                .contentType("application/json")
                .body(note)
                .header("Accept-Language", "RU")
                .when().post("/api/v1.0/notes").then()
                //.body("message", equalTo("Bad request"))
                .body("status", equalTo(400))
                .statusCode(400);
    }

    @Test
    public void testGetNoteWithIncorrectId() {
        Map<String, String> note = new HashMap<>();
        note.put("issueId", "1");
        note.put("content", "1111");

        Response response = given()
                .contentType("application/json")
                .body(note)
                .header("Accept-Language", "RU")
                .when().post("/api/v1.0/notes").then()
                .statusCode(201)
                .extract().response();

        int id = response.jsonPath().getInt("id") + 10;
        given().pathParam("id", id).
                when().get("/api/v1.0/notes/{id}").then().
                body("message", equalTo("Note not found"))
                .body("status", equalTo(404))
                .statusCode(404);
    }
}
