package sedykh.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Представляет нио клиента, который посылает запрос и получает ответ от нио сервера
 */
public class NioClient {

    private int port;
    private String address;
    private StringBuilder result = new StringBuilder();
    private ByteBuffer buffer;
    private ByteBuffer length = ByteBuffer.allocate(6);
    private boolean isClose = false;
    private boolean isFirstMessage = true;
    private int bufferSize;

    /**
     * Инициализирует клиента
     *
     * @param port    порт
     * @param address хост
     */
    public NioClient(int port, String address) {
        this.port = port;
        this.address = address;
    }

    /**
     * Инициализирует взаимодействие с сервером
     */
    public void start() {
        try {
            String command = getCommand();
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_CONNECT);
            channel.connect(new InetSocketAddress(address, port));

            while (!isClose) {
                selector.select();

                for (SelectionKey selectionKey : selector.selectedKeys()) {
                    if (selectionKey.isConnectable()) {
                        connect(channel, selectionKey);
                    } else if (selectionKey.isReadable()) {
                        read(channel);
                    } else if (selectionKey.isWritable()) {
                        write(channel, command, selectionKey);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Инициализирует соединение с сервером
     */
    private void connect(SocketChannel channel, SelectionKey selectionKey) throws IOException {
        channel.finishConnect();
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    /**
     * Отправляет на сервер команду, считаную с консоли
     */
    private void write(SocketChannel channel, String command, SelectionKey selectionKey)
            throws IOException {
        if (command != null) {
            channel.write(ByteBuffer.wrap(command.getBytes()));
        }
        selectionKey.interestOps(SelectionKey.OP_READ);
    }

    /**
     * Выводит ответ на консоль и завершает работу клиента
     */
    private void read(SocketChannel channel) throws IOException {
        if (isFirstMessage) {
            channel.read(length);
            bufferSize = getBufferSize();
            buffer = ByteBuffer.allocate(bufferSize);
            isFirstMessage = false;
        } else {
            channel.read(buffer);
        }
        if (buffer.position() >= bufferSize) {
            result.append(new String(buffer.array()));
            System.out.println(result.toString());
            channel.close();
            close();
        }
    }

    private int getBufferSize() {
        return Integer.parseInt(new String(length.array()));
    }

    private void close() {
        isClose = true;
    }

    private void isCommandValid(String command) {
        if (command.startsWith("get") && command.length() > 3) {

        } else {
            throw new IllegalArgumentException("Request " + command + " not supported, please change request");
        }
    }

    /**
     * Читает с консоли команду
     *
     * @return считаную команду
     */
    private String getCommand() {
        String result = "";
        Scanner scanner = new Scanner(System.in);
        result = scanner.nextLine();
        isCommandValid(result);
        scanner.close();

        return result.length() + 1 + " " + result.substring(0, 3) + " " + result.substring(3, result.length());
    }
}
