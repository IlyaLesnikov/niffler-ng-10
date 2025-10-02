package guru.qa.niffler.util;

import com.github.javafaker.Faker;

public class RandomDataUtil {

  private RandomDataUtil() {}

  private static final Faker faker = new Faker();

  public static String username() {
    return faker.name().username();
  }

  public static String name() {
    return faker.name().name();
  }

  public static String password() {
    return faker.internet().password();
  }
}
