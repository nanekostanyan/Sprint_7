package courier_login;

import com.google.gson.Gson;
import helper.TestHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.CourierLoginRequest;

import java.util.Random;

import static org.hamcrest.Matchers.notNullValue;
import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CourierLoginFieldsTest extends TestHelper {
    private final String login;
    private final boolean spoilLogin;
    private final String password;
    private final boolean spoilPassword;
    private final String firstName;

    public CourierLoginFieldsTest(String testName, boolean spoilLogin, boolean spoilPassword) {
        this.login = TestHelper.prepareTestValue("login");
        this.spoilLogin = spoilLogin;
        this.password = TestHelper.prepareTestValue("password");
        this.spoilPassword = spoilPassword;
        this.firstName = "Bella"; // Не влияет на тест, думаю что требованием отсутствия хардкода можно пренебречь
    }

    @Before
    @Step("setUp")
    public final void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

        createCourier(login, password, firstName);
    }

    @After
    @Step("tearDown")
    public final void tearDown() {
        deleteCourier(login, password);
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
        CourierLoginRequest request = new CourierLoginRequest(
                spoilLogin ? "" : login,
                spoilPassword ? "" : password
        );

        Gson gson = new Gson();
        System.out.printf("login: `%s`; password: `%s`; json: `%s`", login, password, gson.toJson(request));

        Response response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier/login");

        response.then().statusCode(400);
        response.then().assertThat().body("message", notNullValue());
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

        Gson gson = new Gson();
        System.out.printf("login: `%s`; password: `%s`; json: `%s`", login, password, gson.toJson(request));

        Response response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier/login");

        response.then().statusCode(400);
        response.then().assertThat().body("message", notNullValue());
    }

    @Test
    @Step("Запускаем тест cantLoginCourierWithSpoiledFields")
    public void cantLoginCourierWithSpoiledFields() {
        CourierLoginRequest request = new CourierLoginRequest(
                spoilLogin ? spoilString(login) : login,
                spoilPassword ? spoilString(password) : password
        );

        Gson gson = new Gson();
        System.out.printf("login: `%s`; password: `%s`; json: `%s`", login, password, gson.toJson(request));

        Response response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier/login");

        response.then().statusCode(404);
        response.then().assertThat().body("message", notNullValue());
    }

    private String spoilString(String str) {
        // Просто удалю один случайный символ
        Random rand = new Random();
        int randomIndex = rand.nextInt(str.length());
        return str.substring(0, randomIndex) + str.substring(randomIndex + 1);
    }
}
