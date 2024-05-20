package monologue;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DriverStation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import monologue.Annotations.IgnoreLogged;
import monologue.EvalAnno.LogMetadata;
import monologue.EvalAnno.LogType;

class EvalField {
  private static Supplier<?> getSupplier(Field field, Logged loggable) {
    field.setAccessible(true);
    return () -> {
      try {
        return field.get(loggable);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        DriverStation.reportWarning(
            field.getName() + " supllier is erroring: " + e.toString(), false);
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

    if (evalNestedLogged(field, loggable, rootPath)) {
      MonologueLog.runtimeLog(rootPath + "." + field.getName() + " was logged recursively");
    } else {
      evalFieldAnnotations(field, loggable, rootPath);
    }
  }

  private static boolean evalNestedLogged(Field field, Logged loggable, String rootPath) {
    final Optional<Object> fieldOptional = getField(field, loggable);

    if (fieldOptional.isEmpty() || field.isAnnotationPresent(IgnoreLogged.class)) {
      MonologueLog.runtimeLog(rootPath + "." + field.getName() + " is null or ignored");
      return false;
    }

    boolean recursed = false;

    // if the field is of type Logged
    if (Logged.class.isAssignableFrom(field.getType())) {
      if (!Modifier.isFinal(field.getModifiers()) && !Monologue.shouldAllowNonFinalLoggedFields()) {
        MonologueLog.runtimeWarn(
            rootPath + "." + field.getName() + " is reccomended to be final for logging");
      }
      Logged logged = (Logged) fieldOptional.get();
      String pathOverride = logged.getOverrideName();
      if (pathOverride.equals("")) {
        pathOverride = field.getName();
      }
      Monologue.logObj(logged, rootPath + "/" + pathOverride);
      recursed = true;
    } else if (field.getType().isArray()) {
      // If object array
      if (Object.class.isAssignableFrom(fieldOptional.get().getClass().getComponentType())) {
        if (!Modifier.isFinal(field.getModifiers())
            && !Monologue.shouldAllowNonFinalLoggedFields()) {
          MonologueLog.runtimeWarn(
              rootPath + "." + field.getName() + " is reccomended to be final for logging");
        }
        int idx = 0;
        // Include all elements whose runtime class is Loggable
        for (Object obj : (Object[]) fieldOptional.get()) {
          if (obj instanceof Logged) {
            String pathOverride = ((Logged) obj).getOverrideName();
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
          String pathOverride = ((Logged) obj).getOverrideName();
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

  private static void evalFieldAnnotations(Field field, Logged loggable, String rootPath) {
    LogType logType = EvalAnno.annoEval(field);

    if (logType == LogType.None) {
      return;
    }

    if (Modifier.isStatic(field.getModifiers())) {
      MonologueLog.runtimeWarn(rootPath + "." + field.getName() + " is static and will be ignored");
      return;
    }

    if (EvalAnno.overloadedAnno(field)) {
      MonologueLog.runtimeWarn(
          rootPath + "." + field.getName() + " has more than 1 logging annotation");
      return;
    }

    LogMetadata logMetadata = EvalAnno.LogMetadata.from(field);

    if (logMetadata.once && getField(field, loggable).isEmpty()) {
      MonologueLog.runtimeWarn(
          rootPath + "." + field.getName() + " is logged once and is null at setup");
    }

    String name = logMetadata.relativePath.equals("") ? field.getName() : logMetadata.relativePath;
    String key = rootPath + "/" + name;
    Class<?> type = field.getType();

    if (!TypeChecker.isValidType(type)) {
      MonologueLog.runtimeWarn(
          rootPath + "." + field.getName() + " is not a valid type for logging");
      return;
    }

    if (Monologue.isMonologueDisabled())
      // Most type validation and user code has happened,
      // everything after this is actually logging so if disabled, return true
      return;

    if (logType == LogType.File) {
      if (NTSendable.class.isAssignableFrom(type)) {
        MonologueLog.runtimeWarn(
            "NTSendable isn't supported for @Log.File, use @Log or @Log.NT instead: " + rootPath + "." + field.getName());

        Monologue.ntLogger.addSendable(key, (NTSendable) getField(field, loggable).get());
      } else if (Sendable.class.isAssignableFrom(type)) {
        Monologue.dataLogger.addSendable(key, (Sendable) getField(field, loggable).get());
      } else {
        Monologue.dataLogger.addSupplier(
            key, type, getSupplier(field, loggable), logMetadata.level, logMetadata.once);
      }
    } else if (logType == LogType.Nt) {
      if (Sendable.class.isAssignableFrom(type) || NTSendable.class.isAssignableFrom(type)) {
        Monologue.ntLogger.addSendable(key, (Sendable) getField(field, loggable).get());
      } else {
        Monologue.ntLogger.addSupplier(
            key, type, getSupplier(field, loggable), logMetadata.level, logMetadata.once);
        if (logMetadata.level == LogLevel.DEFAULT && !logMetadata.once) {
          // The data *could* need to only go to datalog if its default log level,
          // register a supplier for dataLogger that can pickup the logging when the nt
          // one is deactivated by changing the FILE_ONLY flag
          Monologue.dataLogger.addSupplier(
              key, type, getSupplier(field, loggable), logMetadata.level, logMetadata.once);
        }
      }
    }
  }
}
