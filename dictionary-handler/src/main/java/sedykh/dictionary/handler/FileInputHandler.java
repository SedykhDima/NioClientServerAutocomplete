package sedykh.dictionary.handler;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import sedykh.dictionary.PopularityOfWord;

/**
 * Работает с данными из входного потока файла
 *
 * @author Седых Д.
 */
public class FileInputHandler extends HandlerOfExternalData {
    /**
     * Создает экземпляр класса и заполняет словарь
     *
     * @param reader входной поток
     */
    public FileInputHandler(Reader reader) throws IOException {
        super(reader);
        initDictionary();
    }

    @Override
    protected void initDictionary() throws IOException {
        dictionary = new ArrayList<>();
        while (reader.ready()) {
            dictionary.add(new PopularityOfWord(reader.readLine()));
        }
        reader.close();
    }
}
