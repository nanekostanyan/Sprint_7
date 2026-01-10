package helper;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class BaseTest {
    @BeforeClass
    @Step("setUp")
    public static void globalSetUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); // Нашла такой вариант логирования, но
//        решила оставить логирование только для упавших тестов
    }
}
