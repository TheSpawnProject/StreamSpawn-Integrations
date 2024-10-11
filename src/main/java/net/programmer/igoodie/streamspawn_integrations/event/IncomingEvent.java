package net.programmer.igoodie.streamspawn_integrations.event;

import net.programmer.igoodie.goodies.runtime.GoodieObject;

public class IncomingEvent {

    protected final String eventName;
    protected final GoodieObject eventArgs;

    public IncomingEvent(String eventName) {
        this.eventName = eventName;
        this.eventArgs = new GoodieObject();
    }

    public String getEventName() {
        return eventName;
    }

    public GoodieObject getEventArgs() {
        return eventArgs;
    }

    @Override
    public String toString() {
        return "IncomingEvent{" +
                "eventName='" + eventName + '\'' +
                ", eventArgs=" + eventArgs +
                '}';
    }

}
