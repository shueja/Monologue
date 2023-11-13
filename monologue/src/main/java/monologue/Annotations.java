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
   * @param once  [optional] whether to log the field/method on update or not
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
     * Whether to log the field/method on update or not.
     */
    public boolean once() default false;

    /**
     * The log level to use.
     */
    public LogLevel level() default LogLevel.COMP;
  }

  /**
   * Logs the annotated field/method to NetworkTables if inside a {@link Logged}
   * class.
   * 
   * @param path  [optional] the relative path to log to. If empty, the path will
   *             be the name of the field/method
   * @param once  [optional] whether to log the field/method on update or not
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
     * Whether to log the field/method on update or not.
     */
    public boolean once() default false;

    /**
     * The log level to use.
     */
    public LogLevel level() default LogLevel.COMP;
  }
}
