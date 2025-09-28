package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendApiClient;
import guru.qa.niffler.util.RandomDataUtil;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
  private final SpendApiClient spendApi = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
        .ifPresent(annotationCategory -> {
          CategoryJson category = categoryFromAnnotation(annotationCategory);
          CategoryJson createdCategory = spendApi.createCategory(category);
          if (annotationCategory.archived()) {
            final CategoryJson archivedCategory = new CategoryJson(
                createdCategory.id(),
                createdCategory.name(),
                createdCategory.username(),
                true
            );
            createdCategory = spendApi.updateCategory(archivedCategory);
          }
          context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class) &&
        extensionContext.getRequiredTestMethod().isAnnotationPresent(Category.class);
  }

  @Override
  public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    ExtensionContext.Store store = extensionContext.getStore(NAMESPACE);
    return store.get(extensionContext.getUniqueId(), CategoryJson.class);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
        .ifPresent(annotationCategory -> {
          if (!annotationCategory.archived()) {
            ExtensionContext.Store store = context.getStore(NAMESPACE);
            CategoryJson category = store.get(context.getUniqueId(), CategoryJson.class);
            CategoryJson archivedCategory = new CategoryJson(
                category.id(),
                category.name(),
                category.username(),
                true
            );
            spendApi.updateCategory(archivedCategory);
          }
        });
  }

  @Nonnull
  private CategoryJson categoryFromAnnotation(@Nonnull Category annotation) {
    return new CategoryJson(
        null,
        RandomDataUtil.username(),
        annotation.name(),
        false
    );
  }
}
