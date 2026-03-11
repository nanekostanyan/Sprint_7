package createcourier;

import helper.BaseTest;
import helper.CourierApi;
import helper.PrepareTestData;
import io.qameta.allure.Step;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.*;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CreateCourierFieldsTest extends BaseTest {
    private final CourierApi courierApi = new CourierApi();
    private final String login;
    private final String password;
    private final String firstName;
    private final boolean emptyIsNull;
    private CreateCourierRequest request;

    public CreateCourierFieldsTest(String testName, String login, String password, String firstName, boolean emptyIsNull) {
        this.login = PrepareTestData.prepareTestValue(login);
        this.password = PrepareTestData.prepareTestValue(password);
        this.firstName = PrepareTestData.prepareTestValue(firstName);
        this.emptyIsNull = emptyIsNull;
    }

    @Before
    @Step("setUp")
    public final void setUp() {
        if (emptyIsNull) {
            this.request = new CreateCourierRequest();
            if (!login.isBlank()) {
                request.setLogin(login);
            }
            if (!password.isBlank()) {
                request.setPassword(password);
            }
            if (!firstName.isBlank()) {
                request.setFirstName(firstName);
            }
        }
    }

    @Parameterized.Parameters(name = "{0}")
    @Step("Получаем параметры теста")
    public static Object[] getFields() {
        // Первые 4 теста (с false последним параметром) - если значение пустое, поле всё равно будет присутствовать в json,
        // Последние 4 теста (с true последним параметром) - если значение пустое, поля в json будут отсутствовать
        return new Object[][] {
                {
                    "all fields are empty", "", "", "", false
                },
                {
                    "login is empty", "", "some_password", "some_username", false
                },
                {
                    "password is empty", "some_login", "", "some_username", false
                },
                {
                    "first_name is empty", "some_login", "some_password", "", false
                },
                {
                    "all fields are missing", "", "", "", true
                },
                {
                    "login is missing", "", "some_password", "some_username", true
                },
                {
                    "password is missing", "some_login", "", "some_username", true
                },
                {
                    "first_name is missing", "some_login", "some_password", "", true
                },
        };
    }

    @Test
    @Step("Запускаем тест emptyFieldReturnsError")
    public void emptyFieldReturnsError() {
        courierApi.createCourierWRequest(login, password, firstName, request);
        courierApi.checkResponseSC(SC_BAD_REQUEST);
        courierApi.responseHasMessage();
    }

    @Rule
    public TestWatcher watch = new TestWatcher() {
        @Override
        @Step("Удаляем курьера, который мог создаться в провалившемся тесте")
        protected void failed(Throwable e, Description description) {
            // Если тест упал, есть вероятность, что курьер был создан и можно попробовать его удалить
            if (courierApi.getResponseSC() == SC_CREATED) {
                courierApi.deleteCourierWCheck(login, password);
            }
        }
    };
}
