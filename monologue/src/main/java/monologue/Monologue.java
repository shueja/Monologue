package monologue;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DriverStation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Monologue {

  public static final NTLogger ntLogger = new NTLogger();
  public static final DataLogger dataLogger = new DataLogger();
  public static final Map<Logged, String> loggedRegistry = new HashMap<Logged, String>();

  private static String camelToNormal(String camelCase) {
    StringBuilder sb = new StringBuilder();
    for (char c : camelCase.toCharArray()) {
      if (Character.isUpperCase(c)) {
        sb.append(' ');
      }
      sb.append(c);
    }
    sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
    return sb.toString();
  }

  private static String methodNameFix(String name) {
    if (name.startsWith("get")) {
      name = name.substring(3);
    } else if (name.endsWith("getter")) {
      name = name.substring(0, name.length() - 6);
    }
    name = name.substring(0, 1).toLowerCase() + name.substring(1);
    return name;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  public @interface LogBoth {
    public String path() default "";

    public boolean once() default false;

    public int level() default 0;
  }

  /**
   * Annotate a field or method IN A SUBSYSTEM with this to log it to SmartDashboard
   *
   * <p>Supported Types(primitive or not): Double, Boolean, String, Integer, <br>
   * Double[], Boolean[], String[], Integer[], Sendable
   *
   * @param oneShot [optional] whether or not to only log once
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  public @interface LogFile {
    public boolean once() default false;

    public int level() default 0;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.METHOD})
  public @interface LogNT {
    public boolean once() default false;

    public int level() default 0;
  }

  private static Supplier<?> getSupplier(Field field, Logged loggable) {
    field.setAccessible(true);
    return () -> {
      try {
        return field.get(loggable);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        DriverStation.reportWarning(field.getName() + " supllier is erroring", false);
        e.printStackTrace();
        return null;
      }
    };
  }

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

  public static void setupLogging(Logged loggable, String rootPath) {
    Monologue.setupLogging(loggable, rootPath, true);
  }
  public static void setupLogging(Logged loggable, String rootPath, boolean createDataLog) {
    System.out.println(rootPath);
    loggedRegistry.put(loggable, rootPath);
    String ss_name = rootPath;
    for (Field field : getInheritedPrivateFields(loggable.getClass())) {
      
      field.setAccessible(true);
      if (isNull(field, loggable)) {

        continue;
      }
      // if the field is of type Logged
      if (Logged.class.isAssignableFrom(field.getType())) {
        try {
          String pathOverride = ((Logged) field.get(loggable)).getPath();
          if (pathOverride.equals("")) {
            pathOverride = field.getName();
          } 
          // recursion for the Logged field
          Monologue.setupLogging(
              (Logged) field.get(loggable), ss_name + "/" + pathOverride, createDataLog);
          continue;
        } catch (IllegalArgumentException | IllegalAccessException e) {
          DriverStation.reportWarning(field.getName() + " supllier is erroring", false);
          e.printStackTrace();
          continue;
        }
      }

      if (field.getType().isArray()) {
        try {
          // If primitive array
          if (Object.class.isAssignableFrom(field.get(loggable).getClass().getComponentType())) {

          // Include all elements whose runtime class is Loggable
          for (Object obj : (Object[]) field.get(loggable)) {
            if (obj instanceof Logged) {
              try {
                String pathOverride = ((Logged) obj).getPath();
                if (pathOverride.equals("")) {
                  pathOverride = obj.getClass().getSimpleName();
                } 
                Monologue.setupLogging(
                    (Logged) obj,
                    ss_name + "/" + field.getName() + "/" + pathOverride,
                    createDataLog);
                continue;
              } catch (IllegalArgumentException e) {
                DriverStation.reportWarning(field.getName() + " supllier is erroring", false);
                e.printStackTrace();
              }
            }
          }
        }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
        // Proceed on all valid elements
        // Handle collections similarly
      } else if (Collection.class.isAssignableFrom(field.getType())) {
        try {
          int idx = 0;
          // Include all elements whose runtime class is Loggable
          for (Object obj : (Collection) field.get(loggable)) {
            System.out.println(obj);
            if (obj instanceof Logged) {
              try {
                String pathOverride = ((Logged) obj).getPath();
                if (pathOverride.equals("")) {
                  pathOverride = obj.getClass().getSimpleName() + "[" + idx++ + "]";
                } 
                Monologue.setupLogging(
                    (Logged) obj,
                    ss_name + "/" + field.getName() + "/" + pathOverride,
                    createDataLog);
                continue;
              } catch (IllegalArgumentException e) {
                e.printStackTrace();
                DriverStation.reportWarning(field.getName() + " supllier is erroring", true);
              }
            }
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      if (field.getAnnotations().length == 0) {
        continue;
      }
      // setup the annotation.
      String annotationPath = "";
      boolean oneShot;
      int level;
      String name = field.getName();
      DataType type;
      try{
      type = DataType.fromClass(field.getType());
      } catch (IllegalArgumentException e) {
        DriverStation.reportWarning("Tried to log invalid type " + name + "(" + field.getType() + ") in " + ss_name, false);
        continue;
      }
      if ((field.isAnnotationPresent(LogFile.class) || field.isAnnotationPresent(LogBoth.class))
          && createDataLog) {
        dataLogger.startLog();

        LogFile annotation = field.getAnnotation(LogFile.class);
        if (annotation == null) {
          LogBoth logAnnotation = field.getAnnotation(LogBoth.class);
          annotationPath = logAnnotation.path();
          oneShot = logAnnotation.once();
          level = logAnnotation.level();
        } else {
          oneShot = annotation.once();
          level = annotation.level();
        }
        String key = annotationPath.equals("") ? ss_name + "/" + name : annotationPath;
        if (type == DataType.NTSendable) {
          dataLogger.addSendable(key, (NTSendable) getSupplier(field, loggable).get());
        } else if (type == DataType.Sendable) {
          dataLogger.addSendable(key, (Sendable) getSupplier(field, loggable).get());
        } else {
          dataLogger.helper(getSupplier(field, loggable), type, key, oneShot, level);
        }
      }
      if (field.isAnnotationPresent(LogNT.class) || field.isAnnotationPresent(LogBoth.class)) {

        LogNT annotation = field.getAnnotation(LogNT.class);
        if (annotation == null) {
          LogBoth logAnnotation = field.getAnnotation(LogBoth.class);
          annotationPath = logAnnotation.path();
          oneShot = logAnnotation.once();
          level = logAnnotation.level();
        } else {

          oneShot = annotation.once();
          level = annotation.level();
        }
        String key = annotationPath.equals("") ? ss_name + "/" + field.getName() : annotationPath;
        if (type == DataType.Sendable || type == DataType.NTSendable) {
          ntLogger.addSendable(key, (Sendable) getSupplier(field, loggable).get());
        } else {
          ntLogger.helper(getSupplier(field, loggable), type, key, oneShot, level);
        }
      }
    }

    for (Method method :getInheritedMethods(loggable.getClass())) {
      if ((method.isAnnotationPresent(LogFile.class) || method.isAnnotationPresent(LogBoth.class))
          && createDataLog) {
        dataLogger.startLog();
        method.setAccessible(true);
        String annotationPath = "";
        boolean oneShot;
        int level;
        LogFile annotation = method.getAnnotation(LogFile.class);
        if (annotation == null) {
          LogBoth logAnnotation = method.getAnnotation(LogBoth.class);
          annotationPath = logAnnotation.path();
          oneShot = logAnnotation.once();
          level = logAnnotation.level();
        } else {
          oneShot = annotation.once();
          level = annotation.level();
        }
        String name = method.getName(); // methodNameFix(method.getName());
        String path = annotationPath.equals("") ? ss_name + "/" + name : annotationPath;
        
        DataType type = DataType.fromClass(method.getReturnType());
        if (method.getParameterCount() > 0) {
          throw new IllegalArgumentException("Cannot have parameters on a LogFile method");
        }
        dataLogger.helper(getSupplier(method, loggable), type, path, oneShot, level);
      }
      if (method.isAnnotationPresent(LogNT.class) || method.isAnnotationPresent(LogBoth.class)) {
        method.setAccessible(true);
        String annotationPath = "";
        boolean oneShot;
        int level;
        LogNT annotation = method.getAnnotation(LogNT.class);
        if (annotation == null) {
          LogBoth logAnnotation = method.getAnnotation(LogBoth.class);
          annotationPath = logAnnotation.path();
          oneShot = logAnnotation.once();
          level = logAnnotation.level();
        } else {
          oneShot = annotation.once();
          level = annotation.level();
        }
        String key = annotationPath.equals("") ? ss_name + "/" + method.getName() : annotationPath;
        DataType type = DataType.fromClass(method.getReturnType());
        if (method.getParameterCount() > 0) {
          throw new IllegalArgumentException("Cannot have parameters on a LogFile method");
        }
        ntLogger.helper(getSupplier(method, loggable), type, key, oneShot, level);
      }
    }
  }

  public static void update() {
    ntLogger.update();
    dataLogger.update();
  }

  public static void updateNT() {
    ntLogger.update();
  }

  public static void updateFileLog() {
    dataLogger.update();
  }

  private static boolean isNull(Field field, Object obj) {
    field.setAccessible(true);
    boolean isNull = true;
    try {
      isNull = field.get(obj) == null;
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return isNull;
  }

  private static List<Field> getInheritedPrivateFields(Class<?> type) {
    List<Field> result = new ArrayList<Field>();

    Class<?> i = type;
    while (i != null && i != Object.class) {
        Collections.addAll(result, i.getDeclaredFields());
        i = i.getSuperclass();
    }

    return result;
}
  private static List<Method> getInheritedMethods(Class<?> type) {
    List<Method> result = new ArrayList<Method>();

    Class<?> i = type;
    while (i != null && i != Object.class) {
        Collections.addAll(result, i.getDeclaredMethods());
        i = i.getSuperclass();
    }

    return result;
  }
}
