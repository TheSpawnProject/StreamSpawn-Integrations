package automated;

import io.socket.client.IO;
import net.programmer.igoodie.integration.StreamlabsIntegration;

import java.util.Scanner;

public class StreamlabsTest {

    private static final Scanner scanner = new Scanner(System.in);

    public static class MyStreamlabsIntegration extends StreamlabsIntegration {

        @Override
        protected IO.Options generateOptions() {
            IO.Options options = super.generateOptions();
            String token = scanner.nextLine().trim();
            options.query = "token=" + token;
            return options;
        }

    }

    public static void main(String[] args) {
        StreamlabsIntegration integration = new MyStreamlabsIntegration();

//        integration.addOptionInterceptor(options -> {
//            System.out.println("Enter your Streamlabs > Socket API token");
//            String token = scanner.nextLine();
//            options.query = "token=" + token;
//        });

        integration.addRawEventListener(System.out::println);
        integration.addUnknownEventListener(event -> System.out.println("Unknown: " + event));
        integration.addEventListener(System.out::println);

        integration.initialize();
        integration.start();
    }

}
