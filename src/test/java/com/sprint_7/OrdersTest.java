package com.sprint_7;

import com.sprint_7.model.GetOrdersResponse;
import com.sprint_7.model.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.sprint_7.TestUtils.PRACTICUM;
import static com.sprint_7.TestUtils.get;
import static java.lang.Integer.valueOf;
import static org.junit.Assert.assertNotEquals;

public class OrdersTest {

    private static final String ORDERS_PATH = "/api/v1/orders";

    @Before
    public void setUp() {
        RestAssured.baseURI = PRACTICUM;
    }

    @Test
    @DisplayName("Check that /api/v1/orders will return orders")
    public void shouldCreateOrderWithDifferentColors() {
        Response response = get(ORDERS_PATH);
        int expectedCode = 200;
        response.then().assertThat().statusCode(expectedCode);

        GetOrdersResponse getOrdersResponse = response.as(GetOrdersResponse.class);

        int amountOfOrders = getOrdersResponse.getOrders().size();
        assertNotEquals(amountOfOrders, 0);
        for (Order order : getOrdersResponse.getOrders()) {
            assertNotEquals(order.getId(), valueOf(0));
            assertNotEquals(order.getCreatedAt(), null);
            assertNotEquals(order.getUpdatedAt(), null);
        }
    }
}
