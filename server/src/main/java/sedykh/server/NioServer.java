package sedykh.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Отвечает на запросы клиентов
 *
 * @author Седых Д.
 */
public class NioServer {

    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(32);
    private MessageProcessor messageProcessor;
    private final List<ChangeRequest> changeRequests = new LinkedList<>();
    private final Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<>();
    static final int CHANGEOPS = 2;

    /**
     * Запускает сервер по указанному адресу
     *
     * @param port     порт
     * @param pathFile имя файла для инициализации словаря
     */
    public NioServer(int port, String pathFile) throws IOException {
        messageProcessor = new MessageProcessor(pathFile);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));
        selector = SelectorProvider.provider().openSelector();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        new Thread(messageProcessor).start();
    }

    public void start() {
        try {
            while (true) {
                /*Ждем пока обработчик сообщений отправит сигнал о готовности данных и разбудит селектор*/
                synchronized (changeRequests) {
                    for (ChangeRequest change : changeRequests) {
                        switch (change.getType()) {
                            case CHANGEOPS:
                                SelectionKey key = change.getChannel().keyFor(selector);
                                key.interestOps(change.getOps());
                                break;
                            default:
                        }
                    }
                    changeRequests.clear();
                }
                selector.select();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();
                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    } else if (key.isConnectable()) {
                        key.channel().close();
                    }
                }
            }
        } catch (IOException e) {
            Logger.getLogger(NioServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        buffer.clear();
        try {
            socketChannel.read(buffer);
        } catch (IOException e) {
            socketChannel.close();
        }
        messageProcessor.processData(this, socketChannel, buffer.array());
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        synchronized (pendingData) {
            List<ByteBuffer> queue = pendingData.get(socketChannel);
            while (!queue.isEmpty()) {
                ByteBuffer buf = queue.get(0);
                socketChannel.write(buf);
                if (buf.remaining() > 0) {
                    break;
                }
                queue.remove(0);
            }
            if (queue.isEmpty()) {
                key.interestOps(SelectionKey.OP_CONNECT);
            }
        }
    }

    /**
     * Отправляет серверу сигнал, что данные клиента готовы для записи
     *
     * @param channel канал клиента
     * @param data    массив байт представляющих слова для автодополнения
     */
    public void send(SocketChannel channel, byte[] data) {
        synchronized (changeRequests) {
            changeRequests.add(new ChangeRequest(channel, CHANGEOPS, SelectionKey.OP_WRITE));
            synchronized (pendingData) {
                List<ByteBuffer> queue = pendingData.get(channel);
                if (queue == null) {
                    queue = new ArrayList<>();
                    pendingData.put(channel, queue);
                }
                queue.add(ByteBuffer.wrap(data));
            }
        }
        selector.wakeup();
    }
}
