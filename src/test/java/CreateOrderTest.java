import helper.TestHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.CreateOrderRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final List<CreateOrderRequest.Color> colors;

    public CreateOrderTest(String testName, List<CreateOrderRequest.Color> colors) {
        this.colors = colors;
    }

    @Before
    @Step("setUp")
    public final void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Parameterized.Parameters(name = "{0}")
    @Step("Получаем параметры теста")
    public static Object[] getFields() {
        return new Object[][] {
                {
                    "without colors", null,
                },
                {
                    "with empty colors", new ArrayList<>(),
                },
                {
                    "gray color", new ArrayList<>(Arrays.asList(CreateOrderRequest.Color.GREY)),
                },
                {
                    "black color", new ArrayList<>(Arrays.asList(CreateOrderRequest.Color.BLACK)),
                },
                {
                    "both colors", new ArrayList<>(Arrays.asList(CreateOrderRequest.Color.GREY, CreateOrderRequest.Color.BLACK)),
                },
                {
                    "same color twice", new ArrayList<>(Arrays.asList(CreateOrderRequest.Color.BLACK, CreateOrderRequest.Color.BLACK)),
                }
        };
    }

    @Test
    @Step("Запускаем тест createOrder")
    public void createOrder() {
        CreateOrderRequest request = createRequest(colors);

        Response response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/orders");

        response.then().statusCode(201);
        response.then().assertThat().body("track", notNullValue());
    }

    private CreateOrderRequest createRequest(List<CreateOrderRequest.Color> colors) {
        Random rand = new Random();
        final int bound = 1000;

        CreateOrderRequest request = new CreateOrderRequest();
        request.setFirstName(TestHelper.prepareTestValue("Naruto"));
        request.setLastName(TestHelper.prepareTestValue("Uchiha"));
        request.setAddress(TestHelper.prepareTestValue("Konoha, 142 apt."));
        request.setMetroStation(rand.nextInt(bound));
        request.setPhone(TestHelper.createPhone());
        request.setRentTime(rand.nextInt(bound));
        request.setDeliveryDate(TestHelper.createDate());
        request.setComment(TestHelper.prepareTestValue("Saske, come back to Konoha"));
        if (colors != null) {
            request.setColor(colors);
        }

        return request;
    }
}
