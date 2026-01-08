package courierlogin;

import helper.BaseTest;
import helper.CourierApi;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CourierLoginTest extends CourierApi {
    private final String login = BaseTest.prepareTestValue("user");
    private final String password = BaseTest.prepareTestValue("password");
    private final String firstName = BaseTest.prepareTestValue("firstName");

    @Before
    @Step("setUp")
    public final void setUp() {
        createCourierWCheck(login, password, firstName);
    }

    @After
    @Step("tearDown")
    public final void tearDown() {
        deleteCourierWCheck(login, password);
    }

    @Test
    @Step("Запускаем тест createCourierSuccessfully")
    public void loginCourierSuccessfully() {
        loginCourier(login, password);
        checkResponseSC(SC_OK);
        responseHasId();
    }
}
