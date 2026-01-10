package createcourier;

import helper.BaseTest;
import helper.CourierApi;
import helper.PrepareTestData;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CreateCourierTest extends BaseTest {
    private final CourierApi courierApi = new CourierApi();
    private final String login = PrepareTestData.prepareTestValue("user");
    private final String password = PrepareTestData.prepareTestValue("password");
    private final String firstName = PrepareTestData.prepareTestValue("firstName");

    @After
    @Step("tearDown")
    public final void tearDown() {
        courierApi.deleteCourier(login, password);
    }

    @Test
    @Step("Запускаем тест createCourierSuccessfully")
    public void createCourierSuccessfully() {
        courierApi.createCourier(login, password, firstName);
        courierApi.checkResponseSC(SC_CREATED);
        courierApi.checkResponseFieldOk(true);
    }

    @Test
    @Step("Запускаем тест cantCreateDuplicateCourier")
    public void cantCreateDuplicateCourier() {
        courierApi.createCourier(login, password, firstName);
        courierApi.checkResponseSC(SC_CREATED);
        courierApi.checkResponseFieldOk(true);

        courierApi.createCourier(login, password, firstName);
        courierApi.checkResponseSC(SC_CONFLICT);
        courierApi.responseHasMessage();
    }
}
