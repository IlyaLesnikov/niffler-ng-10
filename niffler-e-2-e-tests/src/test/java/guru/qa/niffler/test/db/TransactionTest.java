package guru.qa.niffler.test.db;

import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.TransactionIsolation;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.util.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

public class XaTransactionTest {

  @Test
  void xaTransactionTest() {
    new UserDbClient().createUser(
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
}
