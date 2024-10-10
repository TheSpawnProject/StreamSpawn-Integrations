package net.programmer.igoodie.integration.base;

import net.programmer.igoodie.event.IncomingEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class EventPublisher<R> {

    protected List<RawEventListener<R>> rawEventListeners;
    protected List<RawEventListener<R>> unknownEventListeners;
    protected List<EventListener> eventListeners;

    public EventPublisher() {
        this.rawEventListeners = new ArrayList<>();
        this.unknownEventListeners = new ArrayList<>();
        this.eventListeners = new ArrayList<>();
    }

    public abstract void initialize();

    public abstract void start();

    public abstract void stop();

    public void addRawEventListener(RawEventListener<R> listener) {
        this.rawEventListeners.add(listener);
    }

    public void addUnknownEventListener(RawEventListener<R> listener) {
        this.unknownEventListeners.add(listener);
    }

    public void addEventListener(EventListener listener) {
        this.eventListeners.add(listener);
    }

    protected void pushRawEvent(R rawEvent) {
        rawEventListeners.forEach(listener -> listener.handleRawEvent(rawEvent));
    }

    protected void pushUnknownEvent(R rawEvent) {
        unknownEventListeners.forEach(listener -> listener.handleRawEvent(rawEvent));
    }

    protected void pushEvent(IncomingEvent event) {
        eventListeners.forEach(listener -> listener.handleEvent(event));
    }

    @FunctionalInterface
    public interface RawEventListener<R> {
        void handleRawEvent(R eventBody);
    }

    @FunctionalInterface
    public interface EventListener {
        void handleEvent(IncomingEvent event);
    }

}
