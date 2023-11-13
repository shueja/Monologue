package monologue.evaluation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import monologue.Annotations.LogFile;
import monologue.LogLevel;
import monologue.Annotations.LogNT;

public class AnnoEval {
    
    public static enum LogType {
        File, Nt, None;
    }

    public static LogType annoEval(Method method) {
        if (method.isAnnotationPresent(LogNT.class)) {
            return LogType.Nt;
        } else if (method.isAnnotationPresent(LogFile.class)) {
            return LogType.File;
        } else {
            return LogType.None;
        }
    }

    public static LogType annoEval(Field field) {
        if (field.isAnnotationPresent(LogNT.class)) {
            return LogType.Nt;
        } else if (field.isAnnotationPresent(LogFile.class)) {
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

        public static LogMetadata from(Method method) {
            if (method.isAnnotationPresent(LogFile.class)) {
                LogFile anno = method.getAnnotation(LogFile.class);
                return new LogMetadata(anno.level(), anno.once(), anno.path());
            } else if (method.isAnnotationPresent(LogNT.class)) {
                LogNT anno = method.getAnnotation(LogNT.class);
                return new LogMetadata(anno.level(), anno.once(), anno.path());
            } else {
                return null;
            }
        }

        public static LogMetadata from(Field field) {
            if (field.isAnnotationPresent(LogFile.class)) {
                LogFile anno = field.getAnnotation(LogFile.class);
                return new LogMetadata(anno.level(), anno.once(), anno.path());
            } else if (field.isAnnotationPresent(LogNT.class)) {
                LogNT anno = field.getAnnotation(LogNT.class);
                return new LogMetadata(anno.level(), anno.once(), anno.path());
            } else {
                return null;
            }
        }
    }
}
