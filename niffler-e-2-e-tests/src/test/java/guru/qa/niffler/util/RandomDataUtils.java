package guru.qa.niffler.util;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.CurrencyValues;

import java.util.Random;

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
    return faker.commerce().promotionCode(wordsCount);
  }

  public static String firstName() {
    return faker.name().firstName();
  }

  public static String fullName() {
    return faker.name().fullName();
  }

  public static CurrencyValues currencyValues() {
    CurrencyValues[] currencyValues = CurrencyValues.values();
    int randomIndex = new Random().nextInt(0, currencyValues.length);
    return currencyValues[randomIndex];
  }

  public static String password() {
    return faker.internet().password();
  }
}
