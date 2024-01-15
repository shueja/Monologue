package monologue;

import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;

class MonologueLog {
  private static final StringLogEntry entry =
      new StringLogEntry(DataLogManager.getLog(), "/MonologueSetup");

  static {
    RuntimeLog("Monologue Setup Logger initialized");
  }

  static void RuntimeLog(String message) {
    entry.append("[Monologue] " + message);
  }

  static void RuntimeWarn(String message) {
    entry.append("[Monologue] " + message);
    DriverStation.reportWarning("[Monologue] " + message, false);
  }
}
