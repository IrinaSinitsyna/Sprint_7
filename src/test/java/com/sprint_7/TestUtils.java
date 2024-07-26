package com.sprint_7;

import com.sprint_7.model.CreateCourierRequest;
import io.restassured.response.Response;
import org.hamcrest.core.Is;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.sprint_7.CourierCreationTest.COURIER_CREATION_PATH;
import static io.restassured.RestAssured.given;

public class TestUtils {

    public static final String PRACTICUM = "http://qa-scooter.praktikum-services.ru";

    public static String getStringFromPath(String path) throws IOException {
        File jsonFile = new File(path);
        return Files.readString(jsonFile.toPath());
    }

    public static Response post(String path, CreateCourierRequest body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(path);
    }

    public static <T> Response post(String path, T body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(path);
    }

    public static <T> Response get(String path) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(path);
    }

    public static void assertExpectedBodyAndCode(Response response, String body, int code) {
        response.then().assertThat().body(Is.is(body))
                .and()
                .statusCode(code);
    }

    public static CreateCourierRequest getCourierCreationRequest(String login, String password) {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest();
        createCourierRequest.setLogin(login);
        createCourierRequest.setPassword(password);
        createCourierRequest.setFirstName("name");
        return createCourierRequest;
    }
}
