package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.User;
import guru.qa.niffler.page.component.ErrorComponent;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
  private final SelenideElement signUpButton = $("#register-button");
  private final SelenideElement signInButton = $("[class*='_sign-in']");
  private final ErrorComponent errorComponent;


  public RegisterPage() {
    errorComponent = new ErrorComponent();
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
    signUpButton.submit();
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
    signInButton.click();
    return new LoginPage();
  }

  public RegisterPage shouldErrorMessageEqualText(String errorText) {
    errorComponent.shouldErrorEqualText(errorText);
    return this;
  }
}
