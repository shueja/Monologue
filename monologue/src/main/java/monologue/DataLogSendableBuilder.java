package monologue;

import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.Topic;
import edu.wpi.first.util.datalog.BooleanArrayLogEntry;
import edu.wpi.first.util.datalog.BooleanLogEntry;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DataLogEntry;
import edu.wpi.first.util.datalog.DoubleArrayLogEntry;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.datalog.FloatArrayLogEntry;
import edu.wpi.first.util.datalog.FloatLogEntry;
import edu.wpi.first.util.datalog.IntegerArrayLogEntry;
import edu.wpi.first.util.datalog.IntegerLogEntry;
import edu.wpi.first.util.datalog.RawLogEntry;
import edu.wpi.first.util.datalog.StringArrayLogEntry;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.util.function.BooleanConsumer;
import edu.wpi.first.util.function.FloatConsumer;
import edu.wpi.first.util.function.FloatSupplier;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

class DataLogSendableBuilder implements NTSendableBuilder {
  private static final DataLog log = DataLogManager.getLog();
  private static final NetworkTable rootTable =
      NetworkTableInstance.getDefault().getTable("DataLogSendable");

  private static Optional<NetworkTable> networkTable = Optional.empty();

  private final Map<DataLogEntry, Supplier<?>> dataLogMap = new HashMap<>();
  private final List<Runnable> updateTables = new ArrayList<>();
  private final List<AutoCloseable> closeables = new ArrayList<>();
  private String prefix;

  DataLogSendableBuilder(String prefix) {
    if (!prefix.endsWith("/")) {
      this.prefix = prefix + "/";
    } else {
      this.prefix = prefix;
    }
  }

  @Override
  public void setSafeState(Runnable func) {}

  @Override
  public void setActuator(boolean value) {}

  @Override
  public void setSmartDashboardType(String type) {}

  @Override
  public BackendKind getBackendKind() {
    return BackendKind.kUnknown;
  }

  @Override
  public boolean isPublished() {
    return true;
  }

  @Override
  public void clearProperties() {
    dataLogMap.clear();
  }

