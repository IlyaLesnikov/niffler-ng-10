package guru.qa.niffler.model;

import java.sql.Connection;
import java.util.function.Function;

public record XaFunction<T>(Function<Connection, T> function, String jdbcUrl) {
}
