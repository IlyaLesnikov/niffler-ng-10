package guru.qa.niffler.model;

public enum TransactionIsolation {

  NONE(0),
  READ_UNCOMMITTED(1),
  READ_COMMITTED(2),
  REPEATABLE_READ(4),
  SERIALIZABLE(8);

  private final int level;

  TransactionIsolation(int level) {
    this.level = level;
  }

  public int level() {
    return level;
  }
}
