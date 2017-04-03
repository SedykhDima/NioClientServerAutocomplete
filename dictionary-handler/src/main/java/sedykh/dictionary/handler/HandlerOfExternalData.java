package sedykh.dictionary.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import sedykh.dictionary.PopularityOfWord;

/**
 * Работает с данными из входного потока
 *
 * @author Седых Д.
 */
public abstract class HandlerOfExternalData {
    protected ArrayList<PopularityOfWord> dictionary;
    protected String[] prefixArray;
    protected BufferedReader reader;

    /**
     * Создает экземпляр класса и заполняет словарь и массив префиксов данными из входного потока
     *
     * @param reader входной поток
     */
    public HandlerOfExternalData(Reader reader) throws IOException {
        this.reader = new BufferedReader(reader);
    }

    /**
     * Читает данные из входного потока и заполняет ими словарь
     */
    protected abstract void initDictionary() throws IOException;

    /**
     * Читает данные из входного потока и заполняет ими массив префиксов
     */
    protected void initPrefixArray() throws IOException {
        int countPrefix = getCountPrefix();
        prefixArray = new String[countPrefix];

        for (int i = 0; i < countPrefix; i++) {
            prefixArray[i] = reader.readLine();
        }
    }

    protected int getCountWordsInDictionary() throws IOException {
        int result = Integer.parseInt(reader.readLine());

        if (result > 100_000 || result < 1) {
            throw new IllegalArgumentException(
                    "Number countWordsInDictionary doesn't satisfy the conditions");
        }
        return result;
    }

    protected int getCountPrefix() throws IOException {
        int result = Integer.parseInt(reader.readLine());

        if (result > 15_000 || result < 1) {
            throw new IllegalArgumentException("Number countPrefix doesn't satisfy the conditions");
        }
        return result;
    }

    public ArrayList<PopularityOfWord> getDictionary() {
        return dictionary;
    }

    public String[] getPrefixArray() {
        return prefixArray;
    }
}
