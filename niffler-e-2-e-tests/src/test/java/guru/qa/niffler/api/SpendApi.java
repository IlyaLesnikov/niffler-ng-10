package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

import static guru.qa.niffler.api.Endpoint.*;

public interface SpendApi {
  @POST(internalSpendsAdd)
  Call<SpendJson> post(@Body SpendJson body);
  @PATCH(internalSpendsEdit)
  Call<SpendJson> patch(@Body SpendJson body);
  @GET(internalSpendsId)
  Call<SpendJson> get(@Path("id") int id);
  @GET(internalSpendsAll)
  Call<List<CurrencyJson>> getSpends();
  @DELETE(internalSpendsRemove)
  Call<Void> delete(@Query("ids") int ids);
  @POST(internalCategoriesAdd)
  Call<CategoryJson> post(@Body CategoryJson category);
  @PATCH(internalCategoriesUpdate)
  Call<CategoryJson> patch(@Body CategoryJson category);
  @DELETE(internalCategoriesAll)
  Call<CategoryJson> getCategories(@Query("excludeArchived") Boolean excludeArchived);
}
