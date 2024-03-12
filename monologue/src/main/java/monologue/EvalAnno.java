package monologue;

import java.lang.reflect.AccessibleObject;
import monologue.Annotations.Log;

class EvalAnno {

  public static enum LogType {
    File,
    Nt,
    None;
  }

  public static LogType annoEval(AccessibleObject element) {
    if (element.isAnnotationPresent(Log.NT.class)
        || element.isAnnotationPresent(Log.NT.Once.class)
        || element.isAnnotationPresent(Log.class)
        || element.isAnnotationPresent(Log.Once.class)) {
      return LogType.Nt;
    } else if (element.isAnnotationPresent(Log.File.class)
        || element.isAnnotationPresent(Log.File.Once.class)) {
      return LogType.File;
    } else {
      return LogType.None;
    }
  }

  public static class LogMetadata {
    public final LogLevel level;
    public final boolean once;
    public final String relativePath;

    public LogMetadata(LogLevel level, boolean once, String path) {
      this.level = level;
      this.once = once;
      this.relativePath = path;
    }

    public static LogMetadata from(AccessibleObject element) {
      if (element.isAnnotationPresent(Log.File.class)) {
        Log.File anno = element.getAnnotation(Log.File.class);
        return new LogMetadata(anno.level(), false, anno.key());
      } else if (element.isAnnotationPresent(Log.NT.class)) {
        Log.NT anno = element.getAnnotation(Log.NT.class);
        return new LogMetadata(anno.level(), false, anno.key());
      } else if (element.isAnnotationPresent(Log.class)) {
        Log anno = element.getAnnotation(Log.class);
        return new LogMetadata(anno.level(), false, anno.key());
      } else if (element.isAnnotationPresent(Log.File.Once.class)) {
        Log.File.Once anno = element.getAnnotation(Log.File.Once.class);
        return new LogMetadata(LogLevel.DEFAULT, true, anno.key());
      } else if (element.isAnnotationPresent(Log.NT.Once.class)) {
        Log.NT.Once anno = element.getAnnotation(Log.NT.Once.class);
        return new LogMetadata(LogLevel.DEFAULT, true, anno.key());
      } else if (element.isAnnotationPresent(Log.Once.class)) {
        Log.Once anno = element.getAnnotation(Log.Once.class);
        return new LogMetadata(LogLevel.DEFAULT, true, anno.key());
      } else {
        return null;
      }
    }
  }
}
