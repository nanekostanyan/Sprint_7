package create_courier;

import com.google.gson.Gson;
import helper.TestHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateCourierFieldsTest extends TestHelper{
    private final String login;
    private final String password;
    private final String firstName;
    private final boolean emptyIsNull;
    private CreateCourierRequest request;
    private int resultCode;

    public CreateCourierFieldsTest(String testName, String login, String password, String firstName, boolean emptyIsNull) {
        this.login = TestHelper.prepareTestValue(login);
        this.password = TestHelper.prepareTestValue(password);
        this.firstName = TestHelper.prepareTestValue(firstName);
        this.emptyIsNull = emptyIsNull;
    }

    @Before
    @Step("setUp")
    public final void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

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
        } else {
            this.request = new CreateCourierRequest(login, password, firstName);
        }
//        Не удалила, чтобы было проще проверить итоговый json
        Gson gson = new Gson();
        System.out.println(gson.toJson(request));
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
        Response response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier");

        resultCode = response.statusCode();
        response.then().statusCode(400);
        response.then().assertThat().body("message", notNullValue());
    }

    @Rule
    public TestWatcher watch = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            // Если тест упал, есть вероятность, что курьер был создан и можно попробовать его удалить
            if (resultCode == 201) {
                deleteCourier(login, password);
            }
        }
    };
}
