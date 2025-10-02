package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

public class FriendsTest {

  private final Config CFG = Config.getInstance();

  @Test
  @ExtendWith(UsersQueueExtension.class)
  void friendsTableShouldBeEmptyForNewUser(@UserType(type = UserType.Type.EMPTY) UsersQueueExtension.StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(staticUser.username(), staticUser.password())
        .submitIcon()
        .submitIconOptionFriends()
        .shouldBeVisibleEmptyFriends();
  }

  @Test
  @ExtendWith(UsersQueueExtension.class)
  void friendsShouldBePresentInFriendsTable(@UserType(type = UserType.Type.WITH_FRIEND) UsersQueueExtension.StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(staticUser.username(), staticUser.password())
        .submitIcon()
        .submitIconOptionFriends()
        .shouldBeVisibleMyFriendsHeader()
        .shouldHaveFriendName(staticUser.friend());
  }

  @Test
  @ExtendWith(UsersQueueExtension.class)
  void incomeInvitationBePresentInFriendsTable(@UserType(type = UserType.Type.WITH_INCOME_REQUEST) UsersQueueExtension.StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(staticUser.username(), staticUser.password())
        .submitIcon()
        .submitIconOptionFriends()
        .shouldBeVisibleFriendsRequestHeader()
        .shouldBeVisibleAcceptButton()
        .shouldBeVisibleDeclineButton()
        .shouldBeVisibleIncomeRequest(staticUser.income());
  }

  @Test
  @ExtendWith(UsersQueueExtension.class)
  void outcomeInvitationBePresentInAllPeoplesTable(@UserType(type = UserType.Type.WITH_OUTCOME_REQUEST) UsersQueueExtension.StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(staticUser.username(), staticUser.password())
        .submitIcon()
        .submitIconOptionFriends()
        .allPeopleTabSubmit()
        .shouldBeVisibleWaiting(staticUser.outcome());
  }
}
