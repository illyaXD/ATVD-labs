package org.example;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Lab4ATVD {
    private static final String baseUrl = "https://76ee7148-d788-4aba-b12f-de4b1826f90e.mock.pstmn.io";

    private static final String USER = "/ownerName",
            USER_CREATE = "/createSomething",
            USER_UPDATE = "/updateMe",
            USER_DELETE = "/deleteWorld";

    private String username = "Illia Karapysh";

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void testCreateOK() {
        Map<String, ?> create = Map.of(
                "username", username,
                "firstName", "Illia",
                "lastName", "Karapysh",
                "password", Faker.instance().internet().password()
        );
        given().body(create)
                .post(USER_CREATE + "?permission=yes")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testCreateFail() {
        Map<String, ?> create = Map.of(
                "username", username,
                "firstName", "Illia",
                "lastName", "Karapys",
                "password", Faker.instance().internet().password()
        );
        given().body(create)
                .post(USER_CREATE)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(dependsOnMethods = "testCreateOK")
    public void testGetOK() {
        given()
                .get(USER + "/success")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("name", equalTo(username));
    }

    @Test(dependsOnMethods = "testCreateOK")
    public void testGetForbidden() {
        given()
                .get(USER + "/unsuccess")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test(dependsOnMethods = "testGetOK")
    public void testUpdate() {
        Map<String, ?> update = Map.of(
                "name", username,
                "password", "122-21-1");
        given().body(update)
                .put(USER_UPDATE)
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test(dependsOnMethods = "testGetOK", priority = 1)
    public void testDelete() {
        given()
                .delete(USER_DELETE)
                .then()
                .statusCode(HttpStatus.SC_GONE);
    }
}