  @Override
  public void close() {
    clearProperties();
    for (AutoCloseable c : closeables) {
      try {
        c.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void addCloseable(AutoCloseable c) {
    closeables.add(c);
  }

  @Override
  public void addBooleanProperty(String key, BooleanSupplier getter, BooleanConsumer setter) {
    if (getter != null) {
      dataLogMap.put(new BooleanLogEntry(log, prefix + key), () -> getter.getAsBoolean());
    }
  }

  @Override
  public void addDoubleProperty(String key, DoubleSupplier getter, DoubleConsumer setter) {
    if (getter != null) {
      dataLogMap.put(new DoubleLogEntry(log, prefix + key), () -> getter.getAsDouble());
    }
  }

  @Override
  public void addStringProperty(String key, Supplier<String> getter, Consumer<String> setter) {
    if (getter != null) {
      dataLogMap.put(new StringLogEntry(log, prefix + key), getter);
    }
  }

  @Override
  public void addRawProperty(
      String key, String typeString, Supplier<byte[]> getter, Consumer<byte[]> setter) {
    if (getter != null) {
      dataLogMap.put(new RawLogEntry(log, prefix + key + "(" + typeString + ")"), getter);
    }
  }

  @Override
  public void addFloatProperty(String key, FloatSupplier getter, FloatConsumer setter) {
    if (getter != null) {
      dataLogMap.put(new FloatLogEntry(log, prefix + key), () -> getter.getAsFloat());
    }
  }

  @Override
  public void addIntegerProperty(String key, LongSupplier getter, LongConsumer setter) {
    if (getter != null) {
      dataLogMap.put(new IntegerLogEntry(log, prefix + key), (Supplier<?>) getter);
    }
  }

  @Override
  public void addBooleanArrayProperty(
      String key, Supplier<boolean[]> getter, Consumer<boolean[]> setter) {
    if (getter != null) {
      dataLogMap.put(new BooleanArrayLogEntry(log, prefix + key), getter);
    }
  }

  @Override
  public void addDoubleArrayProperty(
      String key, Supplier<double[]> getter, Consumer<double[]> setter) {
    if (getter != null) {
      dataLogMap.put(new DoubleArrayLogEntry(log, prefix + key), getter);
    }
  }

  @Override
  public void addStringArrayProperty(
      String key, Supplier<String[]> getter, Consumer<String[]> setter) {
    if (getter != null) {
      dataLogMap.put(new StringArrayLogEntry(log, prefix + key), getter);
    }
  }

  @Override
  public void addFloatArrayProperty(
      String key, Supplier<float[]> getter, Consumer<float[]> setter) {
    if (getter != null) {
      dataLogMap.put(new FloatArrayLogEntry(log, prefix + key), getter);
    }
  }

  @Override
  public void addIntegerArrayProperty(
      String key, Supplier<long[]> getter, Consumer<long[]> setter) {
    if (getter != null) {
      dataLogMap.put(new IntegerArrayLogEntry(log, prefix + key), getter);
    }
  }

  @Override
  public NetworkTable getTable() {
    if (networkTable.isPresent()) {
      return networkTable.get();
    } else {
      networkTable = Optional.of(rootTable.getSubTable(prefix));
      /// NTLogger.addNetworkTable(networkTable.get(), prefix);
      return networkTable.get();
    }
  }

  @Override
  public void setUpdateTable(Runnable func) {
    updateTables.add(func);
  }

  @Override
  public Topic getTopic(String key) {
    return getTable().getTopic(key);
  }

  @Override
  public void update() {
    long timestamp = (long) (Timer.getFPGATimestamp() * 1e6);
    for (Map.Entry<DataLogEntry, Supplier<?>> entry : dataLogMap.entrySet()) {
      var key = entry.getKey();
      var val = entry.getValue().get();
      if (key instanceof BooleanArrayLogEntry) {
        ((BooleanArrayLogEntry) key).append((boolean[]) val, timestamp);

      } else if (key instanceof BooleanLogEntry) {
        ((BooleanLogEntry) key).append((boolean) val, timestamp);

      } else if (key instanceof DoubleArrayLogEntry) {
        ((DoubleArrayLogEntry) key).append((double[]) val, timestamp);

      } else if (key instanceof DoubleLogEntry) {
        ((DoubleLogEntry) key).append((double) val, timestamp);

      } else if (key instanceof FloatArrayLogEntry) {
        ((FloatArrayLogEntry) key).append((float[]) val, timestamp);

      } else if (key instanceof FloatLogEntry) {
        ((FloatLogEntry) key).append((float) val, timestamp);

      } else if (key instanceof IntegerArrayLogEntry) {
        ((IntegerArrayLogEntry) key).append((long[]) val, timestamp);

      } else if (key instanceof IntegerLogEntry) {
        ((IntegerLogEntry) key).append((long) val, timestamp);

      } else if (key instanceof RawLogEntry) {
        ((RawLogEntry) key).append((byte[]) val, timestamp);

      } else if (key instanceof StringArrayLogEntry) {
        ((StringArrayLogEntry) key).append((String[]) val, timestamp);

      } else if (key instanceof StringLogEntry) {
        ((StringLogEntry) key).append((String) val, timestamp);
      }
    }
    for (Runnable updateTable : updateTables) {
      updateTable.run();
    }
  }

  @Override
  public void publishConstBoolean(String key, boolean value) {
    new BooleanLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstInteger(String key, long value) {
    new IntegerLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstFloat(String key, float value) {
    new FloatLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstDouble(String key, double value) {
    new DoubleLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstString(String key, String value) {
    new StringLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstBooleanArray(String key, boolean[] value) {
    new BooleanArrayLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstIntegerArray(String key, long[] value) {
    new IntegerArrayLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstFloatArray(String key, float[] value) {
    new FloatArrayLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstDoubleArray(String key, double[] value) {
    new DoubleArrayLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstStringArray(String key, String[] value) {
    new StringArrayLogEntry(log, prefix + key).append(value);
  }

  @Override
  public void publishConstRaw(String key, String typeString, byte[] value) {
    new RawLogEntry(log, prefix + key).append(value);
  }
}
