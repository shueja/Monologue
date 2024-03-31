package monologue;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.struct.StructSerializable;
import java.util.Collection;

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

  private static final Class<?>[] COLLECTION_TYPES = {Collection.class};

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

  static boolean isValidType(Class<?> type) {
    if (type.isArray()) {
      Class<?> componentType = type.getComponentType();
      return isValidLiteralType(componentType) || isValidExtendableType(componentType);
    }

    for (Class<?> collectionType : COLLECTION_TYPES) {
      if (collectionType.isAssignableFrom(type)) {
        Class<?> elementType = type.getTypeParameters()[0].getGenericDeclaration();
        return isValidLiteralType(elementType) || isValidExtendableType(elementType);
      }
    }

    return isValidLiteralType(type) || isValidExtendableType(type);
  }
}
