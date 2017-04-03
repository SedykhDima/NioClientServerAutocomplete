package sedykh.dictionary.factory;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import sedykh.dictionary.handler.ConsoleInputHandler;
import sedykh.dictionary.handler.FileInputHandler;
import sedykh.dictionary.handler.HandlerOfExternalData;

/**
 * Создает объект для работы с данными из входного потока
 *
 * @author Седых Д.
 */
public class HandlerOfExternalDataFactory {

    private HandlerOfExternalDataFactory() {
    }

    public static HandlerOfExternalData getInstance(Reader reader) throws IOException {
        if (reader instanceof FileReader) {
            return new FileInputHandler(reader);
        } else if (reader instanceof InputStreamReader) {
            return new ConsoleInputHandler(reader);
        }
        return null;
    }
}
