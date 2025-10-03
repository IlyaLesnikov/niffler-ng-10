package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendApiClient;
import guru.qa.niffler.util.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
  private final SpendApiClient spendApi = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(annotationUser -> {
          Category[] annotationCategories = annotationUser.categories();
          Category annotationCategory = null;
          if (annotationCategories.length != 0) {
            annotationCategory = annotationCategories[0];
          }
          if (annotationCategory != null) {
            ExtensionContext.Store store = context.getStore(NAMESPACE);
            CategoryJson category = new CategoryJson(
                null,
                RandomDataUtils.categoryName(),
                annotationUser.username(),
                false
            );
            CategoryJson categoryCreated = spendApi.createCategory(category);
            if (annotationCategory.archived()) {
              categoryCreated = spendApi.updateCategory(
                  new CategoryJson(
                      categoryCreated.id(),
                      categoryCreated.name(),
                      categoryCreated.username(),
                      true
                  )
              );
            }
            store.put(context.getUniqueId(), categoryCreated);
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class) &&
        extensionContext.getRequiredTestMethod().isAnnotationPresent(User.class);
  }

  @Override
  public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    ExtensionContext.Store store = extensionContext.getStore(NAMESPACE);
    return store.get(extensionContext.getUniqueId(), CategoryJson.class);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(annotationUser -> {
          ExtensionContext.Store store = context.getStore(NAMESPACE);
          CategoryJson category =  store.get(context.getUniqueId(), CategoryJson.class);
          if (category != null) {
            spendApi.updateCategory(
                new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
                )
            );
          }
        });
  }
}
