package helper;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.CreateOrderRequest;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;

public class OrderApi {
    private Response response;

    @Step("callOrdersList")
    public void callOrdersList() {
        response = given()
                .get("/api/v1/orders");
    }

    @Step("createOrderWColors")
    public void createOrderWColors(List<CreateOrderRequest.Color> colors) {
        CreateOrderRequest request = createRequest(colors);

        response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/orders");
    }

    @Step("Генерируем запрос для создания заказа")
    private CreateOrderRequest createRequest(List<CreateOrderRequest.Color> colors) {
        Random rand = new Random();
        final int bound = 1000;

        CreateOrderRequest request = new CreateOrderRequest();
        request.setFirstName(PrepareTestData.prepareTestValue("Naruto"));
        request.setLastName(PrepareTestData.prepareTestValue("Uchiha"));
        request.setAddress(PrepareTestData.prepareTestValue("Konoha, 142 apt."));
        request.setMetroStation(rand.nextInt(bound));
        request.setPhone(PrepareTestData.createPhone());
        request.setRentTime(rand.nextInt(bound));
        request.setDeliveryDate(PrepareTestData.createDate());
        request.setComment(PrepareTestData.prepareTestValue("Saske, come back to Konoha"));
        if (colors != null) {
            request.setColor(colors);
        }

        return request;
    }

    @Step("Проверяем код ответа")
    public void checkResponseSC(int sc) {
        assertNotNull(response);
        response.then().statusCode(sc);
    }

    @Step("Проверяем, есть ли в теле ответа ключ orders")
    public void responseHasOrders() {
        assertNotNull(response);
        response.then().assertThat().body("orders", notNullValue());
    }

    @Step("Проверяем, есть ли в теле ответа ключ track")
    public void responseHasTrack() {
        assertNotNull(response);
        response.then().assertThat().body("track", notNullValue());
    }
}
