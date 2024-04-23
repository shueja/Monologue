package monologue;

import edu.wpi.first.util.struct.StructSerializable;

/**
 * Interface for classes that can hold {@link Monologue} annotated fields for
 * {@link Monologue#setupMonologue} and {@link Monologue#logObj} to log.
 * 
 * This class also allows for an imperative way to log values with the {@link #log} methods.
 * 
 * Class fields that hold {@code Logged} should be final.
 * 
 * @see Monologue
 * @see MonoDashboard
 * @see Annotations.Log
 */
public interface Logged {
  static String getFullPath(Logged logged) {
    return Monologue.loggedRegistry.getOrDefault(logged, "notfound");
  }

  /**
   * Normally the name of {@code this} in the object tree is based fo the field in the object the reference is stored in.
   * Overriding this method allows you to specify a different name for the object in the object tree.
   *
   * @return The name of the object in the object tree.
   */
  public default String getOverrideName() {
    return "";
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, boolean value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, boolean value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, int value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, int value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, long value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, long value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, float value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, float value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, double value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, double value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, String value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, String value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, byte[] value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, byte[] value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, boolean[] value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, boolean[] value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, int[] value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, int[] value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, long[] value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, long[] value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, float[] value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, float[] value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, double[] value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, double[] value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default void log(String key, String[] value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default void log(String key, String[] value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default <R extends StructSerializable> void log(String key, R value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default <R extends StructSerializable> void log(String key, R value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }

  /**
   * Logs a value with the default log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   */
  public default <R extends StructSerializable> void log(String key, R[] value) {
    log(key, value, LogLevel.DEFAULT);
  }

  /**
   * Logs a value with the specified log level.
   * The key is relative to the objects path this is being called in.
   * 
   * @param key The key to log the value under relative to the objects path.
   * @param value The value to log.
   * @param level The log level to log the value under.
   */
  public default <R extends StructSerializable> void log(String key, R[] value, LogLevel level) {
    if (!Monologue.isMonologueReady(key) || Monologue.isMonologueDisabled()) return;
    Monologue.ntLogger.put(getFullPath(this) + "/" + key, value, level);
  }
}
