// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package monologue;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DataLogEntry;

/** Log array of integer values. 
 * The datatype matches NT instead of DataLog spec
 *  to avoid type mismatches when NT-logging to this key.
 * This gets around https://github.com/wpilibsuite/allwpilib/issues/6203
*/
public class NTIntegerArrayLogEntry extends DataLogEntry {
  /** The data type for integer array values. */
  public static final String kDataType = "int[]";

  /**
   * Constructs a integer array log entry.
   *
   * @param log datalog
   * @param name name of the entry
   * @param metadata metadata
   * @param timestamp entry creation timestamp (0=now)
   */
  public NTIntegerArrayLogEntry(DataLog log, String name, String metadata, long timestamp) {
    super(log, name, kDataType, metadata, timestamp);
  }

  /**
   * Constructs a integer array log entry.
   *
   * @param log datalog
   * @param name name of the entry
   * @param metadata metadata
   */
  public NTIntegerArrayLogEntry(DataLog log, String name, String metadata) {
    this(log, name, metadata, 0);
  }

  /**
   * Constructs a integer array log entry.
   *
   * @param log datalog
   * @param name name of the entry
   * @param timestamp entry creation timestamp (0=now)
   */
  public NTIntegerArrayLogEntry(DataLog log, String name, long timestamp) {
    this(log, name, "", timestamp);
  }

  /**
   * Constructs a integer array log entry.
   *
   * @param log datalog
   * @param name name of the entry
   */
  public NTIntegerArrayLogEntry(DataLog log, String name) {
    this(log, name, 0);
  }

  /**
   * Appends a record to the log.
   *
   * @param value Value to record
   * @param timestamp Time stamp (0 to indicate now)
   */
  public void append(long[] value, long timestamp) {
    m_log.appendIntegerArray(m_entry, value, timestamp);
  }

  /**
   * Appends a record to the log.
   *
   * @param value Value to record
   */
  public void append(long[] value) {
    m_log.appendIntegerArray(m_entry, value, 0);
  }
}
