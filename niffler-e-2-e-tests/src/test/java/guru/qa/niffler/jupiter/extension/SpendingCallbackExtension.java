package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendApiClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingCallbackExtension implements BeforeEachCallback {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingCallbackExtension.class);
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
}
