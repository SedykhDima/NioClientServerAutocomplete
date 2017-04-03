package sedykh.server;

import java.nio.channels.SocketChannel;

/**
 * Регистрирует смену действия селектора для клиента
 */
public class ChangeRequest {

    private SocketChannel channel;
    private int type;
    private int ops;

    /**
     * Инициализирует объект смены действия для клиента.
     *
     * @param channel канал клиента
     * @param type    для чего меняем действия
     * @param ops     на какие действия меняем
     */
    public ChangeRequest(SocketChannel channel, int type, int ops) {
        this.channel = channel;
        this.type = type;
        this.ops = ops;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public int getType() {
        return type;
    }

    public int getOps() {
        return ops;
    }
}
