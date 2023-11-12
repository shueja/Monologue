package monologue.util;

import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

public class MonologueLog {
    private static final StringLogEntry entry = new StringLogEntry(DataLogManager.getLog(), "/MonologueSetup");
    static {
        entry.append("Monologue Setup Logger initialized");
    }
    public static synchronized void SetupLog(String message) {
        entry.append(message);
    }
}
