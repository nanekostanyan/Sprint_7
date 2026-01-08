package helper;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.CourierLoginRequest;
import pojo.CourierLoginResponse;
import pojo.CreateCourierRequest;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CourierApi extends BaseTest {
    private Response response;

    // Разделила создание курьера на два метода.
    // С *WCheck, для вызова в setUp, сразу проверяя код ответа;
    // Без *WCheck, для непосредственного тестирования. Их ответы нужно проверять отдельно.
    @Step("Создаём курьера перед тестом")
    public void createCourierWCheck(String login, String password, String firstName) {
        createCourier(login, password, firstName);
        // Не проверяем бизнес логику, только убеждаемся что действительно можем продолжить тест
        checkResponseSC(SC_CREATED);
        response = null; // Обнулила, чтобы подготовительные работы не мешали самому тесту. Если в ходе теста не
        // получим ответ, мы хотим об этом знать и упасть на проверках ответа
    }

    @Step("createCourierWRequest")
    public void createCourierWRequest(String login, String password, String firstName, CreateCourierRequest request) {
        if (request == null) {
            request = new CreateCourierRequest(login, password, firstName);
        }

        response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier");
    }

    @Step("createCourier")
    public void createCourier(String login, String password, String firstName) {
        CreateCourierRequest request = new CreateCourierRequest(login, password, firstName);

        response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier");
    }

    @Step("Удаляем созданного курьера в конце теста")
    public void deleteCourierWCheck(String login, String password){
        deleteCourier(login, password);
        // Не проверяем бизнес логику, только убеждаемся что удаление действительно произведено и ранее созданный курьер
        // не повлияет на последующие тесты
        checkResponseSC(SC_OK);
        response = null;
    }

    @Step("deleteCourier")
    public void deleteCourier(String login, String password){
        int courierID = getCourierID(login, password);

        response = given()
                .header("Content-type", "application/json")
                .delete(String.format("/api/v1/courier/%d", courierID));
    }

    @Step("Получаем ID курьера")
    private int getCourierID(String login, String password) {
        loginCourier(login, password);
        checkResponseSC(SC_OK);

        CourierLoginResponse courierLoginResponse = response.body().as(CourierLoginResponse.class);
        int id = courierLoginResponse.getId();
        response = null;
        return id;
    }

    @Step("loginCourier")
    public void loginCourier(String login, String password) {
        CourierLoginRequest request = new CourierLoginRequest(login, password);
        loginCourierWRequest(request);
    }

    @Step("loginCourierWRequest")
    public void loginCourierWRequest(CourierLoginRequest request) {
        response = given()
                .header("Content-type", "application/json")
                .body(request)
                .post("/api/v1/courier/login");
    }

    @Step("Получаем код ответа")
    public int getResponseSC() {
        assertNotNull(response);
        return response.statusCode();
    }

    @Step("Проверяем код ответа")
    public void checkResponseSC(int sc) {
        assertNotNull(response);
        response.then().statusCode(sc);
    }

    @Step("Проверяем, есть ли в теле ответа ключ ok")
    public void checkResponseFieldOk(boolean val) {
        assertNotNull(response);
        response.then().assertThat().body("ok", equalTo(val));
    }

    @Step("Проверяем, есть ли в теле ответа ключ message")
    public void responseHasMessage() {
        assertNotNull(response);
        response.then().assertThat().body("message", notNullValue());
    }

    @Step("Проверяем, есть ли в теле ответа ключ id")
    public void responseHasId() {
        assertNotNull(response);
        response.then().assertThat().body("id", notNullValue());
    }
}
