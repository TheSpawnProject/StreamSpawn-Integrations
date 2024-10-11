package net.programmer.igoodie.streamspawn_integrations.integration.base;

import net.programmer.igoodie.streamspawn_integrations.event.IncomingEvent;

public abstract class EventGenerator<R> {

    public EventGenerator() {}

    public abstract void initialize();

    public abstract void start();

    public abstract void stop();

    protected void onRawEvent(R rawEvent) {}

    protected void onUnknownRawEvent(R rawEvent) {}

    protected void onEvent(IncomingEvent event) {}

}
