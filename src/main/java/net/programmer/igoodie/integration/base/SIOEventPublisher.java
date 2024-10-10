package net.programmer.igoodie.integration.base;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class SIOEventPublisher extends EventPublisher<JSONObject> {

    protected URI url;
    protected Socket socket;

    protected List<OptionInterceptor> optionInterceptors;

    public SIOEventPublisher(String url) {
        this.url = URI.create(url);
        this.optionInterceptors = new ArrayList<>();
    }

    public Socket getSocket() {
        return socket;
    }

    public void addOptionInterceptor(OptionInterceptor interceptor) {
        this.optionInterceptors.add(interceptor);
    }

    protected IO.Options generateOptions() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.transports = new String[]{"websocket"};
        optionInterceptors.forEach(interceptor -> interceptor.intercept(options));
        return options;
    }

    @Override
    public void initialize() {
        IO.Options options = generateOptions();
        this.socket = IO.socket(url, options);
        this.bindEvents();
    }

    protected void bindEvents() {
        this.socket.on(Socket.EVENT_CONNECT, args -> onConnect(socket, args));
        this.socket.on(Socket.EVENT_DISCONNECT, args -> onDisconnect(socket, args));
    }

    @Override
    public void start() {
        this.socket.connect();
    }

    @Override
    public void stop() {
        this.socket.disconnect();
    }

    protected abstract void onConnect(Socket socket, Object... args);

    protected abstract void onDisconnect(Socket socket, Object... args);

    @FunctionalInterface
    public interface OptionInterceptor {
        void intercept(IO.Options options);
    }

}
