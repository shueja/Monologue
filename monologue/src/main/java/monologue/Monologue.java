package monologue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import java.util.function.BooleanSupplier;

/**
 * The Monologue class is the main entry point for the Monologue library. It is responsible for
 * setting up the Monologue library, updating the loggers, and logging objects.
 *
 * <p>Monologue is a library that allows for easy logging of objects to NetworkTables and Datalog.
 * It has {@link Annotations} that allow implicit logging of fields and methods on objects that
 * implement the {@link Logged} interface.
 *
 * <p>Monologue works by creating a tree of objects that implement the {@link Logged} interface and
 * then logging the fields and methods of those objects to NetworkTables and Datalog based on their
 * annotations. For example let's say the root object is {@code RobotContainer.java}, you would
 * implemenet {@link Logged} on the root object and then call {@link #setupMonologue(Logged, String,
 * MonologueConfig)} with the root object and a root path (typically "/Robot"). This will recurse
 * through all the fields in {@code RobotContainer.java} and search for more objects that implement
 * {@link Logged} and repeat the process until all fields and methods have been logged.
 *
 * <p>Monologue has a rich error handling system that will tell you what you did wrong and where you
 * did it wrong. If you would like to run Monologue in whole robot Unit Tests you can use {@link
 * #setupMonologueDisabled(Logged, String, boolean)} to disable logging and only run the error
 * checking.
 */
public class Monologue {

  static {
    // we need to make sure we never log network tables through the implicit wpilib logger
    DataLogManager.logNetworkTables(false);
  }

  /** The Monologue library wide FILE_ONLY flag, is used to filter logging behavior */
  private static boolean FILE_ONLY = true;

  private static MonologueConfig config = new MonologueConfig();

  private static boolean HAS_SETUP_BEEN_CALLED = false;
  private static boolean IS_DISABLED = false;
  private static boolean THROW_ON_WARN = false;
  private static boolean ALLOW_NON_FINAL_LOGGED_FIELDS = false;

  static final NTLogger ntLogger = new NTLogger();
  static final DataLogger dataLogger = new DataLogger();
  static final WeakHashMap<Logged, String> loggedRegistry = new WeakHashMap<Logged, String>();

  /**
   * An object to hold the configuration for the Monologue library. This allows for easier default
   * values, more readable code, and ability to add more configuration later without breaking
   * existing code.
   */
  public static record MonologueConfig(
      BooleanSupplier fileOnly,
      boolean lazyLogging,
      String datalogPrefix,
      boolean throwOnWarn,
      boolean allowNonFinalLoggedFields) {
    public MonologueConfig {
      if (fileOnly == null) {
        MonologueLog.runtimeWarn(
            "fileOnly cannot be null in MonologueConfig, falling back to false");
        fileOnly = () -> false;
      }
      if (datalogPrefix == null) {
        MonologueLog.runtimeWarn(
            "datalogPrefix cannot be null in MonologueConfig, falling back to \"NT:\"");
        datalogPrefix = "NT:";
      }
    }

    public MonologueConfig() {
      this(() -> false, false, "NT:", false, false);
    }

    /**
     * Updates the fileOnly flag supplier.
     *
     * @param fileOnly The new fileOnly flag supplier
     * @return A new MonologueConfig object with the updated fileOnly flag supplier
     */
    public MonologueConfig withFileOnly(BooleanSupplier fileOnly) {
      return new MonologueConfig(
          fileOnly, lazyLogging, datalogPrefix, throwOnWarn, allowNonFinalLoggedFields);
    }

    /**
     * Updates the fileOnly static flag.
     *
     * @param fileOnly The new fileOnly flag
     * @return A new MonologueConfig object with the updated fileOnly flag
     */
    public MonologueConfig withFileOnly(Boolean fileOnly) {
      return new MonologueConfig(
          () -> fileOnly, lazyLogging, datalogPrefix, throwOnWarn, allowNonFinalLoggedFields);
    }

    /**
     * Updates the lazyLogging flag.
     *
     * @param lazyLogging The new lazyLogging flag
     * @return A new MonologueConfig object with the updated lazyLogging flag
     */
    public MonologueConfig withLazyLogging(boolean lazyLogging) {
      return new MonologueConfig(
          fileOnly, lazyLogging, datalogPrefix, throwOnWarn, allowNonFinalLoggedFields);
    }

    /**
     * Updates the datalogPrefix.
     *
     * @param datalogPrefix The new datalogPrefix
     * @return A new MonologueConfig object with the updated datalogPrefix
     */
    public MonologueConfig withDatalogPrefix(String datalogPrefix) {
      return new MonologueConfig(
          fileOnly, lazyLogging, datalogPrefix, throwOnWarn, allowNonFinalLoggedFields);
    }

    /**
     * Updates the throwOnWarn flag. If true, Monologue will throw an exception when a Monologue
     * internal warning is emitted. This is useful for catching issues in CI / Unit Tests.
     *
     * @param throwOnWarn The new throwOnWarn flag
     * @return A new MonologueConfig object with the updated throwOnWarn flag
     */
    public MonologueConfig withThrowOnWarning(boolean throwOnWarn) {
      return new MonologueConfig(
          fileOnly, lazyLogging, datalogPrefix, throwOnWarn, allowNonFinalLoggedFields);
    }

    /**
     * Updates the allowNonFinalLoggedFields flag. If true, Monologue will allow non-final fields
     * containing {@link Logged} objects to be logged. This is not reccomended as it can lead to
     * unexpected behavior.
     *
     * @param allowNonFinalLoggedFields The new allowNonFinalLoggedFields flag
     * @return A new MonologueConfig object with the updated allowNonFinalLoggedFields flag
     */
    public MonologueConfig withAllowNonFinalLoggedFields(boolean allowNonFinalLoggedFields) {
      return new MonologueConfig(
          fileOnly, lazyLogging, datalogPrefix, throwOnWarn, allowNonFinalLoggedFields);
    }
  }

