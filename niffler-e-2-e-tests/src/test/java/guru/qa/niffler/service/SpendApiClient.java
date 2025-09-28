package guru.qa.niffler.service;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static guru.qa.niffler.api.Endpoint.*;
import static java.net.HttpURLConnection.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient extends RestClient {

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.post(spend).execute();
    } catch (IOException exception) {
      throw new RuntimeException("Не удалось выполнить запрос на endpoint %s".formatted(internalSpendsAdd), exception);
    }
    assertEquals(HTTP_CREATED, response.code());
    return response.body();
  }

  public SpendJson editSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.patch(spend).execute();
    } catch (IOException exception) {
      throw new RuntimeException("Не удалось выполнить запрос на endpoint %s".formatted(internalSpendsEdit));
    }
    assertEquals(HTTP_OK, response.code());
    return response.body();
  }

  public SpendJson getSpend(int id) {
    final Response<SpendJson> response;
    try {
      response = spendApi.get(id).execute();
    } catch (IOException exception) {
      throw new RuntimeException("Не удалось выполнить запрос на endpoint %s".formatted(internalSpendsId));
    }
    assertEquals(HTTP_OK, response.code());
    return response.body();
  }

  public List<CurrencyJson> getSpends() {
    final Response<List<CurrencyJson>> response;
    try {
      response = spendApi.getSpends().execute();
    } catch (IOException exception) {
      throw new RuntimeException("Не удалось выполнить запрос на endpoint %s".formatted(internalSpendsAll));
    }
    assertEquals(HTTP_CREATED, response.code());
    return response.body();
  }

  public void deleteSpend(int id) {
    try {
      final Response<Void> response = spendApi.delete(id).execute();
      assertEquals(HTTP_ACCEPTED, response.code());
    } catch (IOException exception) {
      throw new RuntimeException("Не удалось выполнить запрос на endpoint %s".formatted(internalSpendsRemove));
    }
  }

  public CategoryJson createCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.post(category).execute();
    } catch (IOException exception) {
      throw new RuntimeException("Не удалось выполнить запрос на endpoint %s".formatted(internalCategoriesAdd));
    }
    assertEquals(HTTP_CREATED, response.code());
    return response.body();
  }

  public CategoryJson updateCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.post(category).execute();
    } catch (IOException exception) {
      throw new RuntimeException("Не удалось выполнить запрос на endpoint %s".formatted(internalCategoriesUpdate));
    }
    assertEquals(HTTP_CREATED, response.code());
    return response.body();
  }

  public CategoryJson getCategories(Boolean excludeArchived) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.getCategories(excludeArchived).execute();
    } catch (IOException exception) {
      throw new RuntimeException("Не удалось выполнить запрос на endpoint %s".formatted(internalCategoriesAll));
    }
    assertEquals(HTTP_CREATED, response.code());
    return response.body();
  }
}
