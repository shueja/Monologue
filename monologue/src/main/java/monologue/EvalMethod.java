package monologue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.DriverStation;
import monologue.EvalAnno.LogMetadata;
import monologue.EvalAnno.LogType;

class EvalMethod {

  private static Supplier<?> getSupplier(Method method, Logged loggable) {
    return () -> {
      try {
        return method.invoke(loggable);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        DriverStation.reportWarning(method.getName() + " supllier is erroring", false);
        return null;
      }
    };
  }

  public static void evalMethod(Method method, Logged loggable, String rootPath) {
    evalMethodAnnotations(method, loggable, rootPath);
  }

  private static void evalMethodAnnotations(Method method, Logged loggable, String rootPath) {

    LogType logType = EvalAnno.annoEval(method);

    if (logType == LogType.None) { return; }

    method.setAccessible(true);
    if (method.getParameterCount() > 0) {
      DriverStation.reportWarning("Cannot have parameters on a logged method", false);
      return;
    }

    LogMetadata logMetadata = EvalAnno.LogMetadata.from(method);

    String name = logMetadata.relativePath.isEmpty() ? method.getName() : logMetadata.relativePath;
    String path = rootPath + "/" + name;
    DataType type;

    try{
      type = DataType.fromClass(method.getReturnType());
    } catch (IllegalArgumentException e) {
      MonologueLog.RuntimeWarn("Tried to log invalid type " + name + " -> " + method.getReturnType() + " in " + rootPath);
      return;
    }

    if (type == DataType.NTSendable || type == DataType.Sendable) {
      MonologueLog.RuntimeWarn("Tried to log invalid type " + name + " -> " + method.getReturnType() + " in " + rootPath
        + "Sendable isn't supported yet for methods");
      return;
    }

    if (logType == LogType.File) {
      Monologue.dataLogger.helper(
          getSupplier(method, loggable),
          type,
          path,
          logMetadata.once,
          logMetadata.level
        );
    } else if (logType == LogType.Nt) {
      Monologue.ntLogger.helper(
          getSupplier(method, loggable),
          type,
          path,
          logMetadata.once,
          logMetadata.level
        );
    }
  }
}
