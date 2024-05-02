package by.bsuir.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StickerControllerTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 24110;
    }

    @Test
    public void testGetStickers() {
        given()
                .when()
                .get("/api/v1.0/stickers")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetStickerById() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"name4845\" }")
                .when()
                .post("/api/v1.0/stickers")
                .then()
                .statusCode(201)
                .extract().response();

        long stickerId = response.jsonPath().getLong("id");
        given()
                .pathParam("id", stickerId)
                .when()
                .get("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(200);

        given()
                .pathParam("id", stickerId)
                .when()
                .delete("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteSticker() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"name4845\" }")
                .when()
                .post("/api/v1.0/stickers")
                .then()
                .statusCode(201)
                .extract().response();

        long stickerId = response.jsonPath().getLong("id");

        given()
                .pathParam("id", stickerId)
                .when()
                .delete("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testUpdateSticker() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"name4845\" }")
                .when()
                .post("/api/v1.0/stickers")
                .then()
                .statusCode(201)
                .extract().response();

        long stickerId = response.jsonPath().getLong("id");

        String body = "{ \"id\": " + stickerId + ", \"tweetId\": 8, \"name\": \"updatedname45295\" }";

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/api/v1.0/stickers")
                .then()
                .statusCode(200)
                .body("name", equalTo("updatedname45295"));

        given()
                .pathParam("id", stickerId)
                .when()
                .delete("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetStickerByIdWithWrongArgument() {
        given()
                .pathParam("id", 999999)
                .when()
                .get("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(400)
                .body("errorMessage", equalTo("Sticker not found!"))
                .body("errorCode", equalTo(40004));
    }

    @Test
    public void testDeleteStickerWithWrongArgument() {
        given()
                .pathParam("id", 999999)
                .when()
                .delete("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(400)
                .body("errorMessage", equalTo("Sticker not found!"))
                .body("errorCode", equalTo(40004));
    }

    @Test
    public void testGetStickerByTweetId() {
        Response tweetResponse = given()
                .contentType(ContentType.JSON)
                .body("{ \"title\": \"title3194650\", \"content\": \"content9594\" }")
                .when()
                .post("/api/v1.0/tweets")
                .then()
                .statusCode(201)
                .extract().response();

        long tweetId = tweetResponse.jsonPath().getLong("id");

        String body = "{ \"tweetId\": " + tweetId + ", \"name\": \"name4845\" }";

        Response stickerResponse = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/stickers")
                .then()
                .statusCode(201)
                .extract().response();

        long stickerId = stickerResponse.jsonPath().getLong("id");

        given()
                .pathParam("id", tweetId)
                .when()
                .get("/api/v1.0/stickers/byTweet/{id}")
                .then()
                .statusCode(200);

        given()
                .pathParam("id", stickerId)
                .when()
                .delete("/api/v1.0/stickers/{id}")
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
        String body = "{ \"name\": \"aaa\"}";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/stickers")
                .then()
                .statusCode(201)
                .extract().response();

        Integer stickerId1 = response.jsonPath().getInt("id");

        body = "{ \"name\": \"bbb\"}";
        response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/stickers")
                .then()
                .statusCode(201)
                .extract().response();

        Integer stickerId2 = response.jsonPath().getInt("id");
        String uri = "/api/v1.0/stickers?pageNumber=0&pageSize=10&sortBy=id&sortOrder=asc";
        Integer id = given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract()
                .path("[0].id");

        assertEquals(stickerId1, id);
        uri = "/api/v1.0/stickers?pageNumber=0&pageSize=10&sortBy=id&sortOrder=desc";
        id = given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract()
                .path("[0].id");

        assertEquals(stickerId2, id);

        given()
                .pathParam("id", stickerId1)
                .when()
                .delete("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", stickerId2)
                .when()
                .delete("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testFindAllOrderByContent(){
        String body = "{ \"name\": \"aaa\"}";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/stickers")
                .then()
                .statusCode(201)
                .extract().response();

        Integer stickerId1 = response.jsonPath().getInt("id");

        body = "{ \"name\": \"bbb\"}";
        response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/stickers")
                .then()
                .statusCode(201)
                .extract().response();

        Integer stickerId2 = response.jsonPath().getInt("id");
        String uri = "/api/v1.0/stickers?pageNumber=0&pageSize=10&sortBy=name&sortOrder=asc";
        String content = given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract()
                .path("[0].name");

        assertEquals("aaa", content);
        uri = "/api/v1.0/stickers?pageNumber=0&pageSize=10&sortBy=name&sortOrder=desc";
        content = given()
                .contentType(ContentType.JSON)
                .when()
                .get(uri)
                .then()
                .statusCode(200)
                .extract()
                .path("[0].name");

        assertEquals("bbb", content);

        given()
                .pathParam("id", stickerId1)
                .when()
                .delete("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", stickerId2)
                .when()
                .delete("/api/v1.0/stickers/{id}")
                .then()
                .statusCode(204);
    }
}

