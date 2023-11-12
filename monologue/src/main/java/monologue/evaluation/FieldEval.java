package monologue.evaluation;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DriverStation;
import monologue.DataType;
import monologue.Logged;
import monologue.Monologue;
import monologue.evaluation.AnnoEval.LogMetadata;
import monologue.evaluation.AnnoEval.LogType;

public class FieldEval {
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
      return;
    }

    if (evalNestedLogged(field, loggable, rootPath)) {
      return;
    }

    evalFieldAnnotations(field, loggable, rootPath);
  }

  private static Boolean evalNestedLogged(Field field, Logged loggable, String rootPath) {
    final Optional<Object> fieldOptional = getField(field, loggable);

    if (fieldOptional.isEmpty()) {
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
              pathOverride = obj.getClass().getSimpleName() + "[" + idx++ + "]";
            }
            Monologue.logObj(
                (Logged) obj,
                rootPath + "/" + field.getName() + "/" + pathOverride);
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
            pathOverride = obj.getClass().getSimpleName() + "[" + idx++ + "]";
          }
          Monologue.logObj(
              (Logged) obj,
              rootPath + "/" + field.getName() + "/" + pathOverride);
          recursed = true;
        }
      }
    }
    return recursed;
  }

  private static Boolean evalFieldAnnotations(Field field, Logged loggable, String rootPath) {
    LogType logType = AnnoEval.annoEval(field);

    if (logType == LogType.None) {
      return false;
    }

    LogMetadata logMetadata = AnnoEval.LogMetadata.from(field);

    String name = field.getName();
    String key = rootPath + "/" + name;
    DataType type;

    try {
      type = DataType.fromClass(field.getType());
    } catch (IllegalArgumentException e) {
      DriverStation.reportWarning("Tried to log invalid type " + name + "(" + field.getType() + ") in " + rootPath,
          false);
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
            0 // TODO: Add level to LogFile
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
            0);
      }
    }
    return true;
  }
}
