package sedykh.client;

public class Main {
    public static void main(String[] args) throws Exception {
        InetAddressParser addressParser = new InetAddressParser(args);
        NioClient nioClient = new NioClient(addressParser.getPort(), addressParser.getHost());
        nioClient.start();
    }
}
