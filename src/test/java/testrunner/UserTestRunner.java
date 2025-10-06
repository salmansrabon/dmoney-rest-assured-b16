package testrunner;

import com.github.javafaker.Faker;
import config.Setup;
import config.UserModel;
import controller.UserController;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.Utils;

import java.io.IOException;

public class UserTestRunner extends Setup {
    UserController userController;
    @BeforeClass
    public void myUserController(){
        userController=new UserController(prop);
    }
    @Test(priority = 1, description = "User login")
    public void doLogin() throws ConfigurationException {
        UserModel userModel=new UserModel();
        userModel.setEmail("admin@dmoney.com");
        userModel.setPassword("1234");
        Response res= userController.doLogin(userModel);

        JsonPath jsonObj=res.jsonPath();
        String token= jsonObj.get("token");
        Utils.setEnv("token",token);

        System.out.println(token);
    }
    @Test(priority = 2, description = "Create new user")
    public void createUser() throws ConfigurationException {
        UserModel userModel=new UserModel();
        Faker faker=new Faker();
        userModel.setName(faker.name().fullName());
        userModel.setEmail(faker.internet().emailAddress().toString());
        userModel.setPassword("1234");
        userModel.setPhone_number("0120"+ Utils.generateRandomNumber(1000000,9999999));
        userModel.setNid("123456789");
        userModel.setRole("Customer");
        Response res= userController.createUser(userModel);

        JsonPath jsonObj=res.jsonPath();
        int userId= jsonObj.get("user.id");
        String name= jsonObj.get("user.name");
        String email= jsonObj.get("user.email");
        String password= jsonObj.get("user.password");
        String phoneNumber= jsonObj.get("user.phone_number");

        Utils.setEnv("userId",String.valueOf(userId));
        Utils.setEnv("name",name);
        Utils.setEnv("email",email);
        Utils.setEnv("password",password);
        Utils.setEnv("phoneNumber",phoneNumber);

        System.out.println(res.asString());

        Assert.assertEquals(jsonObj.get("message"),"User created");
    }
    @Test(priority = 3, description = "Search User by id")
    public void searchUserById() throws IOException {
        Response res= userController.searchUser(prop.getProperty("userId"));
        System.out.println(res.asString());

        JsonPath jsonObj=res.jsonPath();
        Assert.assertEquals(jsonObj.get("message"),"User found");

    }
}
