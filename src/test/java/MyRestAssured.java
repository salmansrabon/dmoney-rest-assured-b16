import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.jupiter.api.Test;
import utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class MyRestAssured {
    Properties prop;
    public MyRestAssured() throws IOException {
        prop=new Properties();
        FileInputStream fs=new FileInputStream("./src/test/resources/config.properties");
        prop.load(fs);
    }
    @Test
    public void doLogin() throws ConfigurationException {
        RestAssured.baseURI="https://dmoney.roadtocareer.net";
        Response res= given().contentType("application/json").body("{\n" +
                "    \"email\":\"admin@dmoney.com\",\n" +
                "    \"password\":\"1234\"\n" +
                "}").when().post("/user/login");
        //System.out.println(res.asString());

        //Extract value from json object
        JsonPath jsonObj=res.jsonPath();
        String token= jsonObj.get("token");
        System.out.println(token);

        Utils.setEnv("token",token);
    }
    @Test
    public void searchUser() throws IOException {
        RestAssured.baseURI="https://dmoney.roadtocareer.net";
        Response res= given().contentType("application/json")
                .header("Authorization","bearer "+ prop.getProperty("token"))
                .when().get("/user/search/id/97969");
        System.out.println(res.asString());
    }

    @Test
    public void createUser(){
        RestAssured.baseURI="https://dmoney.roadtocareer.net";
        Response res= given().contentType("application/json")
                .header("Authorization","bearer "+ prop.getProperty("token"))
                .header("X-AUTH-SECRET-KEY",prop.getProperty("partnerKey"))
                .body("{\n" +
                        "    \"name\":\"b16 user 2\",\n" +
                        "    \"email\":\"b16customer2@dmoney.com\",\n" +
                        "    \"password\":\"1234\",\n" +
                        "    \"phone_number\":\"01307787888\",\n" +
                        "    \"nid\":\"123456789\",\n" +
                        "    \"role\":\"Customer\"\n" +
                        "}")
                .when().post("/user/create");
        System.out.println(res.asString());
    }

}
