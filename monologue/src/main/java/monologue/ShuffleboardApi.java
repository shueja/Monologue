package monologue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;

/** Lighter weight interface for shuffleboard widgets */
public class ShuffleboardApi {
  private static final NetworkTable shuffleboardNetworkTable = NetworkTableInstance.getDefault()
      .getTable("Shuffleboard");
  private static final NetworkTable metaNetworkTable = shuffleboardNetworkTable.getSubTable(".metadata");
  private static final NetworkTableEntry tabsEntry = metaNetworkTable.getEntry("Tabs");
  private static final Map<sbPath, ShuffleTab> tables = new HashMap<>();
  private static final Map<String, Runnable> tasks = new HashMap<>();

  /**
   * Updates all the shuffleboard entry values
   * This method should be called every cycle unless
   * {@link ShuffleboardApi#registerPeriodic(TimedRobot)} is used
   */
  public static void run() {
    tasks.values().forEach(Runnable::run);
  }

  /**
   * Adds shuffleboard updating to the robot periodic loop,
   * if this method is used {@link ShuffleboardApi#run()} does not need to be
   * called
   * 
   * @param robot the robot to add the periodic loop to
   */
  public static void registerPeriodic(TimedRobot robot) {
    robot.addPeriodic(ShuffleboardApi::run, 0.01);
  }

  /**
   * An internal representation of an NT path,
   * more ergonomic than a String
   */
  private static class sbPath {
    private final String[] paths;

    sbPath(String... paths) {
      this.paths = paths;
    }

    /**
     * Uses raw array indexing so can be unsafe
     * 
     * @param index the index of the path to get
     * @return the path at the given index
     */
    public String get(int index) {
      return paths[index];
    }

    public int len() {
      return paths.length;
    }

    public Boolean isValid() {
      // len has to be more than 2 but less than 5
      // all paths have to be non-empty
      // first path has to be "Shuffleboard"
      if (len() < 3 || len() > 4)
        return false;
      for (String path : paths) {
        if (path.isEmpty())
          return false;
      }
      return paths[0].equals("Shuffleboard");
    }

    public String compress() {
      StringBuilder sb = new StringBuilder();
      for (String path : paths) {
        sb.append(path).append("/");
      }
      return sb.substring(0, sb.length() - 1);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof sbPath) {
        sbPath other = (sbPath) obj;
        if (other.len() != len())
          return false;
        for (int i = 0; i < len(); i++) {
          if (!other.get(i).equals(get(i)))
            return false;
        }
        return true;
      }
      return false;
    }

    @Override
    public int hashCode() {
      int hash = 0;
      for (String path : paths) {
        hash += path.hashCode();
      }
      return hash;
    }

