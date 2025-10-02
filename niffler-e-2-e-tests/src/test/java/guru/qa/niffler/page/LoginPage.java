package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.ErrorComponent;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement submitBtn = $("#login-button");
  private final SelenideElement createNewAccountButton = $("#register-button");
  private final ErrorComponent errorComponent;

  public LoginPage() {
    errorComponent = new ErrorComponent();
  }

  public MainPage login(String username, String password) {
    usernameInput.val(username);
    passwordInput.val(password);
    submitBtn.click();
    return new MainPage();
  }

  public RegisterPage createNewAccountSubmit() {
    createNewAccountButton.click();
    return new RegisterPage();
  }

  public LoginPage shouldErrorEqualText(String errorText) {
    errorComponent.shouldErrorEqualText(errorText);
    return this;
  }
}
