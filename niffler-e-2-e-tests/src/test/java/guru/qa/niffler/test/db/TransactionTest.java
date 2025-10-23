package guru.qa.niffler.test.db;

import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.TransactionIsolation;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.util.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        ),
        new AuthUserJson(
            null,
            RandomDataUtils.username(),
            RandomDataUtils.password(),
            true,
            true,
            true,
            true,
            List.of()
        ),
        TransactionIsolation.TRANSACTION_READ_UNCOMMITTED
    );
  }

  @Test
  void xaTransactionNegativeTest() {
    new UserDbClient().createUser(
        new UserJson(
            null,
            RandomDataUtils.username(),
            RandomDataUtils.firstName(),
            RandomDataUtils.surname(),
            RandomDataUtils.fullName(),
            RandomDataUtils.currencyValues(),
            "",
            ""
        ),
        new AuthUserJson(
            null,
            null,
            RandomDataUtils.password(),
            true,
            true,
            true,
            true,
            List.of()
        ),
        TransactionIsolation.TRANSACTION_READ_UNCOMMITTED
    );
  }
}
