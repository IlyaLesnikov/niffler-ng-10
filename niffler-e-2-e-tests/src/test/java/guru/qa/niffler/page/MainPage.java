package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPage {
  private final ElementsCollection tableRows = $$("#spendings tr");
  private final SelenideElement historySpendingsHeader = $("#spendings h2");
  private final SelenideElement statisticsHeader = $("#stat h2");
  private final SelenideElement icon = $("[data-testid='PersonIcon']");
  private final ElementsCollection windowIconElement = $$("[role='menuitem']");

  public EditSpendingPage editSpending(String description) {
    tableRows.find(text(description)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public MainPage checkThatTableContains(String description) {
    tableRows.find(text(description)).should(visible);
    return this;
  }

  public MainPage shouldHistoryOfSpendingsVisible() {
    historySpendingsHeader.shouldBe(visible, Duration.ofMillis(10_000L));
    assertEquals("History of Spendings", historySpendingsHeader.text());
    return this;
  }

  public MainPage shouldStatisticsHeaderVisible() {
    historySpendingsHeader.shouldBe(visible);
    assertEquals("Statistics", statisticsHeader.text());
    return this;
  }

  public MainPage submitIcon() {
    icon.shouldBe(visible).click();
    return this;
  }

  public ProfilePage submitIconElement(String elementText) {
    SelenideElement element = windowIconElement.find(text(elementText));
    element.click();
    return new ProfilePage();
  }
}
