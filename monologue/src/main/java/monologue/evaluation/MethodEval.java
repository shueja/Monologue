package monologue.evaluation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.DriverStation;
import monologue.DataType;
import monologue.LogLevel;
import monologue.Logged;
import monologue.Monologue;
import monologue.evaluation.AnnoEval.LogMetadata;
import monologue.evaluation.AnnoEval.LogType;

public class MethodEval {

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

  public static void evalMethodAnnotations(Method method, Logged loggable, String rootPath) {

    LogType logType = AnnoEval.annoEval(method);

    if (logType == LogType.None) { return; }

    method.setAccessible(true);
    if (method.getParameterCount() > 0) {
      DriverStation.reportWarning("Cannot have parameters on a logged method", false);
      return;
    }

    LogMetadata logMetadata = AnnoEval.LogMetadata.from(method);

    if (logMetadata.level == LogLevel.DEBUG && !Monologue.DEBUG) {
      return;
    } else if (logMetadata.level == LogLevel.FILE_IN_COMP && !Monologue.DEBUG) {
      logType = LogType.File;
    }

    String name = logMetadata.relativePath.isEmpty() ? method.getName() : logMetadata.relativePath;
    String path = rootPath + "/" + name;
    DataType type;

    try{
      type = DataType.fromClass(method.getReturnType());
    } catch (IllegalArgumentException e) {
      DriverStation.reportWarning("Tried to log invalid type " + name + " -> " + method.getReturnType() + " in " + rootPath, false);
      return;
    }

    if (logType == LogType.File) {
      Monologue.dataLogger.helper(
          getSupplier(method, loggable),
          type,
          path,
          logMetadata.once
        );
    } else if (logType == LogType.Nt) {
      Monologue.ntLogger.helper(
          getSupplier(method, loggable),
          type,
          path,
          logMetadata.once
        );
    }
  }
}
