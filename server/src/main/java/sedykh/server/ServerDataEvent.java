package sedykh.server;

import java.nio.channels.SocketChannel;

/**
 * Хранит данные для ответа клиенту, подключенному к серверу
 */
public class ServerDataEvent {
    private NioServer server;
    private SocketChannel channel;
    private byte[] data;

    /**
     * Создает объект хранящий данные для клиента
     *
     * @param server  сервер, к которому подключен клиент
     * @param channel канал клиента
     * @param data    данные
     */
    public ServerDataEvent(NioServer server, SocketChannel channel, byte[] data) {
        this.server = server;
        this.channel = channel;
        this.data = data;
    }

    public NioServer getServer() {
        return server;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public byte[] getData() {
        return data;
    }
}
