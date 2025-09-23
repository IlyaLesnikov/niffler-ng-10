package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.User;
import guru.qa.niffler.util.WebAssertion;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
  private final SelenideElement signUpButton = $("#register-button");
  private final WebAssertion webAssertion;

  public RegisterPage() {
    webAssertion = new WebAssertion();
  }

  public RegisterPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  public RegisterPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  public LoginPage setPasswordSubmit(String passwordSubmit) {
    passwordInput.setValue(passwordSubmit);
    return new LoginPage();
  }

  public LoginPage registerNewUser(User user) {
    setUsername(user.username());
    setPassword(user.password());
    setPasswordSubmit(user.passwordSubmit());
    return new LoginPage();
  }
}
