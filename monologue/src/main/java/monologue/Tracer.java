package monologue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

/**
 * A Utility class for tracing code execution time.
 * Will put info to NetworkTables under the "Tracer" table.
 */
public class Tracer {
    private static final ArrayList<String> trace = new ArrayList<>();
    private static final HashMap<String, Double> traceTimes = new HashMap<>();
    private static final HashMap<String, Double> traceStartTimes = new HashMap<>();
    private static final NetworkTable rootTable = NetworkTableInstance.getDefault().getTable("Tracer");
    private static final HashMap<String, NetworkTableEntry> entryHeap = new HashMap<>();

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

    /**
     * Starts a trace,
     * should be called at the beginning of a function thats not being called by user code.
     * Should be paired with {@link Tracer#endTrace()} at the end of the function.
     * 
     * Best used in periodic functions in Subsystems and Robot.java.
     * 
     * @param name the name of the trace, should be unique to the function.
     */
    public static void startTrace(String name) {
        trace.add(name);
        traceStartTimes.put(traceStack(), Timer.getFPGATimestamp());
    }

    /**
     * Ends a trace, should only be called at the end of a function thats not being called by user code.
     * If a {@link Tracer#startTrace(String)} is not paired with a {@link Tracer#endTrace()} there could be a crash.
     */
    public static void endTrace() {
        try {
            var startTime = traceStartTimes.get(traceStack());
            traceTimes.put(traceStack(),  Timer.getFPGATimestamp() - startTime);
            trace.remove(trace.size() - 1);
            if (trace.size() == 0) {
                endCycle();
            }
        } catch (Exception e) {
            DriverStation.reportError("[Tracer] An end trace was called with no opening trace", false);
        }
    }

    /**
     * Traces a function, should be used in place of {@link Tracer#startTrace(String)} and {@link Tracer#endTrace()}
     * for functions called by user code like {@code CommandScheduler.run()} and other expensive functions.
     * @param name the name of the trace, should be unique to the function.
     * @param runnable the function to trace.
     * 
     * @apiNote If you want to return a value then use {@link Tracer#traceFunc(String, Supplier)}.
     */
    public static void traceFunc(String name, Runnable runnable) {
        startTrace(name);
        runnable.run();
        endTrace();
    }

    /**
     * Traces a function, should be used in place of {@link Tracer#startTrace(String)} and {@link Tracer#endTrace()}
     * for functions called by user code like {@code CommandScheduler.run()} and other expensive functions.
     * 
     * @param name the name of the trace, should be unique to the function.
     * @param supplier the function to trace.
     */
    public static <T> T traceFunc(String name, Supplier<T> supplier) {
        startTrace(name);
        T ret = supplier.get();
        endTrace();
        return ret;
    }

    private static void endCycle() {
        for (var mapEntry : entryHeap.entrySet()) {
            if (!traceTimes.containsKey(mapEntry.getKey())) {
                entryHeap.remove(mapEntry.getKey()).unpublish();
            }
        }
        for (var trace : traceTimes.entrySet()) {
            NetworkTableEntry entry;
            if (!entryHeap.containsKey(trace.getKey())) {
                entry = rootTable.getEntry(trace.getKey());
                entryHeap.put(trace.getKey(), entry);
            } else {
                entry = entryHeap.get(trace.getKey());
            }
            entry.setDouble(trace.getValue());
        }
        traceTimes.clear();
    }
}
