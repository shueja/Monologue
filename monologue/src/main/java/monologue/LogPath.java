package monologue;

class LogPath {
  private static final String BLACKLIST = "";
  private final String[] paths;

  LogPath(String... paths) {
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

  public boolean isRoot() {
    return len() == 0;
  }

  public boolean isValid() {
    // ensure no paths are empty
    for (String path : paths) {
      if (path.equals("")) {
        return false;
      }
      for (char c : path.toCharArray()) {
        if (BLACKLIST.contains(c + "")) {
          return false;
        }
      }
    }
    return true;
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
    if (obj instanceof LogPath) {
      LogPath other = (LogPath) obj;
      if (other.len() != len()) return false;
      for (int i = 0; i < len(); i++) {
        if (!other.get(i).equals(get(i))) return false;
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

  public static LogPath from(String path) {
    while (path.startsWith("/")) path = path.substring(1);
    while (path.endsWith("/")) path = path.substring(0, path.length() - 1);
    return new LogPath(path.split("/"));
  }
}
