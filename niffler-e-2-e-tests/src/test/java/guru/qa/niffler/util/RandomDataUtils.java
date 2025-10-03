package guru.qa.niffler.util;

import com.github.javafaker.Faker;

public class RandomDataUtils {

  private RandomDataUtils() {}

  private static final Faker faker = new Faker();

  public static String username() {
    return faker.name().username();
  }

  public static String categoryName() {
    return faker.name().name();
  }

  public static String surname() {
    return faker.name().nameWithMiddle();
  }

  public static String sentence(int wordsCount) {
    return faker.name().nameWithMiddle();
  }


  public static String password() {
    return faker.internet().password();
  }
}
