package monologue;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.util.struct.StructSerializable;

/**
 * The MonoDashboard class is a utility class that provides a simple way to use Monologue's
 * logging tooling from any part of your robot code. It provides a set of put methods that
 * allow you to log data to the NetworkTables and DataLog.
 * 
 * @see Monologue
 * @see LogLevel
 */
public class MonoDashboard {
    static String ROOT_PATH = "";

    static void setRootPath(String rootPath) {
        ROOT_PATH = NetworkTable.normalizeKey(rootPath, true);
    }

    public static void put(String entryName, boolean value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, boolean value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, int value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, int value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, long value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, long value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, float value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, float value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, double value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, double value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, String value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, String value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, byte[] value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, byte[] value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, boolean[] value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, boolean[] value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, int[] value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, int[] value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, long[] value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, long[] value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, float[] value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, float[] value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, double[] value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, double[] value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void put(String entryName, String[] value) {
        put(entryName, value, LogLevel.DEFAULT);
    }
    public static void put(String entryName, String[] value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }


    public static <R extends StructSerializable> void put(String entryName, R value) {
        put(entryName, value, LogLevel.DEFAULT);
    }

    public static <R extends StructSerializable> void put(String entryName, R value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static <R extends StructSerializable> void put(String entryName, R[] value) {
        put(entryName, value, LogLevel.DEFAULT);
    }

    public static <R extends StructSerializable> void put(String entryName, R[] value, LogLevel level) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.put(entryName, value, level);

        // The Monologue made NT datalog subscriber only subs to stuff under ROOT_PATH
        // If this is sending data to a different path, we should also log it to the file
        if (!ROOT_PATH.isEmpty() && !entryName.startsWith(ROOT_PATH)) {
            if (!(level == LogLevel.DEFAULT && Monologue.isFileOnly())) {
                Monologue.dataLogger.put(entryName, value, level);
            }
        }
    }

    public static void publishSendable(String entryName, Sendable value) {
        entryName = NetworkTable.normalizeKey(entryName, true);
        if (!Monologue.isMonologueReady("MonoDashboard: " + entryName) || Monologue.isMonologueDisabled()) return;
        Monologue.ntLogger.addSendable(entryName, value);
    }
}
