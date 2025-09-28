package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.User;
import guru.qa.niffler.util.WebAssertion;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterPage {

  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
  private final SelenideElement signUpButton = $("#register-button");
  private final SelenideElement signIntButton = $("[class*='_sign-in']");
  private final SelenideElement errorMessage = $(".form__error");
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
    passwordSubmitInput.setValue(passwordSubmit);
    return new LoginPage();
  }

  public RegisterPage submitSignUp() {
    signIntButton.submit();
    return this;
  }

  public RegisterPage registerNewUser(User user) {
    setUsername(user.username());
    setPassword(user.password());
    setPasswordSubmit(user.passwordSubmit());
    submitSignUp();
    return new RegisterPage();
  }

  public LoginPage submitSignIn() {
    signIntButton.click();
    return new LoginPage();
  }

  public RegisterPage shouldErrorMessageVisible() {
    errorMessage.shouldBe(visible);
    return this;
  }

  public RegisterPage shouldErrorMessageEqualText(String errorText) {
    assertEquals(errorText, errorMessage.text());
    return this;
  }
}
