package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendApiClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        Spending.class
    ).ifPresent(annotationSpending -> {
      final ExtensionContext.Store store = context.getStore(NAMESPACE);
      SpendJson spendingJson = spendingFromAnnotation(annotationSpending);
      SpendJson spending = spendApiClient.createSpend(spendingJson);
      store.put(context.getUniqueId(), spending);
    });
  }

  private SpendJson spendingFromAnnotation(Spending annotation) {
    return new SpendJson(
        null,
        new Date(),
        new CategoryJson(
          null,
            "ilesnikov1",
            annotation.username(),
            false
        ),
        annotation.currency(),
        annotation.amount(),
        annotation.description(),
        annotation.username()
    );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class) &&
        extensionContext.getRequiredTestMethod().isAnnotationPresent(Spending.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    final ExtensionContext.Store store = extensionContext.getStore(NAMESPACE);
    return store.get(extensionContext.getUniqueId(), SpendJson.class);
  }
}
