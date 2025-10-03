package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class FriendsPage {
  private final SelenideElement friendList = $("#simple-tabpanel-friends");
  private final SelenideElement acceptButton = $(xpath("//button[text()='Accept']"));
  private final SelenideElement declineButton = $(xpath("//button[text()='Decline']"));
  private final SelenideElement friendsRequestHeader = $(xpath("//h2[text()='Friend requests']"));
  private final SelenideElement myFriendsHeader = $(xpath("//h2[text()='My friends']"));
  private final SelenideElement friendRequests = $("#requests");
  private final SelenideElement friends = $("#friends");
  private final SelenideElement allPeopleTab = $(xpath("//h2[text()='All people']"));

  public FriendsPage shouldBeVisibleEmptyFriends() {
    friendList.shouldHave(text("There are no users yet"));
    return this;
  }

  public FriendsPage shouldBeVisibleAcceptButton() {
    acceptButton.shouldBe(visible, enabled);
    return this;
  }

  public FriendsPage shouldBeVisibleDeclineButton() {
    declineButton.shouldBe(visible, enabled);
    return this;
  }

  public FriendsPage shouldBeVisibleFriendsRequestHeader() {
    friendsRequestHeader.shouldBe(visible);
    return this;
  }

  public FriendsPage shouldBeVisibleIncomeRequest(String friendName) {
    friendRequests.shouldHave(text(friendName));
    return this;
  }

  public FriendsPage shouldBeVisibleMyFriendsHeader() {
    myFriendsHeader.shouldBe(visible);
    return this;
  }

  public FriendsPage shouldHaveFriendName(String friendName) {
    friends.shouldHave(text(friendName));
    return this;
  }

  public AllPeoplePage allPeopleTabSubmit() {
    allPeopleTab.click();
    return new AllPeoplePage();
  }
}
