package guru.qa.niffler.test.db;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.util.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class TransactionTest {

  @Test
  void xaTransactionPositiveTest() {
    new UserDbClient().createUser(
        new UserJson(
            null,
            RandomDataUtils.username(),
            RandomDataUtils.firstName(),
            RandomDataUtils.surname(),
            RandomDataUtils.fullName(),
            RandomDataUtils.currencyValues(),
            "",
            "df"
        )
    );
  }

  @Test
  void xaTransactionNegativeTest() {
    new UserDbClient().createUser(
        new UserJson(
            null,
            null,
            RandomDataUtils.firstName(),
            RandomDataUtils.surname(),
            RandomDataUtils.fullName(),
            RandomDataUtils.currencyValues(),
            "",
            ""
        )
    );
  }
}
