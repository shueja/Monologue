package monologue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DataLogManager;

public class Monologue {

  /**
   * The Monologue library wide debug flag,
   * is used to filter logging behavior
   */
  private static Boolean DEBUG = true;

  private static Boolean HAS_SETUP_BEEN_CALLED = false;

  static final NTLogger ntLogger = new NTLogger();
  static final DataLogger dataLogger = new DataLogger();
  static final WeakHashMap<Logged, String> loggedRegistry = new WeakHashMap<Logged, String>();


  public static class MonologueConfig {
    public Boolean debug = true;
    public Boolean lazyNT = false;
    public Boolean lazyFile = false;
    public String rootPath = "Robot";

    public MonologueConfig() {}

    public static MonologueConfig defaultConfig() {
      return new MonologueConfig();
    }

    public MonologueConfig withDebug(Boolean debug) {
      this.debug = debug;
      return this;
    }

    public MonologueConfig withLazyNT(Boolean lazyNT) {
      this.lazyNT = lazyNT;
      return this;
    }

    public MonologueConfig withLazyFile(Boolean lazyFile) {
      this.lazyFile = lazyFile;
      return this;
    }

    public MonologueConfig withRootPath(String rootPath) {
      this.rootPath = rootPath;
      return this;
    }
  }

  /**
   * Is the main entry point for the monologue library.
   * It will interate over every member of the provided Logged object and
   * evaluated if it should be logged to the network tables or to a file.
   * 
   * Will also recursively check field values for classes that implement Logged
   * and log those as well.
   * 
   * @param loggable the root Logged object to log
   * @param config   the configuration for the monologue library
   * 
   * @apiNote Should only be called once, if another {@link Logged} tree needs to be created
   *        use {@link #logObj(Logged, String)} for additional trees
   */
  public static void setupMonologue(Logged loggable, MonologueConfig config) {
    if (HAS_SETUP_BEEN_CALLED) {
      throw new IllegalStateException("Monologue.setupMonologue() has already been called");
    }
    HAS_SETUP_BEEN_CALLED = true;

    MonologueLog.RuntimeLog("Monologue.setupMonologue() called on " + loggable.getClass().getName());
    DEBUG = config.debug;
    ntLogger.setLazy(config.lazyNT);
    dataLogger.setLazy(config.lazyFile);
    NetworkTableInstance.getDefault().startEntryDataLog(DataLogManager.getLog(), "", "");
    logObj(loggable, config.rootPath);
  }

  /**
   * Is the main entry point for the monologue library.
   * It will interate over every member of the provided Logged object and
   * evaluated if it should be logged to the network tables or to a file.
   * 
   * Will also recursively check field values for classes that implement Logged
   * and log those as well.
   * 
   * @param loggable the root Logged object to log
   * @param rootpath the root path to log to
   * @param debug    the debug flag for the monologue library
   * 
   * @apiNote Should only be called once, if another {@link Logged} tree needs to be created
   *        use {@link #logObj(Logged, String)} for additional trees
   */
  public static void setupMonologue(Logged loggable, String rootpath, Boolean debug) {
    setupMonologue(loggable, MonologueConfig.defaultConfig().withRootPath(rootpath).withDebug(debug));
  }

  /**
   * Will interate over every element of the provided {@link Logged} object and
   * handle the data transmission from there.
   * 
   * Will also recursively check field values for classes that implement {@link Logged}
   * and log those as well.
   * 
   * @param loggable the obj to scrape
   * @param path     the path to log to
   */
  public static void logObj(Logged loggable, String path) {
    var lpath = LogPath.from(path);
    if (!lpath.isValid()) {
      throw new IllegalArgumentException("Invalid path: " + path);
    } else if (lpath.isRoot()) {
      throw new IllegalArgumentException("Root path of / is not allowed");
    }
    MonologueLog.RuntimeLog("Monologue.logObj() called on " + loggable.getClass().getName() + " with path " + path);

    loggedRegistry.put(loggable, path);

    for (Field field : getAllFields(loggable.getClass())) {
      EvalField.evalField(field, loggable, path);
    }
    for (Method method : getAllMethods(loggable.getClass())) {
      EvalMethod.evalMethod(method, loggable, path);
    }
  }

  /**
   * Updates all the loggers, ideally called every cycle
   */
  public static void updateAll() {
    ntLogger.update(DEBUG);
    dataLogger.update(DEBUG);
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

  public static Boolean isDebug() {
    return DEBUG;
  }

  public static void setDebug(Boolean debug) {
    DEBUG = debug;
    MonologueLog.RuntimeLog("Monologue.setDebug() called with " + debug);
  }
}