    public static sbPath fromPath(String path) {
      while (path.startsWith("/"))
        path = path.substring(1);
      while (path.endsWith("/"))
        path = path.substring(0, path.length() - 1);
      if (!path.startsWith("Shuffleboard"))
        path = "Shuffleboard/" + path;
      return new sbPath(path.split("/"));
    }
  }

  /**
   * Represents the supported metadata fields
   */
  public enum MetadataFields {
    Size("Size"),
    Position("Position"),
    Widget("PreferredComponent"),
    Properties("Properties");

    public final String str;

    MetadataFields(String str) {
      this.str = str;
    }
  }

  /**
   * Represents an object than can contain any number of entries
   */
  public static interface ShuffleEntryContainer {

    /**
     * Adds a new entry to the container that isnt updated
     * 
     * @param name  the name of the entry
     * @param value the value of the entry
     * @return the entry
     * 
     * @apiNote if the name is an already existing entry,
     *          a warning will be emitted and the new entry will not be added
     *          instead returning the existing entry
     */
    public ShuffleEntry addEntryOnce(String name, Object value);

    /**
     * Adds a new entry to the container that is updated by the supplier
     * 
     * @param name          the name of the entry
     * @param valueSupplier the supplier of the value
     * @return the entry
     * 
     * @apiNote if the name is an already existing entry,
     *          a warning will be emitted and the new entry will not be added
     *          instead returning the existing entry
     */
    public ShuffleEntry addEntry(String name, Supplier<?> valueSupplier);

    /**
     * Gets an entry from the container
     * 
     * @param name the name of the entry
     * @return the possibly empty entry
     */
    public Optional<ShuffleEntry> getEntry(String name);

    /**
     * Gets all the entries in the container
     * 
     * @return the list of entries
     */
    public List<ShuffleEntry> getEntries();

    /**
     * Adds a new boolean entry to the container that is updated by the supplier
     * 
     * @param name          the name of the entry
     * @param valueSupplier the supplier of the boolean
     * @return the entry
     * 
     * @apiNote if the name is an already existing entry,
     *          a warning will be emitted and the new entry will not be added
     *          instead returning the existing entry
     */
    default public ShuffleEntry addBoolean(String name, Supplier<Boolean> valueSupplier) {
      return this.addEntry(name, valueSupplier);
    }

    /**
     * Adds a new double entry to the container that is updated by the supplier
     * 
     * @param name          the name of the entry
     * @param valueSupplier the supplier of the double
     * @return the entry
     * 
     * @apiNote if the name is an already existing entry,
     *          a warning will be emitted and the new entry will not be added
     *          instead returning the existing entry
     */
    default public ShuffleEntry addDouble(String name, Supplier<Double> valueSupplier) {
      return this.addEntry(name, valueSupplier);
    }

    /**
     * Adds a new string entry to the container that is updated by the supplier
     * 
     * @param name          the name of the entry
     * @param valueSupplier the supplier of the string
     * @return the entry
     * 
     * @apiNote if the name is an already existing entry,
     *          a warning will be emitted and the new entry will not be added
     *          instead returning the existing entry
     */
    default public ShuffleEntry addString(String name, Supplier<String> valueSupplier) {
      return this.addEntry(name, valueSupplier);
    }

    /**
     * Adds a new boolean array entry to the container that is updated by the
     * supplier
     * 
     * @param name          the name of the entry
     * @param valueSupplier the supplier of the boolean array
     * @return the entry
     * 
     * @apiNote if the name is an already existing entry,
     *          a warning will be emitted and the new entry will not be added
     *          instead returning the existing entry
     */
    default public ShuffleEntry addBooleanArray(String name, Supplier<Boolean[]> value) {
      return this.addEntry(name, value);
    }

    /**
     * Adds a new double array entry to the container that is updated by the
     * supplier
     * 
     * @param name          the name of the entry
     * @param valueSupplier the supplier of the double array
     * @return the entry
     * 
     * @apiNote if the name is an already existing entry,
     *          a warning will be emitted and the new entry will not be added
     *          instead returning the existing entry
     */
    default public ShuffleEntry addDoubleArray(String name, Supplier<Double[]> value) {
      return this.addEntry(name, value);
    }

    /**
     * Adds a new string array entry to the container that is updated by the
     * supplier
     * 
     * @param name          the name of the entry
     * @param valueSupplier the supplier of the string array
     * @return the entry
     * 
     * @apiNote if the name is an already existing entry,
     *          a warning will be emitted and the new entry will not be added
     *          instead returning the existing entry
     */
    default public ShuffleEntry addStringArray(String name, Supplier<String[]> value) {
      return this.addEntry(name, value);
    }
  }

  /**
   * Represents a Shuffleboard tab that can contain any number of entries and
   * layouts
   */
  public static class ShuffleTab implements ShuffleEntryContainer {
    private final NetworkTable table;
    private final NetworkTable metaTable;
    private final HashMap<String, ShuffleEntry> entries = new HashMap<>();
    private final HashMap<String, ShuffleLayout> layouts = new HashMap<>();
    private final sbPath path;

    private ShuffleTab(String name) {
      path = sbPath.fromPath(name);
      this.table = shuffleboardNetworkTable.getSubTable(path.get(1));
      this.metaTable = metaNetworkTable.getSubTable(path.get(1));
      table.getEntry(".type").setString("ShuffleboardTab");
      tables.put(path, this);
    }

    @Override
    public Optional<ShuffleEntry> getEntry(String name) {
      if (entries.keySet().contains(name)) {
        return Optional.of(entries.get(name));
      } else {
        return Optional.empty();
      }
    }

    @Override
    public List<ShuffleEntry> getEntries() {
      return new ArrayList<>(entries.values());
    }

    public String getName() {
      return path.get(path.len() - 1);
    }

    /**
     * Gets a layout from the tab, creating it if it doesnt exist
     * 
     * @param name the name of the layout
     * @return the layout
     */
    public ShuffleLayout getLayout(String name) {
      if (layouts.keySet().contains(name)) {
        return layouts.get(name);
      } else {
        var out = new ShuffleLayout(path.compress() + "/" + name);
        layouts.put(name, out);
        return out;
      }
    }

    /**
     * Adds a sendable to the tab
     * 
     * @param name     the name of the sendable
     * @param sendable the sendable
     */
    public void addSendable(String name, Sendable sendable) {
      var builder = new SendableBuilderImpl();
      builder.setTable(table.getSubTable(name));
      sendable.initSendable(builder);
      builder.startListeners();
      table.getSubTable(name).getEntry(".controllable").setBoolean(false);
      tasks.put(path.compress() + "/" + name, () -> builder.update());
    }

    @Override
    public ShuffleEntry addEntryOnce(String name, Object value) {
      if (entries.keySet().contains(name)) {
        DriverStation.reportWarning("Already exists: " + path.compress() + "/" + name, false);
        return entries.get(name);
      }
      var entry = table.getEntry(name);
      entry.setValue(value);
      var out = new ShuffleEntry(path.compress() + "/" + name, entry, metaTable.getSubTable(name));
      entries.put(name, out);
      return out;
    }

    @Override
    public ShuffleEntry addEntry(String name, Supplier<?> valueSupplier) {
      if (entries.keySet().contains(name)) {
        DriverStation.reportWarning("Already exists: " + path.compress() + "/" + name, false);
        return entries.get(name);
      }
      String entryPath = path.compress() + "/" + name;
      var entry = table.getEntry(name);
      entry.setValue(valueSupplier.get());
      tasks.put(entryPath, () -> entry.setValue(valueSupplier.get()));
      var out = new ShuffleEntry(entryPath, entry, metaTable.getSubTable(name));
      entries.put(name, out);
      return out;
    }
  }

  /**
   * Represents an object that can have its metadata set
   */
  public static interface ShuffleMetadataCarrier {
    /**
     * A more raw unsafe method for setting metadata
     * 
     * @param metadata the metadata to set
     * @return the entry/layout
     */
    public ShuffleMetadataCarrier applyMetadata(Map<MetadataFields, Object> metadata);

    /**
     * A builder method for updating the metadata of an entry/layout,
     * allows for easy chaining into other metadata methods
     * 
     * @param properties the properties to set
     * @return the entry/layout
     */
    default public ShuffleMetadataCarrier withProperties(Map<String, Object> properties) {
      return this.applyMetadata(Map.of(MetadataFields.Properties, properties));
    }

    /**
     * A builder method for updating the metadata of an entry/layout,
     * allows for easy chaining into other metadata methods
     * 
     * @param width  the width of the entry/layout in the tabs grid
     * @param height the height of the entry/layout in the tabs grid
     * @return the entry/layout
     */
    default public ShuffleMetadataCarrier withSize(Integer width, Integer height) {
      return this.applyMetadata(Map.of(MetadataFields.Size, new double[] {
          width, height
      }));
    }

    /**
     * A builder method for updating the metadata of an entry/layout,
     * allows for easy chaining into other metadata methods
     * 
     * @param columnIndex the column index of the entry/layout in the tabs grid
     * @param rowIndex    the row index of the entry/layout in the tabs grid
     * @return the entry/layout
     */
    default public ShuffleMetadataCarrier withPosition(int columnIndex, int rowIndex) {
      return this.applyMetadata(Map.of(MetadataFields.Position, new double[] {
          columnIndex, rowIndex
      }));
    }
  }

  /**
   * Represents a Shuffleboard layout that can contain any number of entries and
   * supports metadata
   */
  public static class ShuffleLayout implements ShuffleEntryContainer, ShuffleMetadataCarrier {
    private final NetworkTable table;
    private final NetworkTable metaTable;
    private final Map<String, ShuffleEntry> entries = new HashMap<>();
    private final sbPath path;
    private NetworkTable propertiesTable;

    private ShuffleLayout(String _path) {
      path = sbPath.fromPath(_path);
      this.table = shuffleboardNetworkTable.getSubTable(path.get(1)).getSubTable(path.get(2));
      this.metaTable = metaNetworkTable.getSubTable(path.get(1)).getSubTable(path.get(2));
      table.getEntry(".type").setString("ShuffleboardLayout");
      metaTable.getEntry("PreferredComponent").setString("ListLayout");
    }

    @Override
    public Optional<ShuffleEntry> getEntry(String name) {
      if (entries.keySet().contains(name)) {
        return Optional.of(entries.get(name));
      } else {
        return Optional.empty();
      }
    }

    @Override
    public List<ShuffleEntry> getEntries() {
      return new ArrayList<>(entries.values());
    }

    public String getName() {
      return path.get(path.len() - 1);
    }

    @Override
    public ShuffleEntry addEntryOnce(String name, Object value) {
      if (entries.keySet().contains(name)) {
        DriverStation.reportWarning("Already exists: " + path.compress() + "/" + name, false);
        return entries.get(name);
      }
      var entry = table.getEntry(name);
      entry.setValue(value);
      var out = new ShuffleEntry(path.compress() + "/" + name, entry, metaTable.getSubTable(name));
      entries.put(name, out);
      return out;
    }

    @Override
    public ShuffleEntry addEntry(String name, Supplier<?> valueSupplier) {
      if (entries.keySet().contains(name)) {
        DriverStation.reportWarning("Already exists: " + path.compress() + "/" + name, false);
        return entries.get(name);
      }
      String entryPath = path.compress() + "/" + name;
      var entry = table.getEntry(name);
      entry.setValue(valueSupplier.get());
      tasks.put(entryPath, () -> entry.setValue(valueSupplier.get()));
      var out = new ShuffleEntry(entryPath, entry, metaTable.getSubTable(name));
      entries.put(name, out);
      return out;
    }

    @Override
    public ShuffleMetadataCarrier applyMetadata(Map<MetadataFields, Object> metadata) {
      for (var field : metadata.keySet()) {
        switch (field) {
          case Size:
            metaTable.getEntry("Size").setDoubleArray((double[]) metadata.get(field));
            break;
          case Position:
            metaTable.getEntry("Position").setDoubleArray((double[]) metadata.get(field));
            break;
          case Widget:
            metaTable.getEntry("PreferredComponent").setString((String) metadata.get(field));
            break;
          case Properties:
            this.propertiesTable = metaTable.getSubTable("Properties");
            @SuppressWarnings("unchecked")
            Map<String, Object> properties = (Map<String, Object>) metadata.get(field);
            properties.forEach((k, v) -> propertiesTable.getEntry(k).setValue(v));
            break;
        }
      }
      return this;
    }
  }

  /**
   * Represents a Shuffleboard entry, typically a widget
   */
  public static class ShuffleEntry implements ShuffleMetadataCarrier {
    private final NetworkTableEntry entry;
    private final NetworkTable metaTable;
    private final sbPath path;
    private NetworkTable propertiesTable;

    private ShuffleEntry(String path, NetworkTableEntry entry, NetworkTable metaTable) {
      this.path = sbPath.fromPath(path);
      this.entry = entry;
      this.metaTable = metaTable;
      this.metaTable.getEntry("Controllable").setBoolean(false);
    }

    /**
     * Get the name of this Shuffleboard entry
     * 
     * @return the name of this Shuffleboard entry
     */
    public String getName() {
      return path.get(path.len() - 1);
    }

    /**
     * Get the NetworkTableEntry associated with this Shuffleboard entry,
     * best for retrieving values
     * 
     * @return the NetworkTableEntry
     */
    public NetworkTableEntry getNtEntry() {
      return entry;
    }

    @Override
    public ShuffleMetadataCarrier applyMetadata(Map<MetadataFields, Object> metadata) {
      for (var field : metadata.keySet()) {
        switch (field) {
          case Size:
            metaTable.getEntry("Size").setDoubleArray((double[]) metadata.get(field));
            break;
          case Position:
            metaTable.getEntry("Position").setDoubleArray((double[]) metadata.get(field));
            break;
          case Widget:
            metaTable.getEntry("PreferredComponent").setString((String) metadata.get(field));
            break;
          case Properties:
            if (propertiesTable == null) {
              this.propertiesTable = metaTable.getSubTable("Properties");
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> properties = (Map<String, Object>) metadata.get(field);
            properties.forEach((k, v) -> propertiesTable.getEntry(k).setValue(v));
            break;
        }
      }
      return this;
    }
  }

  /**
   * Gets a Shuffleboard tab, creating it if it does not exist
   * 
   * @param name the name of the tab
   * @return the Shuffleboard tab
   */
  public static ShuffleTab getTab(String name) {
    // tab already exists short circuit
    var cmp = sbPath.fromPath(name);
    if (tables.containsKey(cmp)) {
      return tables.get(cmp);
    }

    // tab does not exist, create it and update the list of tabs
    var currentTabs = tabsEntry.getStringArray(new String[0]);
    String[] newTabs = new String[currentTabs.length + 1];
    System.arraycopy(currentTabs, 0, newTabs, 0, currentTabs.length);
    newTabs[currentTabs.length] = name;
    tabsEntry.setStringArray(newTabs);
    return new ShuffleTab(name);
  }

  /**
   * A less safe and ergonomic way of creating a Shuffleboard entry
   * 
   * @param path     the path to the entry
   * @param supplier the supplier for the entry
   * @param metadata the metadata for the entry
   * 
   * @apinote this method is not recommended for use unless you know what your
   *          doing
   */
  public static void createEntry(String path, Supplier<?> supplier, Map<MetadataFields, Object> metadata) {
    var npath = sbPath.fromPath(path);
    var nmetadata = metadata == null ? new HashMap<MetadataFields, Object>() : metadata;
    if (!npath.isValid()) {
      DriverStation.reportError("Invalid path: " + path, false);
      return;
    } else if (npath.len() == 3) {
      // tab and entry
      getTab(npath.get(1)).addEntry(npath.get(2), supplier).applyMetadata(nmetadata);
    } else if (npath.len() == 4) {
      // tab, layout, and entry
      getTab(npath.get(1)).getLayout(npath.get(2)).addEntry(npath.get(3), supplier).applyMetadata(nmetadata);
    }
  }

  /**
   * A less safe and ergonomic way of creating a Shuffleboard entry
   * 
   * @param path     the path to the entry
   * @param supplier the supplier for the entry
   * 
   * @apinote this method is not recommended for use unless you know what your
   *          doing
   */
  public static void createEntry(String path, Supplier<?> supplier) {
    createEntry(path, supplier, null);
  }
}
