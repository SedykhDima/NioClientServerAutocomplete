package sedykh.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer(Integer.parseInt(args[1]), args[0]);
        nioServer.start();
    }
}
