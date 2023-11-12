package monologue.evaluation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import monologue.Monologue.LogBoth;
import monologue.Monologue.LogFile;
import monologue.LogLevel;
import monologue.Monologue.LogNT;

public class AnnoEval {
    
    public static enum LogType {
        File, Nt, None;
    }

    public static LogType annoEval(Method method) {
        if (method.isAnnotationPresent(LogNT.class)) {
            return LogType.Nt;
        } else if (method.isAnnotationPresent(LogBoth.class)) {
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
        } else if (field.isAnnotationPresent(LogBoth.class)) {
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

        public LogMetadata(LogLevel level, boolean once) {
            this.level = level;
            this.once = once;
        }

        public static LogMetadata from(Method method) {
            if (method.isAnnotationPresent(LogFile.class)) {
                LogFile anno = method.getAnnotation(LogFile.class);
                return new LogMetadata(anno.level(), anno.once());
            } else if (method.isAnnotationPresent(LogBoth.class)) {
                LogBoth anno = method.getAnnotation(LogBoth.class);
                return new LogMetadata(anno.level(), anno.once());
            } else if (method.isAnnotationPresent(LogNT.class)) {
                LogNT anno = method.getAnnotation(LogNT.class);
                return new LogMetadata(anno.level(), anno.once());
            } else {
                return null;
            }
        }

        public static LogMetadata from(Field field) {
            if (field.isAnnotationPresent(LogFile.class)) {
                LogFile anno = field.getAnnotation(LogFile.class);
                return new LogMetadata(anno.level(), anno.once());
            } else if (field.isAnnotationPresent(LogBoth.class)) {
                LogBoth anno = field.getAnnotation(LogBoth.class);
                return new LogMetadata(anno.level(), anno.once());
            } else if (field.isAnnotationPresent(LogNT.class)) {
                LogNT anno = field.getAnnotation(LogNT.class);
                return new LogMetadata(anno.level(), anno.once());
            } else {
                return null;
            }
        }
    }
}
