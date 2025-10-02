package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {
  private final ElementsCollection people = $$("tbody tr");

  public AllPeoplePage shouldBeVisibleWaiting(String peopleName) {
    people.find(text(peopleName)).shouldHave(text("Waiting..."));
    return this;
  }
}
