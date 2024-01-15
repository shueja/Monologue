package monologue;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DataLogManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

public class Monologue {

  /** The Monologue library wide FILE_ONLY flag, is used to filter logging behavior */
  private static boolean FILE_ONLY = true;

  private static boolean HAS_SETUP_BEEN_CALLED = false;

  static final NTLogger ntLogger = new NTLogger();
  static final DataLogger dataLogger = new DataLogger();
  static final WeakHashMap<Logged, String> loggedRegistry = new WeakHashMap<Logged, String>();

  /**
   * Is the main entry point for the monologue library. It will interate over every member of the
   * provided Logged object and evaluated if it should be logged to the network tables or to a file.
   *
   * <p>Will also recursively check field values for classes that implement Logged and log those as
   * well.
   *
   * @param loggable the root Logged object to log
   * @param rootpath the root path to log to
   * @param fileOnly the FILE_ONLY flag for the monologue library
   * @param lazyLogging if true, will only log when the value changes
   * @apiNote Should only be called once, if another {@link Logged} tree needs to be created use
   *     {@link #logObj(Logged, String)} for additional trees
   */
  public static void setupMonologue(
      Logged loggable, String rootpath, boolean fileOnly, boolean lazyLogging) {
    if (HAS_SETUP_BEEN_CALLED) {
      throw new IllegalStateException("Monologue.setupMonologue() has already been called");
    }
    HAS_SETUP_BEEN_CALLED = true;
    if (!rootpath.startsWith("/")) {
      rootpath = "/" + rootpath;
    }
    MonologueLog.RuntimeLog(
        "Monologue.setupMonologue() called on "
            + loggable.getClass().getName()
            + " with rootpath "
            + rootpath
            + " and fileOnly "
            + fileOnly
            + " and lazyLogging "
            + lazyLogging);

    FILE_ONLY = fileOnly;
    ntLogger.setLazy(lazyLogging);
    dataLogger.setLazy(lazyLogging);
    NetworkTableInstance.getDefault().startEntryDataLog(DataLogManager.getLog(), "", "");
    logObj(loggable, rootpath);

    MonologueLog.RuntimeLog("Monologue.setupMonologue() finished");
  }

  /**
   * Will interate over every element of the provided {@link Logged} object and handle the data
   * transmission from there.
   *
   * <p>Will also recursively check field values for classes that implement {@link Logged} and log
   * those as well.
   *
   * @param loggable the obj to scrape
   * @param path the path to log to
   */
  public static void logObj(Logged loggable, String path) {
    var lpath = LogPath.from(path);
    if (!lpath.isValid()) {
      throw new IllegalArgumentException("Invalid path: " + path);
    } else if (lpath.isRoot()) {
      throw new IllegalArgumentException("Root path of / is not allowed");
    }
    MonologueLog.RuntimeLog(
        "Monologue.logObj() called on " + loggable.getClass().getName() + " with path " + path);

    loggedRegistry.put(loggable, path);

    for (Field field : getAllFields(loggable.getClass())) {
      EvalField.evalField(field, loggable, path);
    }
    for (Method method : getAllMethods(loggable.getClass())) {
      EvalMethod.evalMethod(method, loggable, path);
    }
  }

  /**
   * Updates all the loggers, ideally called every cycle.
   *
   * @apiNote Should only be called on the same thread monologue was setup on
   */
  public static void updateAll() {
    ntLogger.update(FILE_ONLY);
    dataLogger.update(FILE_ONLY);
  }

  private static List<Field> getAllFields(Class<?> type) {
    List<Field> result = new ArrayList<Field>();

    Class<?> i = type;
    while (i != null && i != Object.class) {
      Collections.addAll(result, i.getDeclaredFields());
      i = i.getSuperclass();
    }

    return result;
  }

  private static List<Method> getAllMethods(Class<?> type) {
    List<Method> result = new ArrayList<Method>();

    Class<?> i = type;
    while (i != null && i != Object.class) {
      Collections.addAll(result, i.getDeclaredMethods());
      i = i.getSuperclass();
    }

    return result;
  }

  public static boolean isFileOnly() {
    return FILE_ONLY;
  }

  public static void setFileOnly(boolean fileOnly) {
    FILE_ONLY = fileOnly;
    MonologueLog.RuntimeLog("Monologue.setFileOnly() called with " + fileOnly);
  }
}
