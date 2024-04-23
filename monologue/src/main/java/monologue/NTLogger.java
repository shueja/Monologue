package monologue;


import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.*;
import edu.wpi.first.util.struct.Struct;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

class NTLogger extends GenericLogger {
    public NTLogger() {super();}

    private final NetworkTableInstance table = NetworkTableInstance.getDefault();

    private final Map<String, Integer> published = new HashMap<>();
    private final Map<String, Topic> topics = new HashMap<>();
    private final Map<String, StructEntry<?>> structEntrys = new HashMap<>();
    private final Map<String, StructArrayEntry<?>> structArrayEntrys = new HashMap<>();


    @Override
    public void put(String entryName, boolean value, LogLevel level) {


        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getBooleanTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((BooleanTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setBoolean(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addBoolean(String entryName, Supplier<Boolean> valueSupplier, LogLevel level) {
        var topic = table.getBooleanTopic(entryName);

        var entry = topic.getEntry(false);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private boolean lastValue = false;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return; }
                    if (value != lastValue) {
                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value == null) { return; }
                entry.set(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, int value, LogLevel level) {


        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getIntegerTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((IntegerTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setInteger(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addInteger(String entryName, Supplier<Integer> valueSupplier, LogLevel level) {
        var topic = table.getIntegerTopic(entryName);

        var entry = topic.getEntry(0);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private int lastValue = 0;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return; }
                    if (value != lastValue) {
                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value == null) { return; }
                entry.set(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, long value, LogLevel level) {


        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getIntegerTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((IntegerTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setInteger(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addLong(String entryName, Supplier<Long> valueSupplier, LogLevel level) {
        var topic = table.getIntegerTopic(entryName);

        var entry = topic.getEntry(0);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private long lastValue = 0;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return; }
                    if (value != lastValue) {
                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value == null) { return; }
                entry.set(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, float value, LogLevel level) {


        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getFloatTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((FloatTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setFloat(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addFloat(String entryName, Supplier<Float> valueSupplier, LogLevel level) {
        var topic = table.getFloatTopic(entryName);

        var entry = topic.getEntry(0);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private float lastValue = 0;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return; }
                    if (value != lastValue) {
                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value == null) { return; }
                entry.set(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, double value, LogLevel level) {


        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getDoubleTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((DoubleTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setDouble(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addDouble(String entryName, Supplier<Double> valueSupplier, LogLevel level) {
        var topic = table.getDoubleTopic(entryName);

        var entry = topic.getEntry(0);

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private double lastValue = 0;
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return; }
                    if (value != lastValue) {
                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value == null) { return; }
                entry.set(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, String value, LogLevel level) {

        if (value == null) {return;}


        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getStringTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((StringTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setString(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addString(String entryName, Supplier<String> valueSupplier, LogLevel level) {
        var topic = table.getStringTopic(entryName);

        var entry = topic.getEntry("");

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private String lastValue = "";
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return; }
                    if (value != lastValue) {
                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value == null) { return; }
                entry.set(value, timestamp);
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, byte[] value, LogLevel level) {


        if (value == null) {return;}

        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getRawTopic(entryName);

            var publisher = topic.publish("raw");

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((RawTopic) topics.get(entryName)).publish("raw").getHandle());

        }

        NetworkTablesJNI.setRaw(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addRaw(String entryName, Supplier<byte[]> valueSupplier, LogLevel level) {
        var topic = table.getRawTopic(entryName);

        var entry = topic.getEntry("raw", new byte[] {});

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private byte[] lastValue = new byte[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.set(value, timestamp);
                }
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, boolean[] value, LogLevel level) {


        if (value == null) {return;}

        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getBooleanArrayTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((BooleanArrayTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setBooleanArray(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addBooleanArray(String entryName, Supplier<boolean[]> valueSupplier, LogLevel level) {
        var topic = table.getBooleanArrayTopic(entryName);

        var entry = topic.getEntry(new boolean[] {});

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private boolean[] lastValue = new boolean[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.set(value, timestamp);
                }
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, int[] value, LogLevel level) {


        if (value == null) {return;}

        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getIntegerArrayTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((IntegerArrayTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setIntegerArray(published.get(entryName), WPIUtilJNI.now(), toLongArray(value));

    }

    @Override
    public void addIntegerArray(String entryName, Supplier<int[]> valueSupplier, LogLevel level) {
        var topic = table.getIntegerArrayTopic(entryName);

        var entry = topic.getEntry(new long[] {});

        LongConsumer consumer;

        consumer = (timestamp) -> {
            var value = valueSupplier.get();
            if (value == null) { return;}
            entry.set(toLongArray(value), timestamp);
        };

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(toLongArray(value));
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, long[] value, LogLevel level) {


        if (value == null) {return;}

        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getIntegerArrayTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((IntegerArrayTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setIntegerArray(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addLongArray(String entryName, Supplier<long[]> valueSupplier, LogLevel level) {
        var topic = table.getIntegerArrayTopic(entryName);

        var entry = topic.getEntry(new long[] {});

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private long[] lastValue = new long[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.set(value, timestamp);
                }
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, float[] value, LogLevel level) {


        if (value == null) {return;}

        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getFloatArrayTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((FloatArrayTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setFloatArray(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addFloatArray(String entryName, Supplier<float[]> valueSupplier, LogLevel level) {
        var topic = table.getFloatArrayTopic(entryName);

        var entry = topic.getEntry(new float[] {});

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private float[] lastValue = new float[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.set(value, timestamp);
                }
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, double[] value, LogLevel level) {


        if (value == null) {return;}

        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getDoubleArrayTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((DoubleArrayTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setDoubleArray(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addDoubleArray(String entryName, Supplier<double[]> valueSupplier, LogLevel level) {
        var topic = table.getDoubleArrayTopic(entryName);

        var entry = topic.getEntry(new double[] {});

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private double[] lastValue = new double[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return;}

                    if (!(Arrays.equals(value, lastValue))) {

                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.set(value, timestamp);
                }
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }

    @Override
    public void put(String entryName, String[] value, LogLevel level) {


        if (value == null) {return;}

        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.put(entryName, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName)) {
                NetworkTablesJNI.unpublish(
                    published.remove(entryName)
                );
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getStringArrayTopic(entryName);

            var publisher = topic.publish();

            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {

            published.put(entryName, ((StringArrayTopic) topics.get(entryName)).publish().getHandle());

        }

        NetworkTablesJNI.setStringArray(published.get(entryName), WPIUtilJNI.now(), value);

    }

    @Override
    public void addStringArray(String entryName, Supplier<String[]> valueSupplier, LogLevel level) {
        var topic = table.getStringArrayTopic(entryName);

        var entry = topic.getEntry(new String[] {});

        LongConsumer consumer;

        if (this.isLazy()) {
            consumer = new LongConsumer() {
                private String[] lastValue = new String[] {};
                @Override
                public void accept(long timestamp) {
                    var value = valueSupplier.get();
                    if (value == null) { return;}

                    if (!(Arrays.deepEquals(value, lastValue))) {

                        entry.set(value, timestamp);
                        lastValue = value;
                    }
                }
            };
        } else {
            consumer = (timestamp) -> {
                var value = valueSupplier.get();
                if (value != null) {
                    entry.set(value, timestamp);
                }
            };
        }

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                entry.set(value);
            },
            entry::unpublish
        );

    }


    @Override
    public <R> void addStruct(String entryName, Struct<R> struct, Supplier<? extends R> valueSupplier, LogLevel level) {
        var topic = table.getStructTopic(entryName, struct);
        var publisher = topic.publish();

        LongConsumer consumer;
        consumer = (timestamp) -> {
            var value = valueSupplier.get();
            if (value == null) { return;}
            publisher.set(value, timestamp);
        };

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) { return;}
                publisher.set(value);
            },
            ()->NetworkTablesJNI.unpublish(publisher.getHandle())
        );
    }

    @Override
    public <R> void addStructArray(String entryName, Struct<R> struct, Supplier<R[]> valueSupplier, LogLevel level) {
        var topic = table.getStructArrayTopic(entryName, struct);
        var publisher = topic.publish();

        LongConsumer consumer;

        consumer = (timestamp) -> {
            var value = valueSupplier.get();
            if (value == null) {return;}
            publisher.set(value, timestamp);
        };

        addField(
            entryName,
            level,
            consumer,
            () -> {
                var value = valueSupplier.get();
                if (value == null) {return;}
                publisher.set(value);
            },
            ()->NetworkTablesJNI.unpublish(publisher.getHandle())
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> void putStruct(String entryName, Struct<R> struct, R value, LogLevel level) {
        if (value == null) {return;}
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName) && structEntrys.containsKey(entryName)) {
                ((StructEntry<R>) structEntrys.remove(entryName)).unpublish();
                NetworkTablesJNI.unpublish(published.remove(entryName));
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.putStruct(entryName, struct, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName) && structEntrys.containsKey(entryName)) {
                ((StructEntry<R>) structEntrys.remove(entryName)).unpublish();
                NetworkTablesJNI.unpublish(published.remove(entryName));
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getStructTopic(entryName, struct);
            var publisher = topic.publish();
            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {
            published.put(entryName, ((StructTopic<R>) topics.get(entryName)).publish().getHandle());
        }

        if (!structEntrys.containsKey(entryName)) {
            var entry = ((StructTopic<R>) topics.get(entryName)).getEntry(null);
            entry.set(value);
            structEntrys.put(entryName, entry);
        } else {
            ((StructEntry<R>) structEntrys.get(entryName)).set(value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> void putStructArray(String entryName, Struct<R> struct, R[] value, LogLevel level) {
        if (value == null) {return;}
        if (level == LogLevel.NOT_FILE_ONLY && Monologue.isFileOnly()) {
            if (published.containsKey(entryName) && topics.containsKey(entryName) && structArrayEntrys.containsKey(entryName)) {
                ((StructArrayEntry<R>) structArrayEntrys.remove(entryName)).unpublish();
                NetworkTablesJNI.unpublish(published.remove(entryName));
            }
            return;
        } else if (level == LogLevel.DEFAULT && Monologue.isFileOnly()) {
            Monologue.dataLogger.putStructArray(entryName, struct, value, level);
            if (published.containsKey(entryName) && topics.containsKey(entryName) && structArrayEntrys.containsKey(entryName)) {
                ((StructArrayEntry<R>) structArrayEntrys.remove(entryName)).unpublish();
                NetworkTablesJNI.unpublish(published.remove(entryName));
            }
            return;
        }

        if (!topics.containsKey(entryName)) {
            var topic = table.getStructArrayTopic(entryName, struct);
            var publisher = topic.publish();
            published.put(entryName, publisher.getHandle());
            topics.put(entryName, topic);
        }

        if (!published.containsKey(entryName)) {
            published.put(entryName, ((StructArrayTopic<R>) topics.get(entryName)).publish().getHandle());
        }

        if (!structArrayEntrys.containsKey(entryName)) {
            var entry = ((StructArrayTopic<R>) topics.get(entryName)).getEntry(null);
            entry.set(value);
            structArrayEntrys.put(entryName, entry);
        } else {
            ((StructArrayEntry<R>) structArrayEntrys.get(entryName)).set(value);
        }
    }

    @Override
    public void addSendable(String path, Sendable sendable) {   
        if (sendable == null) {return;}     
        var builder = new SendableBuilderImpl();
        builder.setTable(table.getTable(path));
        sendable.initSendable(builder);
        builder.startListeners();
        table.getTable(path).getEntry(".controllable").setBoolean(false);
        sendables.add(builder);
        NetworkTablesJNI.startEntryDataLog(NetworkTableInstance.getDefault().getHandle(), DataLogManager.getLog(), path, "NT:" + path);
    }

    @Override
    public boolean isNT() {
        return true;
    }
}