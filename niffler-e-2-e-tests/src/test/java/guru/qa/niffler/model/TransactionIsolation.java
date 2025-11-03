package guru.qa.niffler.model;

public enum TransactionIsolation {

  TRANSACTION_NONE(0),
  TRANSACTION_READ_UNCOMMITTED(1),
  TRANSACTION_READ_COMMITTED(2),
  TRANSACTION_REPEATABLE_READ(4),
  TRANSACTION_SERIALIZABLE(8);

  private final int level;

  TransactionIsolation(int level) {
    this.level = level;
  }

  public int level() {
    return level;
  }
}
