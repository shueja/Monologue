package monologue.evaluation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

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
    evalMethodShuffleboard(method, loggable, rootPath);
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

    String name = method.getName();
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

  private static Boolean evalMethodShuffleboard(Method method, Logged loggable, String rootPath) {
    method.setAccessible(true);
    if (method.getParameterCount() > 0) {
      DriverStation.reportWarning("Cannot have parameters on a logged method", false);
      return false;
    }

    if (!method.isAnnotationPresent(MonoShuffleboard.class)) {
      return false;
    }

    boolean isLayout = loggable.getClass().isAnnotationPresent(MonoShuffleboardLayout.class);
    boolean isTab = loggable.getClass().isAnnotationPresent(MonoShuffleboardTab.class);

    if (!isLayout && !isTab) {
      return false;
    }

    String name = method.getName();
    var lpath = LogPath.from(rootPath);

    DataType type;
    try {
      type = DataType.fromClass(method.getReturnType());
    } catch (IllegalArgumentException e) {
      DriverStation.reportWarning("Tried to log invalid type " + name + " -> " + method.getReturnType() + " in " + rootPath,
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
      ShuffleEntry entry = container.addEntry(name, getSupplier(method, loggable));
      MonoShuffleboard shuffleAnno = method.getAnnotation(MonoShuffleboard.class);
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
      DriverStation.reportWarning("Tried to log invalid type " + name + " -> " + method.getReturnType() + " in " + rootPath,
          false);
      return false;
    }
  }
}
