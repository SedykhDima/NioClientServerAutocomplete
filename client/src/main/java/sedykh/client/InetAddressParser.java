package sedykh.client;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Предоставляет удобную работу с сетевым адресом
 */
public class InetAddressParser {
    private int port;
    private String host;

    /**
     * Разбивает массив входных строк на хост и порт
     *
     * @param inputStrings строки содержащие хост и порт
     */
    public InetAddressParser(String[] inputStrings) {
        if (inputStrings.length == 1) {
            StringTokenizer stringTokenizer = new StringTokenizer(inputStrings[0], " :");
            host = stringTokenizer.nextToken();
            port = Integer.parseInt(stringTokenizer.nextToken());
        } else {
            Pattern pattern = Pattern.compile("\\D+");
            Matcher matcher = pattern.matcher(inputStrings[0]);
            if (matcher.find()) {
                host = inputStrings[0];
                port = Integer.parseInt(inputStrings[1]);
            } else {
                host = inputStrings[1];
                port = Integer.parseInt(inputStrings[0]);
            }
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
