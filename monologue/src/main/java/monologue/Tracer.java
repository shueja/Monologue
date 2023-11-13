package monologue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;

public class Tracer {
    private static final ArrayList<String> trace = new ArrayList<>();
    private static final HashMap<String, Double> traceTimes = new HashMap<>();
    private static final HashMap<String, Double> traceStartTimes = new HashMap<>();
    private static final NetworkTable rootTable = NetworkTableInstance.getDefault().getTable("Tracer");
    private static final ArrayList<NetworkTableEntry> entryHeap = new ArrayList<>();

    @SuppressWarnings("unused")
    private static String traceStack(String name) {
        StringBuilder sb = new StringBuilder();
        for (String s : trace) {
            sb.append(s);
            sb.append("/");
        }
        sb.append(name);
        return sb.toString();
    }

    private static String traceStack() {
        StringBuilder sb = new StringBuilder();
        for (String s : trace) {
            sb.append(s);
            sb.append("/");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    public static void startTrace(String name) {
        trace.add(name);
        traceStartTimes.put(traceStack(), Timer.getFPGATimestamp());
    }

    public static void endTrace() {
        var startTime = traceStartTimes.get(traceStack());
        traceTimes.put(traceStack(),  Timer.getFPGATimestamp() - startTime);
        trace.remove(trace.size() - 1);
        if (trace.size() == 0) {
            endCycle();
        }
    }

    public static void traceFunc(String name, Runnable runnable) {
        startTrace(name);
        runnable.run();
        endTrace();
    }

    public static <T> T traceFunc(String name, Supplier<T> supplier) {
        startTrace(name);
        T ret = supplier.get();
        endTrace();
        return ret;
    }

    private static void endCycle() {
        for (var entry : entryHeap) {
            entry.unpublish();
        }
        entryHeap.clear();
        for (var trace : traceTimes.entrySet()) {
            NetworkTableEntry entry = rootTable.getEntry(trace.getKey());
            entry.setDouble(trace.getValue());
            entryHeap.add(entry);
        }
        traceTimes.clear();
    }
}
