package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.util.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(annotationUser -> {
          Spending[] spendings = annotationUser.spendings();
          Spending annotationSpending = null;
          if (spendings.length != 0) {
            annotationSpending = spendings[0];
          }
          if (annotationSpending != null) {
            ExtensionContext.Store store = context.getStore(NAMESPACE);
            SpendJson spend = new SpendJson(
                null,
                new Date(),
                new CategoryJson(
                    null,
                    RandomDataUtils.categoryName(),
                    annotationUser.username(),
                    false
                ),
                annotationSpending.currency(),
                annotationSpending.amount(),
                annotationSpending.description(),
                annotationUser.username()
            );
            SpendJson createdSpend = spendDbClient.createSpend(spend);
            store.put(context.getUniqueId(), createdSpend);
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return isSupportsParameter(parameterContext, extensionContext);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    final ExtensionContext.Store store = extensionContext.getStore(NAMESPACE);
    return store.get(extensionContext.getUniqueId(), SpendJson.class);
  }

  private Boolean isSupportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class) &&
        extensionContext.getRequiredTestMethod().isAnnotationPresent(User.class);
  }
}