  /**
   * Is the main entry point for the monologue library. It will interate over every member of the
   * provided Logged object and evaluated if it should be logged to the network tables or to a file.
   *
   * <p>Will also recursively check field values for classes that implement Logged and log those as
   * well.
   *
   * @param loggable the root Logged object to log
   * @param rootpath the root path to log to\
   * @param config the configuration for the Monologue library
   * @apiNote Should only be called once, if another {@link Logged} tree needs to be created use
   *     {@link #logObj(Logged, String)} for additional trees
   */
  public static void setupMonologue(Logged loggable, String rootpath, MonologueConfig config) {
    if (HAS_SETUP_BEEN_CALLED) {
      MonologueLog.runtimeWarn(
          "Monologue.setupMonologue() has already been called, further calls will do nothing");
      return;
    }

    // create and start a timer to time the setup process
    Timer timer = new Timer();
    timer.start();

    Monologue.config = config;
    HAS_SETUP_BEEN_CALLED = true;
    rootpath = NetworkTable.normalizeKey(rootpath, true);
    MonoDashboard.setRootPath(rootpath);
    MonologueLog.runtimeLog(
        "Monologue.setupMonologue() called on "
            + loggable.getClass().getName()
            + " with rootpath "
            + rootpath
            + " and config"
            + config);

    THROW_ON_WARN = config.throwOnWarn;
    ALLOW_NON_FINAL_LOGGED_FIELDS = config.allowNonFinalLoggedFields;

    FILE_ONLY = config.fileOnly.getAsBoolean();
    ntLogger.setLazy(config.lazyLogging);
    dataLogger.setLazy(config.lazyLogging);

    dataLogger.prefix = config.datalogPrefix;

    DataLog dataLog = DataLogManager.getLog();

    NetworkTableInstance.getDefault()
        .startEntryDataLog(dataLog, rootpath, config.datalogPrefix + rootpath);
    NetworkTableInstance.getDefault().startConnectionDataLog(dataLog, "NTConnection");
    DriverStation.startDataLog(dataLog, true);

    logObj(loggable, rootpath);

    System.gc();

    MonologueLog.runtimeLog("Monologue.setupMonologue() finished in " + timer.get() + " seconds");
  }

  /**
   * Sets up Monologue in a disabled state, will not log anything.
   *
   * <p>This can be helpful for applications like unit tests where you want to validate Monoluge
   * logic and logging types without actually logging anything.
   *
   * <p>This method can also be called multiple times, this allows this to be called multiple times
   * in one unit test session without throwing an exception.
   *
   * @param loggable the root Logged object to log
   * @param rootpath the root path to log to
   * @param throwOnWarn if true, will throw an exception when a Monologue internal warning is
   *     emitted
   */
  public static void setupMonologueDisabled(Logged loggable, String rootpath, boolean throwOnWarn) {
    if (HAS_SETUP_BEEN_CALLED && !IS_DISABLED) {
      MonologueLog.runtimeWarn(
          "Monologue.setupMonologue() has already been called, disabling after setup will do nothing");
      return;
    }

    HAS_SETUP_BEEN_CALLED = true;
    IS_DISABLED = true;
    THROW_ON_WARN = throwOnWarn;

    MonologueLog.runtimeLog(
        "Monologue.setupMonologueDisabled() called on "
            + loggable.getClass().getName()
            + " with rootpath "
            + rootpath);

    // wont actually log anything, will just do state and type validation to provide use in CI/unit
    // tests
    logObj(loggable, rootpath);

    loggedRegistry.clear();

    MonologueLog.runtimeLog("Monologue.setupMonologueDisabled() finished");
  }

