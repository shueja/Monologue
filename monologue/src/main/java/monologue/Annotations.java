package monologue;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Annotations {

  /**
   * Logs the annotated field/method to WPILOG if inside a {@link Logged} class.
   * 
   * @param path  [optional] the relative path to log to. If empty, the path will
   *             be the name of the field/method
   * @param level [optional] the log level to use
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD, ElementType.METHOD })
  public @interface LogFile {
    /**
     * The relative path to log to. If empty, the path will be the name of the field/method.
     */
    public String path() default "";

    /**
     * The log level to use.
     * @apiNote WPILIB Senders do not obey these levels as of now
     */
    public LogLevel level() default LogLevel.COMP;
  }

  /**
   * Logs the annotated field/method to NetworkTables if inside a {@link Logged}
   * class.
   * 
   * @param path  [optional] the relative path to log to. If empty, the path will
   *             be the name of the field/method
   * @param level [optional] the log level to use
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD, ElementType.METHOD })
  public @interface LogNT {
    /**
     * The relative path to log to. If empty, the path will be the name of the field/method.
     */
    public String path() default "";

    /**
     * The log level to use.
     * @apiNote WPILIB Senders do not obey these levels as of now
     */
    public LogLevel level() default LogLevel.COMP;
  }

  /**
   * Logs the annotated field/method to WPILOG if inside a {@link Logged} class.
   * 
   * @param path  [optional] the relative path to log to. If empty, the path will
   *             be the name of the field/method
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD, ElementType.METHOD })
  public @interface LogOnceFile {
    /**
     * The relative path to log to. If empty, the path will be the name of the field/method.
     */
    public String path() default "";
  }

  /**
   * Logs the annotated field/method to NetworkTables if inside a {@link Logged}
   * class.
   * 
   * @param path  [optional] the relative path to log to. If empty, the path will
   *             be the name of the field/method
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD, ElementType.METHOD })
  public @interface LogOnceNT {
    /**
     * The relative path to log to. If empty, the path will be the name of the field/method.
     */
    public String path() default "";
  }

  /**
   * Makes the annotated field containing a {@link Logged} class not be recursed into.
   * 
   * @apiNote this will also make fields inside the object in the field not be logged
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD })
  public @interface IgnoreLogged {}
}
