package monologue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DataLogManager;
import monologue.evaluation.FieldEval;
import monologue.evaluation.MethodEval;

public class Monologue {

  /**
   * The Monologue library wide debug flag,
   * is used to filter logging behavior
   */
  private static Boolean DEBUG = true;

  public static final NTLogger ntLogger = new NTLogger();
  public static final DataLogger dataLogger = new DataLogger();
  public static final Map<Logged, String> loggedRegistry = new HashMap<Logged, String>();

  /**
   * Is the main entry point for the monologue library.
   * It will interate over every member of the provided Logged object and
   * evaluated if it should be logged to the network tables or to a file.
   * 
   * Will also recursively check field values for classes that implement Logged
   * and log those as well.
   * 
   * @param loggable the root Logged object to log
   * @param rootPath the nt/datalog path to log to
   */
  public static void setupMonologue(Logged loggable, String rootPath, Boolean debug) {
    DEBUG = debug;
    NetworkTableInstance.getDefault().startEntryDataLog(DataLogManager.getLog(), "", "");
    logObj(loggable, rootPath);
  }

  /**
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

    loggedRegistry.put(loggable, path);

    for (Field field : getAllFields(loggable.getClass())) {
      FieldEval.evalField(field, loggable, path);
    }
    for (Method method : getAllMethods(loggable.getClass())) {
      MethodEval.evalMethod(method, loggable, path);
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
    Set<Field> result = new HashSet<Field>();

    result.addAll(Arrays.asList(type.getDeclaredFields()));
    result.addAll(Arrays.asList(type.getFields()));

    return result.stream().toList();
  }

  private static List<Method> getAllMethods(Class<?> type) {
    Set<Method> result = new HashSet<Method>();

    result.addAll(Arrays.asList(type.getDeclaredMethods()));
    result.addAll(Arrays.asList(type.getMethods()));

    return result.stream().toList();
  }

  public static Boolean isDebug() {
    return DEBUG;
  }

  public static void setDebug(Boolean debug) {
    DEBUG = debug;
  }
}
