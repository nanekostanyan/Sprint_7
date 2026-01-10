import helper.BaseTest;
import helper.OrderApi;
import io.qameta.allure.Step;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class GetOrdersListTest extends BaseTest {
    private final OrderApi orderApi = new OrderApi();

    @Test
    @Step("Запускаем тест successfullyReceivedListOfOrders")
    public void successfullyReceivedListOfOrders() {
        orderApi.callOrdersList();
        orderApi.checkResponseSC(SC_OK);
        orderApi.responseHasOrders();
    }
}
