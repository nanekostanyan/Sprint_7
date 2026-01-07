import helper.TestHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest extends TestHelper {
    @Before
    @Step("setUp")
    public final void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @Step("Запускаем тест getOrdersList")
    public void getOrdersList() {
        Response response = given()
                .get("/api/v1/orders");

        response.then().statusCode(200);
        response.then().assertThat().body("orders", notNullValue());
    }
}
