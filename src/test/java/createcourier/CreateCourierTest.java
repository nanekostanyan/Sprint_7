package createcourier;

import helper.BaseTest;
import helper.CourierApi;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CreateCourierTest extends CourierApi {
    private final String login = BaseTest.prepareTestValue("user");
    private final String password = BaseTest.prepareTestValue("password");
    private final String firstName = BaseTest.prepareTestValue("firstName");

    @After
    @Step("tearDown")
    public final void tearDown() {
        deleteCourier(login, password);
    }

    @Test
    @Step("Запускаем тест createCourierSuccessfully")
    public void createCourierSuccessfully() {
        createCourier(login, password, firstName);
        checkResponseSC(SC_CREATED);
        checkResponseFieldOk(true);
    }

    @Test
    @Step("Запускаем тест cantCreateDuplicateCourier")
    public void cantCreateDuplicateCourier() {
        createCourier(login, password, firstName);
        checkResponseSC(SC_CREATED);
        checkResponseFieldOk(true);

        createCourier(login, password, firstName);
        checkResponseSC(SC_CONFLICT);
        responseHasMessage();
    }
}
