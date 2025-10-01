package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ErrorComponent {
  private final SelenideElement error = $(".form__error");

  public void shouldErrorEqualText(String errorText) {
    error.shouldHave(text(errorText));
  }
}
