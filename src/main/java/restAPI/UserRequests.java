package restAPI;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import user.User;
import user.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserRequests extends SpecificationData {

    @Step("Создание нового пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post("auth/register")
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(userCredentials)
                .when()
                .post("auth/login")
                .then();
    }

    @Step("Удаление пользователя")
    public void deleteUser(String token) {
        given()
                .spec(getBaseReqSpec())
                .header("Authorization", token)
                .when()
                .delete("auth/user")
                .then();
    }
}

