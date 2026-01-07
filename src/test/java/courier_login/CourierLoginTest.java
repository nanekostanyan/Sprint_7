package courier_login;

import helper.TestHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.CourierLoginRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends TestHelper {
    private final String login = TestHelper.prepareTestValue("user");
    private final String password = TestHelper.prepareTestValue("password");
    private final String firstName = TestHelper.prepareTestValue("firstName");

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

    @Test
    @Step("Запускаем тест createCourierSuccessfully")
    public void loginCourierSuccessfully() {
        CourierLoginRequest request = new CourierLoginRequest(login, password);

        Response response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier/login");

        response.then().statusCode(200);
        response.then().assertThat().body("id", notNullValue());
    }
}
