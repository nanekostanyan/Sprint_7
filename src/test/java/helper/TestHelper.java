package helper;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.CourierLoginRequest;
import pojo.CourierLoginResponse;
import pojo.CreateCourierRequest;

import java.util.Random;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class TestHelper {
    @Step("Создаём курьера перед тестом")
    public void createCourier(String login, String password, String firstName) {
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);

        Response response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier");
        // Не проверяем бизнес логику, только убеждаемся что действительно можем продолжить тест
        response.then().statusCode(201);
    }

    @Step("Удаляем созданного курьера в конце теста")
    public void deleteCourier(String login, String password){
        int courierID = getCourierID(login, password);

        Response response = given()
                .header("Content-type", "application/json")
                .delete(String.format("/api/v1/courier/%d", courierID));
        // Не проверяем бизнес логику, только убеждаемся что удаление действительно произведено и ранее созданный курьер
        // не повлияет на последующие тесты
        response.then().statusCode(200);
    }

    @Step("Получаем ID курьера")
    private int getCourierID(String login, String password) {
        CourierLoginRequest request = new CourierLoginRequest(login, password);

        Response response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier/login");
        response.then().statusCode(200);

        CourierLoginResponse courierLoginResponse = response.body().as(CourierLoginResponse.class);
        return courierLoginResponse.getId();
    }

    public static String prepareTestValue(String val) {
        if (val.isBlank()) {
            return val;
        }
        return val + "_" + UUID.randomUUID();
    }

    public static String createPhone() {
        Random rand = new Random();
        return "+7 800 " + rand.nextInt(1000) + " " + rand.nextInt(100) + " " + rand.nextInt(100);
    }

    public static String createDate() {
        Random rand = new Random();
        return rand.nextInt(100) + 2000 + "-" + rand.nextInt(12) + "-" + rand.nextInt(28);
    }
}
