package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WebTest
public class CategoryTest {

  private static final Config CFG = Config.getInstance();

  @Test
  @User(
      categories = {
          @Category()
      }
  )
  @DisplayName("Архивация категории")
  void categoryArchivingTest(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("ilesnikov1", "ilesnikov1")
        .submitIcon()
        .submitIconElement("Profile")
        .submitArchiveCategory(category.name())
        .submitButton("Archive")
        .shouldCategoryNonVisible(category.name())
        .submitShowArchive()
        .shouldCategoryVisible(category.name());
  }

  @Test
  @User(
      categories = {
          @Category(
              archived = true
          )
      }
  )
  @DisplayName("Разархивация категории")
  void unzippingCategoryTest(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("ilesnikov1", "ilesnikov1")
        .submitIcon()
        .submitIconElement("Profile")
        .shouldCategoryNonVisible(category.name())
        .submitShowArchive()
        .submitUnarchiveCategory(category.name())
        .submitButton("Unarchive")
        .submitShowArchive()
        .shouldCategoryVisible(category.name());
  }
}
