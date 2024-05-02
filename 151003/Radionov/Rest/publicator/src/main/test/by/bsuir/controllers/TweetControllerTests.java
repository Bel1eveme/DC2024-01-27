package by.bsuir.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TweetControllerTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 24110;
    }

    @Test
    public void testGetTweets() {
        given()
                .when()
                .get("/api/v1.0/tweets")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetTweetById() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"title\": \"newTitle\", \"content\": \"content9594\" }")
                .when()
                .post("/api/v1.0/tweets")
                .then()
                .statusCode(201)
                .extract().response();

        long tweetId = response.jsonPath().getLong("id");
        given()
                .pathParam("id", tweetId)
                .when()
                .get("/api/v1.0/tweets/{id}")
                .then()
                .statusCode(200);
        given()
                .pathParam("id", tweetId)
                .when()
                .delete("/api/v1.0/tweets/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteTweet() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"title\": \"newTitle\", \"content\": \"content9594\" }")
                .when()
                .post("/api/v1.0/tweets")
                .then()
                .statusCode(201)
                .extract().response();

        long tweetId = response.jsonPath().getLong("id");

        given()
                .pathParam("id", tweetId)
                .when()
                .delete("/api/v1.0/tweets/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testUpdateTweet() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"title\": \"newTitle\", \"content\": \"content9594\" }")
                .when()
                .post("/api/v1.0/tweets")
                .then()
                .statusCode(201)
                .extract().response();

        long tweetId = response.jsonPath().getLong("id");

        String body = "{ \"id\": " + tweetId + ", \"title\": \"updatedTitle69994\", \"content\": \"updatedContent9402\" }";

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/api/v1.0/tweets")
                .then()
                .statusCode(200)
                .body("title", equalTo("updatedTitle69994"));
        given()
                .pathParam("id", tweetId)
                .when()
                .delete("/api/v1.0/tweets/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetTweetByIdWithWrongArgument() {
        given()
                .pathParam("id", 999999)
                .when()
                .get("/api/v1.0/tweets/{id}")
                .then()
                .statusCode(400)
                .body("errorMessage", equalTo("Tweet not found!"))
                .body("errorCode", equalTo(40004));
    }

    @Test
    public void testDeleteTweetWithWrongArgument() {
        given()
                .pathParam("id", 999999)
                .when()
                .delete("/api/v1.0/tweets/{id}")
                .then()
                .statusCode(400)
                .body("errorMessage", equalTo("Tweet not found!"))
                .body("errorCode", equalTo(40004));
    }

    @Test
    public void testSaveTweetWithWrongTitle() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"title\": \"x\", \"content\": \"content9594\" }")
                .when()
                .post("/api/v1.0/tweets")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetTweetByContentCriteria() {
        String uri = "/api/v1.0/tweets/byCriteria?content=" + "content123";
        String firstContent = given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract()
                .path("[0].content");

        assertEquals("content123", firstContent);
    }

    @Test
    public void testGetTweetByAuthorLoginCriteria() {
        String uri = "/api/v1.0/tweets/byCriteria?authorLogin=" + "12345";
        int firstContent = given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract()
                .path("[0].authorId");

        assertEquals(208, firstContent);
    }
}
