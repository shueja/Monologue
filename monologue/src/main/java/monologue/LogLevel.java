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
}
