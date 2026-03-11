package courierlogin;

import helper.BaseTest;
import helper.CourierApi;
import helper.PrepareTestData;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CourierLoginTest extends BaseTest {
    private final CourierApi courierApi = new CourierApi();
    private final String login = PrepareTestData.prepareTestValue("user");
    private final String password = PrepareTestData.prepareTestValue("password");
    private final String firstName = PrepareTestData.prepareTestValue("firstName");

    @Before
    @Step("setUp")
    public final void setUp() {
        courierApi.createCourierWCheck(login, password, firstName);
    }

    @After
    @Step("tearDown")
    public final void tearDown() {
        courierApi.deleteCourierWCheck(login, password);
    }

    @Test
    @Step("Запускаем тест createCourierSuccessfully")
    public void loginCourierSuccessfully() {
        courierApi.loginCourier(login, password);
        courierApi.checkResponseSC(SC_OK);
        courierApi.responseHasId();
    }
}
