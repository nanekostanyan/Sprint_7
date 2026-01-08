import helper.OrderApi;
import io.qameta.allure.Step;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class GetOrdersListTest extends OrderApi {
    @Test
    @Step("Запускаем тест successfullyReceivedListOfOrders")
    public void successfullyReceivedListOfOrders() {
        callOrdersList();
        checkResponseSC(SC_OK);
        responseHasOrders();
    }
}
