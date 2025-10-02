package guru.qa.niffler.api;

public interface Endpoint {
  String internalSpendsAdd = "internal/spends/add";
  String internalSpendsEdit = "internal/spends/edit";
  String internalSpendsId = "internal/spends/{id}";
  String internalSpendsAll = "internal/spends/all";
  String internalSpendsRemove = "internal/spends/remove";
  String internalCategoriesAdd = "internal/categories/add";
  String internalCategoriesUpdate = "internal/categories/update";
  String internalCategoriesAll = "internal/categories/all";
}
