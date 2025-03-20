package org.example;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;



public class Lab3ATVD {
    private static final String baseUrl = "https://petstore.swagger.io/v2";

    private static final String USER = "/user",
            USER_USERNAME = USER + "/{username}",
            USER_LOGIN = USER + "/login",
            USER_LOGOUT = USER + "/logout";
    private String username;
    private String firstName;
    private static final String PET = "/pet";
    private long petId;
    private String petName;
    private String petCategory;
    private String petStatus;


    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test (priority = 1)
    public void verifyLoginAction() {
        Map<String, ?> body = Map.of(
                "username", "Illia Karapysh",
                "password", "122-21-1"
        );
        Response response = given().body(body)
                .get(USER_LOGIN);
        response.then()
                .statusCode(HttpStatus.SC_OK);
        RestAssured.requestSpecification
                .sessionId(response.jsonPath()
                        .get("message")
                        .toString()
                        .replaceAll("[^0-9]", ""));
    }

    @Test(priority = 2, dependsOnMethods = "verifyLoginAction")
    public void verifyCreateAction() {
        username = Faker.instance().name().username();
        firstName = Faker.instance().harryPotter().character();
        Map<String, ?> body = Map.of(
                "username", username,
                "firstName", firstName,
                "lastName", Faker.instance().gameOfThrones().character(),
                "email", Faker.instance().internet().emailAddress(),
                "password", Faker.instance().internet().password(),
                "phone", Faker.instance().phoneNumber().phoneNumber(),
                "userStatus", Integer.valueOf("1")
        );
        given().body(body)
                .post(USER)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(priority = 3, dependsOnMethods = "verifyCreateAction")
    public void verifyGetAction() {
        given().pathParam("username", username)
                .get(USER_USERNAME)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("firstName", equalTo(firstName));
    }

    @Test(priority = 4, dependsOnMethods = "verifyGetAction")
    public void verifyDeleteAction() {
        given().pathParam("username", username)
                .delete(USER_USERNAME)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyLoginAction", priority = 4)
    public void verifyLogoutAction() {
        given().get(USER_LOGOUT)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }


    @Test
    public void verifyAddPet() {
        petId = 122211;
        petName = "Barsik";
        petCategory = "Cat";
        petStatus = "available";

        Map<String, ?> add = Map.of(
                "id", petId,
                "name", petName,
                "category", Map.of(
                        "id", 0,
                        "name", petCategory
                ),
                "status", petStatus);
        given().body(add)
                .post(PET)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyAddPet")
    public void verifyGetPet() {
        given().pathParam("petId", petId)
                .get(PET + "/{petId}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", equalTo((int)petId))
                .body("name", equalTo(petName))
                .body("category.name", equalTo(petCategory))
                .body("status", equalTo(petStatus));
    }

    @Test(dependsOnMethods = "verifyGetPet")
    public void verifyUpdatePet() {
        petStatus = "sold";
        Map<String, ?> update = Map.of(
                "id", petId,
                "name", petName,
                "category", Map.of(
                        "id", 0,
                        "name", petCategory
                ),
                "status", petStatus);
        given().body(update)
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo((int)petId))
                .body("name", equalTo(petName))
                .body("category.name", equalTo(petCategory))
                .body("status", equalTo(petStatus));
    }
}


