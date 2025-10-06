package controller;

import config.UserModel;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class UserController {
    Properties prop;
    public UserController(Properties prop){
        RestAssured.baseURI="https://dmoney.roadtocareer.net";
        this.prop=prop;
    }
    public Response doLogin(UserModel userModel){
        Response res= given().contentType("application/json").body(userModel).when().post("/user/login");
        return res;
    }
    public Response createUser(UserModel userModel){
        Response res= given().contentType("application/json")
                .header("Authorization","bearer "+ prop.getProperty("token"))
                .header("X-AUTH-SECRET-KEY",prop.getProperty("partnerKey"))
                .body(userModel)
                .when().post("/user/create");
        return res;
    }
    public Response searchUser(String userId) throws IOException {
        Response res= given().contentType("application/json")
                .header("Authorization","bearer "+ prop.getProperty("token"))
                .when().get("/user/search/id/"+userId);
        return res;
    }
}
