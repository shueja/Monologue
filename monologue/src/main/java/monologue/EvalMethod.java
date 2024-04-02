package monologue;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.util.sendable.Sendable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import monologue.EvalAnno.LogMetadata;
import monologue.EvalAnno.LogType;

class EvalMethod {

  private static Supplier<?> getSupplier(Method method, Logged loggable) {
    return () -> {
      try {
        return method.invoke(loggable);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        MonologueLog.runtimeWarn(
            Logged.getFullPath(loggable)
                + "."
                + method.getName()
                + " supllier is erroring: "
                + e.toString());
        return null;
      }
    };
  }

  public static void evalMethod(Method method, Logged loggable, String rootPath) {
    method.setAccessible(true);

    evalMethodAnnotations(method, loggable, rootPath);
  }

  private static void evalMethodAnnotations(Method method, Logged loggable, String rootPath) {

    LogType logType = EvalAnno.annoEval(method);

    if (logType == LogType.None) {
      return;
    }

    if (Modifier.isStatic(method.getModifiers())) {
      MonologueLog.runtimeWarn(
          "Tried to log static method "
              + method.getName()
              + " in "
              + rootPath
              + ": Static methods are not supported");
      return;
    }

    LogMetadata logMetadata = EvalAnno.LogMetadata.from(method);

    String name = logMetadata.relativePath.isEmpty() ? method.getName() : logMetadata.relativePath;
    String path = rootPath + "/" + name;
    Class<?> type;

    type = method.getReturnType();

    if (method.getParameterCount() > 0) {
      MonologueLog.runtimeWarn("Cannot have parameters on a logged method (" + path + ")");
      return;
    }

    if (type.isAssignableFrom(NTSendable.class) || type.isAssignableFrom(Sendable.class)) {
      MonologueLog.runtimeWarn(
          "Tried to log invalid type "
              + name
              + " -> "
              + method.getReturnType()
              + " in "
              + rootPath
              + ": Sendable isn't supported yet for methods");
      return;
    }

    if (!TypeChecker.isValidType(type)) {
      MonologueLog.runtimeWarn(
          rootPath + "." + method.getName() + " is not a valid type for logging");
      return;
    }

    if (Monologue.isMonologueDisabled())
      // Most type validation and user code has happened,
      // everything after this is actually logging so if disabled, return true
      return;

    if (logType == LogType.File) {
      Monologue.dataLogger.addSupplier(
          path,
          method.getReturnType(),
          getSupplier(method, loggable),
          logMetadata.level,
          logMetadata.once);
    } else if (logType == LogType.Nt) {
      Monologue.ntLogger.addSupplier(
          path,
          method.getReturnType(),
          getSupplier(method, loggable),
          logMetadata.level,
          logMetadata.once);
      if (logMetadata.level == LogLevel.DEFAULT && !logMetadata.once) {
        // The data *could* need to only go to datalog if its default log level,
        // register a supplier for dataLogger that can pickup the logging when the nt
        // one is deactivated by changing the FILE_ONLY flag
        Monologue.dataLogger.addSupplier(
            path,
            method.getReturnType(),
            getSupplier(method, loggable),
            logMetadata.level,
            logMetadata.once);
      }
    }
  }
}
