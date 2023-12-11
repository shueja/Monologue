package monologue;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DriverStation;
import monologue.EvalAnno.LogMetadata;
import monologue.EvalAnno.LogType;
import monologue.Annotations.IgnoreLogged;

class EvalField {
  private static boolean isNull(Field field, Object obj) {
    boolean isNull = true;
    try {
      isNull = field.get(obj) == null;
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return isNull;
  }

  private static Supplier<?> getSupplier(Field field, Logged loggable) {
    field.setAccessible(true);
    return () -> {
      try {
        return field.get(loggable);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        DriverStation.reportWarning(field.getName() + " supllier is erroring", false);
        e.printStackTrace();
        return null;
      }
    };
  }

  private static Optional<Object> getField(Field field, Object obj) {
    try {
      return Optional.ofNullable(field.get(obj));
    } catch (IllegalAccessException e) {
      return Optional.empty();
    }
  }

  public static void evalField(Field field, Logged loggable, String rootPath) {
    field.setAccessible(true);

    if (isNull(field, loggable)) {
      MonologueLog.RuntimeLog(rootPath + "." + field.getName() + " is null");
      return;
    }

    if (evalNestedLogged(field, loggable, rootPath)) {
      MonologueLog.RuntimeLog(rootPath + "." + field.getName() + " was logged recursively");
      return;
    }

    evalFieldAnnotations(field, loggable, rootPath);
  }

  private static Boolean evalNestedLogged(Field field, Logged loggable, String rootPath) {
    final Optional<Object> fieldOptional = getField(field, loggable);

    if (fieldOptional.isEmpty() || field.isAnnotationPresent(IgnoreLogged.class)) {
      return false;
    }

    Boolean recursed = false;

    // if the field is of type Logged
    if (Logged.class.isAssignableFrom(field.getType())) {
      Logged logged = (Logged) fieldOptional.get();
      String pathOverride = logged.getPath();
      if (pathOverride.equals("")) {
        pathOverride = field.getName();
      }
      Monologue.logObj(logged, rootPath + "/" + pathOverride);
      recursed = true;
    } else if (field.getType().isArray()) {
      // If primitive array
      if (Object.class.isAssignableFrom(fieldOptional.get().getClass().getComponentType())) {
        int idx = 0;
        // Include all elements whose runtime class is Loggable
        for (Object obj : (Object[]) fieldOptional.get()) {
          if (obj instanceof Logged) {
            String pathOverride = ((Logged) obj).getPath();
            if (pathOverride.equals("")) {
              pathOverride = obj.getClass().getSimpleName();
            }
            Monologue.logObj(
                (Logged) obj,
                rootPath + "/" + field.getName() + "/" + pathOverride + "[" + idx++ + "]");
            recursed = true;
          }
        }
      }
    } else if (Collection.class.isAssignableFrom(field.getType())) {
      int idx = 0;
      // Include all elements whose runtime class is Loggable
      for (Object obj : (Collection<?>) fieldOptional.get()) {
        if (obj instanceof Logged) {
          String pathOverride = ((Logged) obj).getPath();
          if (pathOverride.equals("")) {
            pathOverride = obj.getClass().getSimpleName();
          }
          Monologue.logObj(
              (Logged) obj,
              rootPath + "/" + field.getName() + "/" + pathOverride + "[" + idx++ + "]");
          recursed = true;
        }
      }
    }
    return recursed;
  }

  private static Boolean evalFieldAnnotations(Field field, Logged loggable, String rootPath) {
    LogType logType = EvalAnno.annoEval(field);

    if (logType == LogType.None) {
      return false;
    }

    LogMetadata logMetadata = EvalAnno.LogMetadata.from(field);

    String name = logMetadata.relativePath.equals("") ? field.getName() : logMetadata.relativePath;
    String key = rootPath + "/" + name;
    DataType type;

    try {
      type = DataType.fromClass(field.getType());
    } catch (IllegalArgumentException e) {
      MonologueLog.RuntimeWarn("Tried to log invalid type " + name + "(" + field.getType() + ") in " + rootPath);
      return false;
    }

    if (logType == LogType.File) {
      if (type == DataType.NTSendable) {
        Monologue.dataLogger.addSendable(key, (NTSendable) getSupplier(field, loggable).get());
      } else if (type == DataType.Sendable) {
        Monologue.dataLogger.addSendable(key, (Sendable) getSupplier(field, loggable).get());
      } else {
        Monologue.dataLogger.helper(
            getSupplier(field, loggable),
            type,
            key,
            logMetadata.once,
            logMetadata.level
        );
      }
    } else if (logType == LogType.Nt) {
      if (type == DataType.Sendable || type == DataType.NTSendable) {
        Monologue.ntLogger.addSendable(key, (Sendable) getSupplier(field, loggable).get());
      } else {
        Monologue.ntLogger.helper(
          getSupplier(field, loggable),
          type,
          key,
          logMetadata.once,
          logMetadata.level
        );
        // if (logMetadata.level == LogLevel.FILE_IN_COMP && !logMetadata.once) {
        //   Monologue.dataLogger.helper(
        //     getSupplier(field, loggable),
        //     type,
        //     key,
        //     logMetadata.once,
        //     logMetadata.level
        //   );
        // }
      }
    }
    return true;
  }
}
