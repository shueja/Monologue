package monologue;

public enum LogLevel {
    /**
     * Only on when in debug
     */
    DEBUG,
    /**
     * Always on with same behavior in and outside of debug
     */
    COMP,
    /**
     * When not in debug(aka comp) any nt loggers with this
     * level will be sent straight to file, file loggers are not affected
     */
    FILE_IN_COMP;

    /**
     * The default log level: {@link LogLevel#COMP}
     */
    public static final LogLevel DEFAULT = COMP;

    Boolean shouldLog(boolean debug, boolean nt) {
        switch (this) {
            case COMP:
                return true;
            case FILE_IN_COMP:
                if (debug && nt) {
                    return true;
                } else if (!debug && !nt) {
                    return true;
                } else {
                    return false;
                }
            case DEBUG:
                return debug;
            default:
                return false;
        }
    }
}
