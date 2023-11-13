package monologue.evaluation;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DriverStation;
import monologue.DataType;
import monologue.LogLevel;
import monologue.LogPath;
import monologue.Logged;
import monologue.Monologue;
import monologue.ShuffleboardApi;
import monologue.Monologue.MonoShuffleboard;
import monologue.Monologue.MonoShuffleboardLayout;
import monologue.Monologue.MonoShuffleboardTab;
import monologue.ShuffleboardApi.MetadataFields;
import monologue.ShuffleboardApi.ShuffleEntry;
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

    evalFieldShuffleboard(field, loggable, rootPath);

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

    if (logMetadata.level == LogLevel.DEBUG && !Monologue.DEBUG) {
      return false;
    } else if (logMetadata.level == LogLevel.FILE_IN_COMP && !Monologue.DEBUG) {
      logType = LogType.File;
    }

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
            logMetadata.once
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
            logMetadata.once);
      }
    }
    return true;
  }

  private static Boolean evalFieldShuffleboard(Field field, Logged loggable, String rootPath) {

    if (!field.isAnnotationPresent(MonoShuffleboard.class)) {
      return false;
    }

    boolean isLayout = loggable.getClass().isAnnotationPresent(MonoShuffleboardLayout.class);
    boolean isTab = loggable.getClass().isAnnotationPresent(MonoShuffleboardTab.class);

    if (!isLayout && !isTab) {
      return false;
    }

    String name = field.getName();
    var lpath = LogPath.from(rootPath);

    DataType type;
    try {
      type = DataType.fromClass(field.getType());
    } catch (IllegalArgumentException e) {
      DriverStation.reportWarning("Tried to log invalid type " + name + "(" + field.getType() + ") in " + rootPath,
          false);
      return false;
    }
    
    ShuffleboardApi.ShuffleEntryContainer container;

    if (isLayout && lpath.len() > 1) {
      var layout = ShuffleboardApi.getTab(lpath.get(lpath.len()-2)).getLayout(lpath.get(lpath.len()-1));
      MonoShuffleboardLayout layoutAnno = loggable.getClass().getAnnotation(MonoShuffleboardLayout.class);
      if (layoutAnno.size().length == 2) {
        layout.withSize(layoutAnno.size()[0], layoutAnno.size()[1]);
      }
      if (layoutAnno.pos().length == 2) {
        layout.withPosition(layoutAnno.pos()[0], layoutAnno.pos()[1]);
      }
      container = layout;
    } else {
      container = ShuffleboardApi.getTab(lpath.get(lpath.len()-1));
    }

    //accepts all primitive types and strings
    if (type == DataType.Boolean || type == DataType.Double || type == DataType.Integer || type == DataType.String
        || type == DataType.BooleanArray || type == DataType.DoubleArray || type == DataType.IntegerArray || type == DataType.StringArray) {
      ShuffleEntry entry = container.addEntry(name, getSupplier(field, loggable));
      MonoShuffleboard shuffleAnno = field.getAnnotation(MonoShuffleboard.class);
      if (shuffleAnno.widget().length() > 0) {
        entry.applyMetadata(Map.of(MetadataFields.Widget, shuffleAnno.widget()));
      }
      if (shuffleAnno.size().length == 2) {
        entry.withSize(shuffleAnno.size()[0], shuffleAnno.size()[1]);
      }
      if (shuffleAnno.pos().length == 2) {
        entry.withPosition(shuffleAnno.pos()[0], shuffleAnno.pos()[1]);
      }
      return true;
    } else {
      DriverStation.reportWarning("Tried to log invalid type " + name + "(" + field.getType() + ") in " + rootPath,
          false);
      return false;
    }
  }
}
