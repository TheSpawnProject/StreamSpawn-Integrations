package automated;

import io.socket.client.IO;
import io.socket.client.Socket;
import net.programmer.igoodie.streamspawn_integrations.event.IncomingEvent;
import net.programmer.igoodie.streamspawn_integrations.integration.StreamlabsIntegration;
import org.json.JSONObject;

import java.util.Scanner;

public class StreamlabsTest {

    private static final Scanner scanner = new Scanner(System.in);

    public static class MyStreamlabsIntegration extends StreamlabsIntegration {

        @Override
        protected IO.Options generateOptions() {
            IO.Options options = super.generateOptions();
            System.out.println("Enter your Streamlabs > Socket API token:");
            String token = scanner.nextLine().trim();
            options.query = "token=" + token;
            return options;
        }

        @Override
        protected void onConnect(Socket socket, Object... args) {
            super.onConnect(socket, args);
            System.out.println("Connected");
        }

        @Override
        protected void onDisconnect(Socket socket, Object... args) {
            super.onDisconnect(socket, args);
            System.out.println("Disconnected");
        }

        @Override
        protected void onRawEvent(JSONObject rawEvent) {
            System.out.println(rawEvent);
        }

        @Override
        protected void onUnknownRawEvent(JSONObject rawEvent) {
            System.out.println("Unknown: " + rawEvent);
        }

        @Override
        protected void onEvent(IncomingEvent event) {
            System.out.println(event);
        }

    }

    public static void main(String[] args) {
        StreamlabsIntegration integration = new MyStreamlabsIntegration();
        integration.initialize();
        integration.start();
    }

}
