package net.programmer.igoodie.event;

import net.programmer.igoodie.runtime.event.TSLEvent;

public class KnownEventArgs {

    private KnownEventArgs() {}

    public static TSLEvent.Property<String> SOURCE = TSLEvent.PropertyBuilder.STRING.create("source");

    public static TSLEvent.Property<String> ACTOR = TSLEvent.PropertyBuilder.STRING.create("actor");
    public static TSLEvent.Property<String> ACTOR_ID = TSLEvent.PropertyBuilder.STRING.create("actor_id");

    public static TSLEvent.Property<String> MESSAGE = TSLEvent.PropertyBuilder.STRING.create("message");
    public static TSLEvent.Property<Double> AMOUNT = TSLEvent.PropertyBuilder.DOUBLE.create("amount");
    public static TSLEvent.Property<String> CURRENCY = TSLEvent.PropertyBuilder.STRING.create("currency");

    public static TSLEvent.Property<Integer> MONTHS = TSLEvent.PropertyBuilder.INT.create("months");
    public static TSLEvent.Property<Integer> SUBSCRIPTION_TIER = TSLEvent.PropertyBuilder.INT.create("subscription_tier");

    public static TSLEvent.Property<Integer> RAIDER_COUNT = TSLEvent.PropertyBuilder.INT.create("raider_count");
    public static TSLEvent.Property<Integer> VIEWER_COUNT = TSLEvent.PropertyBuilder.INT.create("viewer_count");

    public static TSLEvent.Property<String> GIFTER = TSLEvent.PropertyBuilder.STRING.create("gifter");
    public static TSLEvent.Property<String> GIFTER_ID = TSLEvent.PropertyBuilder.STRING.create("gifter_id");
    public static TSLEvent.Property<Boolean> IS_GIFTED = TSLEvent.PropertyBuilder.BOOLEAN.create("gifted");

    public static TSLEvent.Property<String> TITLE = TSLEvent.PropertyBuilder.STRING.create("title");

}