  /**
   * Creates a logging tree for the provided {@link Logged} object. Will also recursively check
   * field values for classes that implement {@link Logged} and log those as well.
   *
   * @param loggable the obj to scrape
   * @param path the path to log to
   * 
   * @throws IllegalStateException Make sure {@link #setupMonologue()} or
   *  {@link #setupMonologueDisabled()} is called first
   */
  public static void logObj(Logged loggable, String path) {
    if (!hasBeenSetup())
      throw new IllegalStateException(
          "Tried to use Monologue.logObj before using a Monologue setup method");

    if (path == null || path.isEmpty()) {
      MonologueLog.runtimeWarn("Invalid path for Monologue.logObj(): " + path);
      return;
    } else if (path == "/") {
      MonologueLog.runtimeWarn("Root path of / is not allowed for Monologue.logObj()");
      return;
    }
    MonologueLog.runtimeLog(
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
   * @param fileOnlyOverride the new fileOnly flag to use for the whole library, the flag will
   *     persist after this method until changed again
   * @apiNote Should only be called on the same thread monologue was setup on
   */
  public static void updateAll() {
    if (isMonologueDisabled()) return;
    if (!hasBeenSetup())
      MonologueLog.runtimeWarn("Called Monologue.updateAll before Monologue was setup");
    boolean newFileOnly = config.fileOnly.getAsBoolean();
    if (newFileOnly != FILE_ONLY)
      MonologueLog.runtimeLog("Monologue.updateAll() updated FILE_ONLY flag to " + newFileOnly);
    FILE_ONLY = newFileOnly;
    ntLogger.update(FILE_ONLY);
    dataLogger.update(FILE_ONLY);
  }

  public static void sendNetworkToFile(String subtablePath) {
    if (isMonologueDisabled()) return;
    subtablePath = NetworkTable.normalizeKey(subtablePath, true);
    NetworkTableInstance.getDefault()
        .startEntryDataLog(dataLogger.log, subtablePath, config.datalogPrefix + subtablePath);
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

  /**
   * Checks if the Monologue library is in file only mode.
   *
   * @return true if Monologue is in file only mode, false otherwise
   */
  static boolean isFileOnly() {
    return FILE_ONLY;
  }

  /**
   * Checks if the Monologue library is disabled.
   *
   * @return true if Monologue is disabled, false otherwise
   * @apiNote This is useful for unit tests where you want to validate Monologue logic and logging
   */
  static boolean isMonologueDisabled() {
    return IS_DISABLED;
  }

  /**
   * Checks if the Monologue library has been setup.
   *
   * @return true if Monologue has been setup, false otherwise
   */
  static boolean hasBeenSetup() {
    return HAS_SETUP_BEEN_CALLED;
  }

  /**
   * Checks if the Monologue library should throw an exception when a Monologue internal warning is
   * emitted.
   *
   * @return true if Monologue should throw an exception on warning, false otherwise
   */
  static boolean shouldThrowOnWarn() {
    return THROW_ON_WARN;
  }

  /**
   * Checks if the Monologue library should allow non-final fields containing {@link Logged} objects
   * to be logged.
   *
   * @return true if Monologue should allow non-final fields containing {@link Logged} objects to be
   *     logged, false otherwise
   */
  static boolean shouldAllowNonFinalLoggedFields() {
    return ALLOW_NON_FINAL_LOGGED_FIELDS;
  }

  /**
   * Checks if the Monologue library is ready to log. If it is not ready, it will log a warning
   * using the key provided.
   *
   * @param key The key to log if Monologue is not ready
   * @return true if Monologue is ready, false otherwise
   */
  static boolean isMonologueReady(String key) {
    if (!hasBeenSetup()) {
      MonologueLog.runtimeWarn("Tried to log \"" + key + "\" before Monologue was setup");
      return false;
    }
    return true;
  }
}
