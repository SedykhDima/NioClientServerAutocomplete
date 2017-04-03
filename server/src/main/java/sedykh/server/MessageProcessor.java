package sedykh.server;

import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import sedykh.dictionary.factory.HandlerOfExternalDataFactory;
import sedykh.dictionary.handler.HandlerOfExternalData;
import sedykh.dictionary.trie.Trie;


/**
 * Обрабатывает запросы от клиентов
 *
 * @author Седых Д.
 */
public class MessageProcessor implements Runnable {
    private final List<ServerDataEvent> queue = new LinkedList<>();
    private Trie trie = new Trie();

    public MessageProcessor(String pathFile) {
        try {
            HandlerOfExternalData handlerOfExternalData =
                    HandlerOfExternalDataFactory.getInstance(new FileReader(pathFile));
            trie.fillTrie(handlerOfExternalData.getDictionary());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Формирует ответ для клиента
     *
     * @param server  исходный сервер
     * @param channel канал клиента
     * @param data    слова для автодополнения в виде байтового массива
     */
    public void processData(NioServer server, SocketChannel channel, byte[] data) {
        StringBuilder answer = trie.getResultFor(getCommand(data));
        answer.insert(0, String.format("%06d", answer.length() + 1) + "\n");
        answer.trimToSize();
        synchronized (queue) {
            queue.add(new ServerDataEvent(server, channel, answer.toString().getBytes()));
            queue.notify();
        }
    }

    private String getCommand(byte[] array) {
        String[] commandArray = new String(array).split(" ");
        int length = Integer.parseInt(commandArray[0]) - 4;
        return commandArray[2].substring(0, length);
    }

    @Override
    public void run() {
        ServerDataEvent dataEvent;
        while (true) {
            /*Ждет пока обработчик положит данные в очередь, по готовности отправляет сигнал серверу*/
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                dataEvent = queue.remove(0);
            }
            dataEvent.getServer().send(dataEvent.getChannel(), dataEvent.getData());
        }
    }
}
