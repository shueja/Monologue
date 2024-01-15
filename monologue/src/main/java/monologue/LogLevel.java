package monologue;

public enum LogLevel {
  /**
   * Will only be used when <code>FILE_ONLY</code> is set to <code>false</code>, the only benefit to
   * this is less performance overhead
   */
  NOT_FILE_ONLY,
  /** When in <code>FILE_ONLY</code> mode, only log to file otherwise log to both */
  DEFAULT,
  /** Always logs with same behavior independent of <code>FILE_ONLY</code> flag */
  OVERRIDE_FILE_ONLY;

  boolean shouldLog(boolean fileOnly, boolean nt) {
    switch (this) {
      case OVERRIDE_FILE_ONLY:
        return true;
      case DEFAULT:
        if (!fileOnly && nt) {
          return true;
        } else if (fileOnly && !nt) {
          return true;
        } else {
          return false;
        }
      case NOT_FILE_ONLY:
        return !fileOnly;
      default:
        return false;
    }
  }
}
