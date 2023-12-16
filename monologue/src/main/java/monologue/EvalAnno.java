package monologue;

import java.lang.reflect.AccessibleObject;

import monologue.Annotations.LogFile;
import monologue.Annotations.LogNT;
import monologue.Annotations.LogOnceFile;
import monologue.Annotations.LogOnceNT;

class EvalAnno {
    
    public static enum LogType {
        File, Nt, None;
    }

    public static LogType annoEval(AccessibleObject element) {
        if (element.isAnnotationPresent(LogNT.class) || element.isAnnotationPresent(LogOnceNT.class)) {
            return LogType.Nt;
        } else if (element.isAnnotationPresent(LogFile.class) || element.isAnnotationPresent(LogOnceFile.class)) {
            return LogType.File;
        } else {
            return LogType.None;
        }
    }

    public static class LogMetadata {
        public final LogLevel level;
        public final boolean once;
        public final String relativePath;

        public LogMetadata(LogLevel level, boolean once, String path) {
            this.level = level;
            this.once = once;
            this.relativePath = path;
        }

        public static LogMetadata from(AccessibleObject element) {
            if (element.isAnnotationPresent(LogFile.class)) {
                LogFile anno = element.getAnnotation(LogFile.class);
                return new LogMetadata(anno.level(), false, anno.path());
            } else if (element.isAnnotationPresent(LogNT.class)) {
                LogNT anno = element.getAnnotation(LogNT.class);
                return new LogMetadata(anno.level(), false, anno.path());
            } else if (element.isAnnotationPresent(LogOnceFile.class)) {
                LogOnceFile anno = element.getAnnotation(LogOnceFile.class);
                return new LogMetadata(LogLevel.DEFAULT, true, anno.path());
            } else if (element.isAnnotationPresent(LogOnceNT.class)) {
                LogOnceNT anno = element.getAnnotation(LogOnceNT.class);
                return new LogMetadata(LogLevel.DEFAULT, true, anno.path());
            } else {
                return null;
            }
        }
    }
}
