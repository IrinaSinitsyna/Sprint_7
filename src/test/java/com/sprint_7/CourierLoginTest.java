package com.sprint_7;

import com.sprint_7.model.CourierLoginRequest;
import com.sprint_7.model.CourierLoginResponse;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static com.sprint_7.CourierCreationTest.createCourierAndValidate;
import static com.sprint_7.TestUtils.*;
import static org.junit.Assert.assertNotEquals;

public class CourierLoginTest {

    private static final String COURIER_LOGIN_PATH = "/api/v1/courier/login";

    @Before
    public void setUp() {
        RestAssured.baseURI = PRACTICUM;
    }

    @Test
    @DisplayName("Check that login works as expected for newly created courier")
    public void shouldLoginSuccessfullyWhenRequestIsCorrect() {
        String login = UUID.randomUUID().toString();
        String password = "password";
        createCourierAndValidate(login, password);

        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);
        courierLoginRequest.setPassword(password);
        int expectedCode = 200;

        Response response = post(COURIER_LOGIN_PATH, courierLoginRequest);
        CourierLoginResponse courierLoginResponse = response.as(CourierLoginResponse.class);

        response.then().assertThat().statusCode(expectedCode);
        assertNotEquals(courierLoginResponse.getId(), 0);
    }

    @Test
    @DisplayName("Check that login won't work without password")
    public void shouldNotLoginSuccessfullyWhenPasswordIsMissing() {
        String login = UUID.randomUUID().toString();
        String password = "password";
        createCourierAndValidate(login, password);

        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);
        String expectedBody = "{\"code\":400,\"message\":\"Недостаточно данных для входа\"}";
        int expectedCode = 400;

        Response response = post(COURIER_LOGIN_PATH, courierLoginRequest);

        assertExpectedBodyAndCode(response, expectedBody, expectedCode);
    }

    @Test
    @DisplayName("Check that login won't work without login")
    public void shouldNotLoginSuccessfullyWhenLoginIsMissing() {
        String login = UUID.randomUUID().toString();
        String password = "password";
        createCourierAndValidate(login, password);

        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setPassword(password);
        String expectedBody = "{\"code\":400,\"message\":\"Недостаточно данных для входа\"}";
        int expectedCode = 400;

        Response response = post(COURIER_LOGIN_PATH, courierLoginRequest);

        assertExpectedBodyAndCode(response, expectedBody, expectedCode);
    }

    @Test
    @DisplayName("Check that login won't work with incorrect password")
    public void shouldNotLoginSuccessfullyWhenPasswordIsIncorrect() {
        String login = UUID.randomUUID().toString();
        String password = "password";
        createCourierAndValidate(login, password);

        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);
        courierLoginRequest.setPassword(password + "incorrect");
        String expectedBody = "{\"code\":404,\"message\":\"Учетная запись не найдена\"}";
        int expectedCode = 404;

        Response response = post(COURIER_LOGIN_PATH, courierLoginRequest);

        assertExpectedBodyAndCode(response, expectedBody, expectedCode);
    }

    @Test
    @DisplayName("Check that login won't work with not existing login")
    public void shouldNotLoginSuccessfullyWhenLoginDoesntExist() {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(UUID.randomUUID().toString());
        courierLoginRequest.setPassword("password");
        String expectedBody = "{\"code\":404,\"message\":\"Учетная запись не найдена\"}";
        int expectedCode = 404;

        Response response = post(COURIER_LOGIN_PATH, courierLoginRequest);

        assertExpectedBodyAndCode(response, expectedBody, expectedCode);
    }
}
