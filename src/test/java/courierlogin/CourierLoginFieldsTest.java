package courierlogin;

import helper.BaseTest;
import helper.CourierApi;
import helper.PrepareTestData;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.CourierLoginRequest;
import java.util.Random;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CourierLoginFieldsTest extends BaseTest {
    private final CourierApi courierApi = new CourierApi();
    private final String login;
    private final boolean spoilLogin;
    private final String password;
    private final boolean spoilPassword;
    private final String firstName;

    public CourierLoginFieldsTest(String testName, boolean spoilLogin, boolean spoilPassword) {
        this.login = PrepareTestData.prepareTestValue("login");
        this.spoilLogin = spoilLogin;
        this.password = PrepareTestData.prepareTestValue("password");
        this.spoilPassword = spoilPassword;
        this.firstName = "Bella"; // Не влияет на тест, думаю что требованием отсутствия хардкода можно пренебречь
    }

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

    @Parameterized.Parameters(name = "{0}")
    @Step("Получаем параметры теста")
    public static Object[] getFields() {
        return new Object[][] {
                {
                        "problem with field: all fields", true, true
                },
                {
                        "problem with field: login", true, false
                },
                {
                        "problem with field: password", false, true
                },
        };
    }

    @Test
    @Step("Запускаем тест cantLoginCourierWithEmptyFields")
    public void cantLoginCourierWithEmptyFields() {
        courierApi.loginCourier(
                spoilLogin ? "" : login,
                spoilPassword ? "" : password
        );
        courierApi.checkResponseSC(SC_BAD_REQUEST);
        courierApi.responseHasMessage();
    }

    @Test
    @Step("Запускаем тест cantLoginCourierWithMissingFields")
    public void cantLoginCourierWithMissingFields() {
        CourierLoginRequest request = new CourierLoginRequest();
        if (!spoilLogin) {
            request.setLogin(login);
        }
        if (!spoilPassword) {
            request.setPassword(password);
        }

        courierApi.loginCourierWRequest(request);
        courierApi.checkResponseSC(SC_BAD_REQUEST);
        courierApi.responseHasMessage();
    }

    @Test
    @Step("Запускаем тест cantLoginCourierWithSpoiledFields")
    public void cantLoginCourierWithSpoiledFields() {
        courierApi.loginCourier(
                spoilLogin ? spoilString(login) : login,
                spoilPassword ? spoilString(password) : password
        );
        courierApi.checkResponseSC(SC_NOT_FOUND);
        courierApi.responseHasMessage();
    }

    @Step("Портим входную строку")
    private String spoilString(String str) {
        // Просто удалю один случайный символ
        Random rand = new Random();
        int randomIndex = rand.nextInt(str.length());
        return str.substring(0, randomIndex) + str.substring(randomIndex + 1);
    }
}
