package com.sprint_7;

import com.sprint_7.model.CreateCourierRequest;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static com.sprint_7.TestUtils.*;

public class CourierCreationTest {

    public static final String COURIER_CREATION_PATH = "/api/v1/courier";
    private static final String SUCCESS_BODY = "{\"ok\":true}";
    private static final String NOT_ENOUGH_DATA_BODY = "{\"code\":400,\"message\":\"Недостаточно данных для создания учетной записи\"}";
    private static final int SUCCESS_CODE = 201;

    @Before
    public void setUp() {
        RestAssured.baseURI = PRACTICUM;
    }

    @Test
    @DisplayName("Check that courier creation returns expected response and code")
    public void shouldCreateCourierSuccessfullyWhenCorrectRequestIsPassed() {
        createCourierAndValidate(UUID.randomUUID().toString(), "password");
    }

    @Test
    @DisplayName("Check that courier with the same login won't be created")
    public void shouldNotCreateCourierSuccessfullyWhenDuplicateRequestIsPassed() {
        CreateCourierRequest request = getCourierCreationRequest(UUID.randomUUID().toString(), "password");
        String expectedBody = "{\"code\":409,\"message\":\"Этот логин уже используется. Попробуйте другой.\"}";
        int expectedCode = 409;

        Response firstResponse = post(COURIER_CREATION_PATH, request);
        Response secondResponse = post(COURIER_CREATION_PATH, request);

        assertExpectedBodyAndCode(firstResponse, SUCCESS_BODY, SUCCESS_CODE);
        assertExpectedBodyAndCode(secondResponse, expectedBody, expectedCode);
    }

    @Test
    @DisplayName("Check that courier without login won't be created")
    public void shouldNotCreateCourierSuccessfullyWhenLoginIsMissing() throws IOException {
        String requestBodyWithoutLogin = getStringFromPath("src/test/resources/test-data/CourierCreationRequestMissingLogin.json");
        int expectedCode = 400;

        Response response = post(COURIER_CREATION_PATH, requestBodyWithoutLogin);

        assertExpectedBodyAndCode(response, NOT_ENOUGH_DATA_BODY, expectedCode);
    }

    @Test
    @DisplayName("Check that courier without password won't be created")
    public void shouldNotCreateCourierSuccessfullyWhenPasswordIsMissing() throws IOException {
        String requestBodyWithoutPassword = getStringFromPath("src/test/resources/test-data/CourierCreationRequestMissingPassword.json");
        int expectedCode = 400;

        Response response = post(COURIER_CREATION_PATH, requestBodyWithoutPassword);

        assertExpectedBodyAndCode(response, NOT_ENOUGH_DATA_BODY, expectedCode);
    }

    @Step("Creates courier using POST /api/v1/courier")
    public static void createCourierAndValidate(String login, String password) {
        CreateCourierRequest request = getCourierCreationRequest(login, password);

        Response response = post(COURIER_CREATION_PATH, request);

        assertExpectedBodyAndCode(response, SUCCESS_BODY, SUCCESS_CODE);
    }
}
