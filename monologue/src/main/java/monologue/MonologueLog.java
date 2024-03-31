package monologue;

import edu.wpi.first.hal.DriverStationJNI;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

class MonologueLog {
  private static final StringLogEntry entry;

  static {
    // we need to make sure we never log network tables through the implicit wpilib logger
    DataLogManager.logNetworkTables(false);
    entry = new StringLogEntry(DataLogManager.getLog(), "/MonologueSetup");
    runtimeLog("Monologue Setup Logger initialized");
  }

  private static class MonologueRuntimeError extends RuntimeException {
    MonologueRuntimeError(String message) {
      super(message);
    }
  }

  static void runtimeLog(String message) {
    entry.append("[Monologue] " + message);
  }

  static void runtimeWarn(String warning) {
    if (Monologue.shouldThrowOnWarn()) {
      throw new MonologueRuntimeError("[Monologue] " + warning);
    } else {
      String message = "[Monologue] " + warning;
      entry.append(message);
      DriverStationJNI.sendError(false, 1, false, message, "", "", true);
    }
  }
}
