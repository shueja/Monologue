package monologue;

import edu.wpi.first.util.datalog.*;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DataLogManager;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.function.LongConsumer;
import edu.wpi.first.util.struct.Struct;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class DataLogger extends GenericLogger {
    String prefix = "";
    final DataLog log;

    public DataLogger() {
        super();
        DataLogManager.logNetworkTables(false);
        log = DataLogManager.getLog();
    }

    private static class NTIntegerArrayLogEntry extends DataLogEntry {
        public static final String kDataType = "int[]";

        public NTIntegerArrayLogEntry(DataLog log, String name, String metadata, long timestamp) {
            super(log, name, kDataType, metadata, timestamp);
        }

        public NTIntegerArrayLogEntry(DataLog log, String name) {
            this(log, name, "", 0);
        }

        public void append(long[] value, long timestamp) {
            m_log.appendIntegerArray(m_entry, value, timestamp);
        }

        public void append(long[] value) {
            m_log.appendIntegerArray(m_entry, value, 0);
        }
    }



    @Override
    public void put(String entryName, boolean value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }



        new BooleanLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addBoolean(
        String entryName,
        Supplier<Boolean>valueSupplier,
        LogLevel level
    ) {

        var entry = new BooleanLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private boolean lastValue = false;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}
                    if (value != lastValue) {
                        entry.append(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();

                entry.append(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, int value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }



        new IntegerLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addInteger(
        String entryName,
        Supplier<Integer>valueSupplier,
        LogLevel level
    ) {

        var entry = new IntegerLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private int lastValue = 0;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}
                    if (value != lastValue) {
                        entry.append(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();

                entry.append(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, long value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }



        new IntegerLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addLong(
        String entryName,
        Supplier<Long>valueSupplier,
        LogLevel level
    ) {

        var entry = new IntegerLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private long lastValue = 0;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}
                    if (value != lastValue) {
                        entry.append(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();

                entry.append(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, float value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }



        new FloatLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addFloat(
        String entryName,
        Supplier<Float>valueSupplier,
        LogLevel level
    ) {

        var entry = new FloatLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private float lastValue = 0;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}
                    if (value != lastValue) {
                        entry.append(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();

                entry.append(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, double value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }



        new DoubleLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addDouble(
        String entryName,
        Supplier<Double>valueSupplier,
        LogLevel level
    ) {

        var entry = new DoubleLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private double lastValue = 0;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}
                    if (value != lastValue) {
                        entry.append(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();

                entry.append(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, String value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }

        if (value == null) {return;}



        new StringLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addString(
        String entryName,
        Supplier<String>valueSupplier,
        LogLevel level
    ) {

        var entry = new StringLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private String lastValue = "";
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}
                    if (value != lastValue) {
                        entry.append(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();

                if (value == null) {return;}

                entry.append(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, byte[] value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }


        if (value == null) {return;}


        new RawLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addRaw(
        String entryName,
        Supplier<byte[]>valueSupplier,
        LogLevel level
    ) {

        var entry = new RawLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private byte[] lastValue = new byte[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.append(value, timestamp);
                        if (lastValue.length != value.length) {
                            lastValue = new byte[value.length];
                        }
                        System.arraycopy(value, 0, lastValue, 0, value.length);
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.append(value, timestamp);
                }
            };
        }
        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, boolean[] value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }


        if (value == null) {return;}


        new BooleanArrayLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addBooleanArray(
        String entryName,
        Supplier<boolean[]>valueSupplier,
        LogLevel level
    ) {

        var entry = new BooleanArrayLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private boolean[] lastValue = new boolean[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.append(value, timestamp);
                        if (lastValue.length != value.length) {
                            lastValue = new boolean[value.length];
                        }
                        System.arraycopy(value, 0, lastValue, 0, value.length);
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.append(value, timestamp);
                }
            };
        }
        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, int[] value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }


        if (value == null) {return;}


        new NTIntegerArrayLogEntry(log, prefix + entryName)
            .append(toLongArray(value));

    }

    @Override
    public void addIntegerArray(
        String entryName,
        Supplier<int[]>valueSupplier,
        LogLevel level
    ) {

        var entry = new NTIntegerArrayLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private long[] lastValue = new long[] {};
                @Override
                public void accept(long timestamp) {
                    var intarr = valueSupplier.get();
                    if (intarr == null) {return;}
                    var value = toLongArray(valueSupplier.get());
                    if (!(Arrays.equals(value, lastValue))) {
                        entry.append(value, timestamp);
                        if (lastValue.length != value.length) {
                            lastValue = new long[value.length];
                        }
                        System.arraycopy(value, 0, lastValue, 0, value.length);
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value == null) {return;}
                entry.append(toLongArray(value), timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, long[] value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }


        if (value == null) {return;}


        new NTIntegerArrayLogEntry(log, prefix + entryName)
        .append(value);

    }

    @Override
    public void addLongArray(
        String entryName,
        Supplier<long[]>valueSupplier,
        LogLevel level
    ) {

        var entry = new NTIntegerArrayLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private long[] lastValue = new long[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.append(value, timestamp);
                        if (lastValue.length != value.length) {
                            lastValue = new long[value.length];
                        }
                        System.arraycopy(value, 0, lastValue, 0, value.length);
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.append(value, timestamp);
                }
            };
        }
        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, float[] value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }


        if (value == null) {return;}


        new FloatArrayLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addFloatArray(
        String entryName,
        Supplier<float[]>valueSupplier,
        LogLevel level
    ) {

        var entry = new FloatArrayLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private float[] lastValue = new float[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.append(value, timestamp);
                        if (lastValue.length != value.length) {
                            lastValue = new float[value.length];
                        }
                        System.arraycopy(value, 0, lastValue, 0, value.length);
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.append(value, timestamp);
                }
            };
        }
        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, double[] value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }


        if (value == null) {return;}


        new DoubleArrayLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addDoubleArray(
        String entryName,
        Supplier<double[]>valueSupplier,
        LogLevel level
    ) {

        var entry = new DoubleArrayLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private double[] lastValue = new double[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.append(value, timestamp);
                        if (lastValue.length != value.length) {
                            lastValue = new double[value.length];
                        }
                        System.arraycopy(value, 0, lastValue, 0, value.length);
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.append(value, timestamp);
                }
            };
        }
        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void put(String entryName, String[] value, LogLevel level)
    {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }


        if (value == null) {return;}


        new StringArrayLogEntry(log, prefix + entryName).append(value);

    }

    @Override
    public void addStringArray(
        String entryName,
        Supplier<String[]>valueSupplier,
        LogLevel level
    ) {

        var entry = new StringArrayLogEntry(log, prefix + entryName);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private String[] lastValue = new String[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {return;}

                    if (!(Arrays.deepEquals(value, lastValue))) {

                        entry.append(value, timestamp);
                        if (lastValue.length != value.length) {
                            lastValue = new String[value.length];
                        }
                        System.arraycopy(value, 0, lastValue, 0, value.length);
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.append(value, timestamp);
                }
            };
        }
        addField(
            entryName,
            level,
            consumer
        );

    }


    @Override
    public void addSendable(String path, Sendable sendable) {
        if (sendable == null) {
            return;
        }
        var builder = new DataLogSendableBuilder(path);
        sendable.initSendable(builder);
        sendables.add(builder);
    }

    @Override
    public <R> void addStruct(String entryName, Struct<R> struct, Supplier<? extends R> valueSupplier, LogLevel level) {
        LongConsumer consumer;

        if (this.isLazy()) {
            var entryHandle = log.start(prefix + entryName, struct.getTypeString(), "", 0);
            log.addSchema(struct, 0);
            int size = struct.getSize();
            consumer = new LongConsumer() {
                private ByteBuffer value1 = ByteBuffer.allocate(size);
                private ByteBuffer value2 = ByteBuffer.allocate(size);
                boolean useValue1 = true; 

                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {
                        return;
                    }
                    ByteBuffer cur = useValue1 ? value1 : value2;
                    cur.position(0);
                    struct.pack(cur, value);
                    cur.position(0);
                    // checks that the buffer segments written by the struct match.
                    // ByteBuffer equality looks at the content after the position,
                    // so both positions need to be 0 at this point.
                    if (!(value1.equals(value2))) {
                        log.appendRaw(entryHandle, cur, 0, size, timestamp);
                        useValue1 = !useValue1;
                    }
                }
            };
        } else {
            var entry = StructLogEntry.create(log, prefix + entryName, struct);
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value == null) {
                    return;
                }
                entry.append(value, timestamp);
            };
        }

        addField(
                entryName,
                level,
                consumer);
    }

    @Override
    public <R> void addStructArray(String entryName, Struct<R> struct, Supplier<R[]> valueSupplier, LogLevel level) {
        LongConsumer consumer;

        if (this.isLazy()) {
            var entryHandle = log.start(prefix + entryName, struct.getTypeString()+"[]", "", 0);
            log.addSchema(struct, 0);
            int size = struct.getSize();
            consumer = new LongConsumer() {
                private ByteBuffer value1 = ByteBuffer.allocate(4 * size);
                private ByteBuffer value2 = ByteBuffer.allocate(4 * size);
                boolean useValue1 = true; 
                int lastLength = 0;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) {
                        return;
                    }
                    ByteBuffer cur = useValue1 ? value1 : value2;
                    cur.position(0);
                    if ((value.length * size) > cur.capacity()) {
                      cur =
                          ByteBuffer.allocateDirect(value.length * size * 2)
                              .order(ByteOrder.LITTLE_ENDIAN);
                    }
                    for (R v : value) {
                      struct.pack(cur, v);
                    }
                    cur.position(0);
                    cur.limit(value.length * size);
                    // checks that the buffer segments written by the struct match.
                    // ByteBuffer equality looks at the content after the position and until the limit,
                    // so both positions need to be 0 at this point.
                    if (!(
                        lastLength == value.length &&
                        value1.equals(value2)
                    )) {
                        log.appendRaw(entryHandle, cur, 0, size, timestamp);
                        useValue1 = !useValue1;
                        lastLength = value.length;
                    }
                }
            };
        } else {
            var entry = StructArrayLogEntry.create(log, prefix + entryName, struct);
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value == null) {
                    return;
                }
                entry.append(value, timestamp);
            };
        }

        addField(
                entryName,
                level,
                consumer);
    }

    @Override
    public <R> void putStruct(String entryName, Struct<R> struct, R value, LogLevel level) {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }
        if (value == null) {
            return;
        }

        StructLogEntry.create(log, prefix + entryName, struct).append(value);
    }

    @Override
    public <R> void putStructArray(String entryName, Struct<R> struct, R[] value, LogLevel level) {
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            return;
        }
        if (value == null) {
            return;
        }
        StructArrayLogEntry.create(log, prefix + entryName, struct).append(value);
    }

    @Override
    public boolean isNT() {
        return false;
    }
}