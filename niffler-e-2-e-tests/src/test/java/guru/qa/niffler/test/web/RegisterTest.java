package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.User;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.util.RandomDataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegisterTest {

  private final Config CFG = Config.getInstance();

  @Test
  @DisplayName("Регистрация пользователя с валидными данными")
  void shouldRegisterUserWithValidData() {
    User user = new User(RandomDataUtil.username(), RandomDataUtil.password(), RandomDataUtil.password());
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccountSubmit()
        .registerNewUser(user)
        .submitSignIn()
        .login(user.username(), user.password())
        .shouldStatisticsHeaderVisible()
        .shouldHistoryOfSpendingsVisible();
  }

  @Test
  @DisplayName("Регистрация пользоваталя с невалидным логином")
  void registeringUserWithInvalidUsernameTest() {
    User user = new User("aa", RandomDataUtil.password(), RandomDataUtil.password());
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccountSubmit()
        .registerNewUser(user)
        .submitSignIn()
        .login(user.username(), user.password())
        .shouldStatisticsHeaderVisible()
        .shouldHistoryOfSpendingsVisible();
  }

  @Test
  @DisplayName("Регистрация пользоваталя с невалидным паролем")
  void registeringUserWithInvalidPasswordTest() {
    User user = new User("aa", RandomDataUtil.password(), RandomDataUtil.password());
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccountSubmit()
        .registerNewUser(user)
        .submitSignIn()
        .login(user.username(), user.password())
        .shouldStatisticsHeaderVisible()
        .shouldHistoryOfSpendingsVisible();
  }

  @Test
  @DisplayName("Регистрация пользоваталя с разными паролями")
  void registeringUsersWithOtherUsersTest() {
    User user = new User(RandomDataUtil.username()  , RandomDataUtil.password(), RandomDataUtil.password());
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccountSubmit()
        .registerNewUser(user)
        .shouldErrorMessageEqualText("Passwords should be equal");
  }
}
