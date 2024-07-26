package com.sprint_7;

import com.google.gson.Gson;
import com.sprint_7.model.OrderRequest;
import com.sprint_7.model.OrderResponse;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.sprint_7.TestUtils.*;
import static org.junit.Assert.assertNotEquals;

@RunWith(Parameterized.class)
public class OrdersCreationTest {

    private static final String ORDERS_PATH = "/api/v1/orders";

    private List<String> color;

    public OrdersCreationTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Collection provider() {
        return Arrays.asList(new Object[][]{
                {List.of()},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()},
                {null},
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = PRACTICUM;
    }

    @Test
    @DisplayName("Check that order can be created with different color options")
    public void shouldCreateOrderWithDifferentColors() throws IOException {
        String requestBody = getStringFromPath("src/test/resources/test-data/CreateOrderRequest.json");
        OrderRequest orderRequest = new Gson().fromJson(requestBody, OrderRequest.class);
        orderRequest.setColor(color);

        Response response = post(ORDERS_PATH, orderRequest);
        int expectedCode = 201;
        response.then().assertThat().statusCode(expectedCode);
        OrderResponse orderResponse = response.as(OrderResponse.class);

        assertNotEquals(orderResponse.getTrack(), 0);
    }
}
