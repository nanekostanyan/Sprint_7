package create_courier;

import helper.TestHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest extends TestHelper {
    private final String login = TestHelper.prepareTestValue("user");
    private final String password = TestHelper.prepareTestValue("password");
    private final String firstName = TestHelper.prepareTestValue("firstName");

    @Before
    @Step("setUp")
    public final void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @After
    @Step("tearDown")
    public final void tearDown() {
        deleteCourier(login, password);
    }

    @Test
    @Step("Запускаем тест createCourierSuccessfully")
    public void createCourierSuccessfully() {
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(request)
                        .post("/api/v1/courier");

        response.then().statusCode(201);
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @Step("Запускаем тест cantCreateDuplicateCourier")
    public void cantCreateDuplicateCourier() {
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);

        Response response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier");
        response.then().statusCode(201);
        response.then().assertThat().body("ok", equalTo(true));

        response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier");
        response.then().statusCode(409);
        response.then().assertThat().body("message", notNullValue());
    }
}
