package monologue;

import java.lang.reflect.AccessibleObject;
import monologue.Annotations.Log;

class EvalAnno {

  static enum LogType {
    File,
    Nt,
    None;
  }

  /**
   * Simplifies the user specified annotations on a field/method to a ternary conditiion expressed
   * as {@link LogType}.
   *
   * @param element The field/method to simplify
   * @return The simplified condition
   */
  static LogType annoEval(AccessibleObject element) {
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

  /**
   * Checks if their are multiple logging annotations on one field/method.
   *
   * @param element The field/method to check
   * @return If there are too many logging annotations
   */
  static boolean overloadedAnno(AccessibleObject element) {
    int count = 0;
    for (var anno : Annotations.ALL_ANNOTATIONS) {
      if (element.isAnnotationPresent(anno)) {
        count++;
      }
    }
    return count > 1;
  }

  /** A condensed packaged of what describes a singular logged field/method */
  static class LogMetadata {
    public final LogLevel level;
    public final boolean once;
    public final String relativePath;

    private LogMetadata(LogLevel level, boolean once, String path) {
      this.level = level;
      this.once = once;
      this.relativePath = path;
    }

    /**
     * Derives the metadata from an annotated field/method, if there are no logging annotations this
     * returns null.
     *
     * @param element The field/method to derive the metadata from
     * @return The metadata
     */
    static LogMetadata from(AccessibleObject element) {
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
