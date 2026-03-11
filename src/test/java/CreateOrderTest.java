import helper.BaseTest;
import helper.OrderApi;
import io.qameta.allure.Step;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.CreateOrderRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {
    private final OrderApi orderApi = new OrderApi();
    private final List<CreateOrderRequest.Color> colors;

    public CreateOrderTest(String testName, List<CreateOrderRequest.Color> colors) {
        this.colors = colors;
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
        orderApi.createOrderWColors(colors);
        orderApi.checkResponseSC(SC_CREATED);
        orderApi.responseHasTrack();
    }
}
