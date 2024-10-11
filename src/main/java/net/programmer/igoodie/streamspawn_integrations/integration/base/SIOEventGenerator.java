package net.programmer.igoodie.streamspawn_integrations.integration.base;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONObject;

import java.net.URI;

public abstract class SIOEventGenerator extends EventGenerator<JSONObject> {

    protected URI url;
    protected Socket socket;

    public SIOEventGenerator(String url) {
        this.url = URI.create(url);
    }

    public Socket getSocket() {
        return socket;
    }

    protected IO.Options generateOptions() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.transports = new String[]{"websocket"};
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

}
