package monologue;

import edu.wpi.first.util.function.FloatSupplier;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.struct.StructSerializable;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

class TypeChecker {
  private static final Class<?>[] LITERAL_TYPES = {
    boolean.class,
    int.class,
    long.class,
    float.class,
    double.class,
    Boolean.class,
    Integer.class,
    Long.class,
    Float.class,
    Double.class,
    String.class
  };

  private static final Class<?>[] EXTENDABLE_TYPES = {
    Enum.class, StructSerializable.class, Sendable.class
  };

  private static final Class<?>[] FUNCTIONAL_TYPES = {
    Supplier.class,
    DoubleSupplier.class,
    FloatSupplier.class,
    BooleanSupplier.class,
    IntSupplier.class,
    LongSupplier.class
  };

  static boolean isValidLiteralType(Class<?> type) {
    for (Class<?> literalType : LITERAL_TYPES) {
      if (literalType.isAssignableFrom(type)) {
        return true;
      }
    }
    return false;
  }

  static boolean isValidExtendableType(Class<?> type) {
    for (Class<?> extendableType : EXTENDABLE_TYPES) {
      if (extendableType.isAssignableFrom(type)) {
        return true;
      }
    }
    return false;
  }

  static boolean isValidFunctionalType(Class<?> type) {
    for (Class<?> extendableType : FUNCTIONAL_TYPES) {
      if (extendableType.isAssignableFrom(type)) {
        return true;
      }
    }
    return false;
  }

  static boolean isValidType(Class<?> type) {
    if (type.isArray()) {
      Class<?> componentType = type.getComponentType();
      return isValidLiteralType(componentType)
          || isValidExtendableType(componentType)
          || isValidFunctionalType(type);
    }

    return isValidLiteralType(type) || isValidExtendableType(type) || isValidFunctionalType(type);
  }
}
