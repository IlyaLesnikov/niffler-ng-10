package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.xpath;

public class ProfilePage {
  private final ElementsCollection buttons = $$("button");
  private final SelenideElement showArchivedButton = $("input[class*='PrivateSwitchBase-input']");

  public ProfilePage submitArchiveCategory(String categoryName) {
    String locator = "//span[text()='%s']/../..//button[@aria-label='Archive category']".formatted(categoryName);
    $(xpath(locator)).click();
    return this;
  }

  public ProfilePage submitUnarchiveCategory(String categoryName) {
    String locator = "//span[text()='%s']/../..//button[@aria-label='Unarchive category']".formatted(categoryName);
    $(xpath(locator)).click();
    return this;
  }

  public ProfilePage submitButton(String textElement) {
    buttons.find(text(textElement)).click();
    return this;
  }

  public ProfilePage submitShowArchive() {
    showArchivedButton.click();
    return this;
  }

  public ProfilePage shouldCategoryVisible(String categoryName) {
    String locator = "//span[text()='%s']".formatted(categoryName);
    $(xpath(locator)).shouldBe(visible);
    return this;
  }

  public ProfilePage shouldCategoryNonVisible(String categoryName) {
    String locator = "//span[text()='%s']".formatted(categoryName);
    $(xpath(locator)).shouldNotBe(visible);
    return this;
  }
}
