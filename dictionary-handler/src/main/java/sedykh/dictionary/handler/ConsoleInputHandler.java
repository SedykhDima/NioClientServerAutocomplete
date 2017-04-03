package sedykh.dictionary.handler;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import sedykh.dictionary.PopularityOfWord;

/**
 * Работает с данными из входного потока консоли
 *
 * @author Седых Д.
 */
public class ConsoleInputHandler extends HandlerOfExternalData {

    /**
     * Создает экземпляр класса и заполняет словарь и массив префиксов данными из входного потока
     *
     * @param reader входной поток
     */
    public ConsoleInputHandler(Reader reader) throws IOException {
        super(reader);
        initDictionary();
        initPrefixArray();
        reader.close();
    }

    @Override
    protected void initDictionary() throws IOException {
        int countWordsInDictionary = getCountWordsInDictionary();
        dictionary = new ArrayList<>(countWordsInDictionary);

        for (int i = 0; i < countWordsInDictionary; i++) {
            dictionary.add(new PopularityOfWord(reader.readLine()));
        }
    }
}
