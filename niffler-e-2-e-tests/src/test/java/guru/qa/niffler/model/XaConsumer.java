package guru.qa.niffler.model;

import java.sql.Connection;
import java.util.function.Consumer;

public record XaConsumer(Consumer<Connection> function, String jdbcUrl) {
}
