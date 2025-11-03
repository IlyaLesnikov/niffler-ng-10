package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.model.StaticUser;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);
  private static final Queue<StaticUser> EMPTY = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_FRIEND = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_INCOME_REQUEST = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_OUTCOME_REQUEST = new ConcurrentLinkedQueue<>();

  static {
    EMPTY.add(new StaticUser("ilesnikov1", "ilesnikov1", null, null, null));
    WITH_FRIEND.add(new StaticUser("ilesnikov3", "ilesnikov3", "ilesnikov2", null, null));
    WITH_INCOME_REQUEST.add(new StaticUser("ilesnikov2", "ilesnikov2", null, "ilesnikov1", null));
    WITH_OUTCOME_REQUEST.add(new StaticUser("ilesnikov4", "ilesnikov4", null, null, "ilesnikov3"));
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    ExtensionContext.Store store = context.getStore(NAMESPACE);
    Map<UserType, StaticUser> users = Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(parameter -> AnnotationSupport.isAnnotated(parameter, UserType.class))
        .map(parameter -> {
          UserType annotationUserType = parameter.getAnnotation(UserType.class);
          Optional<StaticUser> staticUser = Optional.empty();
          StopWatch stopWatch = StopWatch.createStarted();
          while (staticUser.isEmpty() && stopWatch.getTime(TimeUnit.SECONDS) < 30) {
            staticUser = Optional.ofNullable(
                getQueueByUserType(annotationUserType.type()).poll()
            );
          }
          Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setStart(new Date().getTime());
          });
          if (staticUser.isEmpty()) throw new IllegalStateException("Can`t find user after 30 seconds");
          return Map.of(annotationUserType, staticUser.get()).entrySet();
        })
        .flatMap(Collection::stream)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    store.put(context.getUniqueId(), users);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().isAnnotationPresent(UserType.class) &&
        parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    ExtensionContext.Store store = extensionContext.getStore(NAMESPACE);
    Map<UserType, StaticUser> users = store.get(extensionContext.getUniqueId(), Map.class);
    return users.get(parameterContext.getParameter().getAnnotation(UserType.class));
  }

  @Override
  @SuppressWarnings("unchecked")
  public void afterEach(ExtensionContext context) {
    ExtensionContext.Store store = context.getStore(NAMESPACE);
    Map<UserType, StaticUser> users = store.get(context.getUniqueId(), Map.class);
    for (Map.Entry<UserType, StaticUser> user: users.entrySet()) {
      getQueueByUserType(user.getKey().type()).add(user.getValue());
    }
  }

  private Queue<StaticUser> getQueueByUserType(UserType.Type type) {
    return switch (type) {
      case EMPTY -> EMPTY;
      case WITH_FRIEND -> WITH_FRIEND;
      case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST;
      case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST;
    };
  }
}
