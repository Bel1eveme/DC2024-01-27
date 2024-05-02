package by.bsuir.controllers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthorControllerTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 24110;
    }

    @Test
    public void testGetAuthors() {
        given()
                .when()
                .get("/api/v1.0/authors")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetAuthorById() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"newAuthor\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/authors")
                .then()
                .statusCode(201)
                .extract().response();

        long authorId = response.jsonPath().getLong("id");
        given()
                .pathParam("id", authorId)
                .when()
                .get("/api/v1.0/authors/{id}")
                .then()
                .statusCode(200);

        given()
                .pathParam("id", authorId)
                .when()
                .delete("/api/v1.0/authors/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteAuthor() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"newAuthor\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/authors")
                .then()
                .statusCode(201)
                .extract().response();

        long authorId = response.jsonPath().getLong("id");

        given()
                .pathParam("id", authorId)
                .when()
                .delete("/api/v1.0/authors/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testUpdateAuthor() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"newAuthor\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/authors")
                .then()
                .statusCode(201)
                .extract().response();

        long authorId = response.jsonPath().getLong("id");

        String body = "{ \"id\": " + authorId + ", \"login\": \"updatedAuthor1091\", \"password\": \"updatedPass5907\", \"firstname\": \"updatedFirstname7007\", \"lastname\": \"updatedLastname3768\" }";

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/api/v1.0/authors")
                .then()
                .statusCode(200)
                .body("login", equalTo("updatedAuthor1091"));

        given()
                .pathParam("id", authorId)
                .when()
                .delete("/api/v1.0/authors/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetAuthorByIdWithWrongArgument() {
        given()
                .pathParam("id", 999999)
                .when()
                .get("/api/v1.0/authors/{id}")
                .then()
                .statusCode(400)
                .body("errorMessage", equalTo("Author not found!"))
                .body("errorCode", equalTo(40004));
    }

    @Test
    public void testDeleteAuthorWithWrongArgument() {
        given()
                .pathParam("id", 999999)
                .when()
                .delete("/api/v1.0/authors/{id}")
                .then()
                .statusCode(400)
                .body("errorMessage", equalTo("Author not found!"))
                .body("errorCode", equalTo(40004));
    }

    @Test
    public void testSaveAuthorWithWrongLogin() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"x\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/authors")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetAuthorByTweetId() {
        Response authorResponse = given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"newAuthor\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/authors")
                .then()
                .statusCode(201)
                .extract().response();

        long authorId = authorResponse.jsonPath().getLong("id");
        String body = "{ \"authorId\": "+ authorId +", \"title\": \"newTitle123\", \"content\": \"content9594\" }";

        Response tweetResponse = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/tweets")
                .then()
                .statusCode(201)
                .extract().response();

        long tweetId = tweetResponse.jsonPath().getLong("id");

        given()
                .pathParam("id", tweetId)
                .when()
                .get("/api/v1.0/authors/byTweet/{id}")
                .then()
                .statusCode(200)
                .body("login", equalTo("newAuthor"));

        given()
                .pathParam("id", authorId)
                .when()
                .delete("/api/v1.0/authors/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", tweetId)
                .when()
                .delete("/api/v1.0/tweets/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testFindAllOrderById(){
        String body = "{ \"login\": \"111\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/authors")
                .then()
                .statusCode(201)
                .extract().response();

        Integer authorId1 = response.jsonPath().getInt("id");

        body = "{ \"login\": \"zzzzz-is-very-very-very-very-very-very-long-more-than-64-symbols\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }";
        response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/authors")
                .then()
                .statusCode(201)
                .extract().response();

        Integer authorId2 = response.jsonPath().getInt("id");
        String uri = "/api/v1.0/authors?pageNumber=0&pageSize=10&sortBy=id&sortOrder=desc";
        Integer content = given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract()
                .path("[0].id");

        assertEquals(authorId2, content);

        given()
                .pathParam("id", authorId1)
                .when()
                .delete("/api/v1.0/authors/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", authorId2)
                .when()
                .delete("/api/v1.0/authors/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testFindAllOrderByLogin(){
        String body = "{ \"login\": \"111\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/authors")
                .then()
                .statusCode(201)
                .extract().response();

        Integer authorId1 = response.jsonPath().getInt("id");

        body = "{ \"login\": \"zzzzz-is-very-very-very-very-very-very-long-more-than-64-symbols\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }";
        response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/authors")
                .then()
                .statusCode(201)
                .extract().response();

        Integer authorId2 = response.jsonPath().getInt("id");
        String uri = "/api/v1.0/authors?pageNumber=0&pageSize=10&sortBy=login&sortOrder=desc";
        String content = given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract()
                .path("[0].login");

        assertEquals("zzzzz-is-very-very-very-very-very-very-long-more-than-64-symbols", content);

        uri = "/api/v1.0/authors?pageNumber=0&pageSize=10&sortBy=login&sortOrder=asc";
        content = given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract()
                .path("[0].login");

        assertEquals("111", content);

        given()
                .pathParam("id", authorId1)
                .when()
                .delete("/api/v1.0/authors/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", authorId2)
                .when()
                .delete("/api/v1.0/authors/{id}")
                .then()
                .statusCode(204);
    }
}
