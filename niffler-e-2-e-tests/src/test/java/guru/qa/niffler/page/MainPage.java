package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainPage {
  private final ElementsCollection tableRows = $$("#spendings tr");
  private final SelenideElement historySpendingsHeader = $("#spendings h2");
  private final SelenideElement statisticsHeader = $("#spendings h2");

  public EditSpendingPage editSpending(String description) {
    tableRows.find(text(description)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public MainPage checkThatTableContains(String description) {
    tableRows.find(text(description)).should(visible);
    return this;
  }

  public MainPage shouldHistoryOfSpendingsVisible() {
    historySpendingsHeader.shouldBe(visible);
    assertEquals("History of Spendings", historySpendingsHeader.text());
    return this;
  }

  public MainPage shouldStatisticsHeaderVisible() {
    historySpendingsHeader.shouldBe(visible);
    assertEquals("Statistics", statisticsHeader.text());
    return this;
  }
}
