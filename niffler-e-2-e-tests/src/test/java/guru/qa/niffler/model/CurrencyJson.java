package guru.qa.niffler.model;

public record CurrencyJson(
    CurrencyValues currency,
    Double currencyRate
) {
}
