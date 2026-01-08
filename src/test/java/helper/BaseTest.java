package helper;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.BeforeClass;

import java.util.Random;
import java.util.UUID;


public class BaseTest {
    @BeforeClass
    @Step("setUp")
    public static void globalSetUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter()); // Нашла такой вариант логирования, но
//        решила оставить логирование только для упавших тестов
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
        return rand.nextInt(100) + 2000 + "-" + (rand.nextInt(11)+1) + "-" + rand.nextInt(28);
    }
}
