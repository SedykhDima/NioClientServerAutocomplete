package sedykh.dictionary;

/**
 * Служит для хранения слова и популярности этого слова в словаре
 *
 * @author Седых Д.
 */
public class PopularityOfWord implements Comparable<PopularityOfWord> {
    private String word;
    private int countOfRepeat;

    /**
     * Делит входную строку на слово и популярность этого слова
     *
     * @param inputString в формате: "слово 13"
     * @throws IllegalArgumentException при неверных входных условиях
     */
    public PopularityOfWord(String inputString) throws IllegalArgumentException {
        String[] dividedInputString = inputString.split(" ");
        word = dividedInputString[0];
        countOfRepeat = Integer.parseInt(dividedInputString[1]);

        if (word.length() > 15 || word.isEmpty()) {
            throw new IllegalArgumentException("Entered word doesn't satisfy the conditions");
        }

        if (countOfRepeat > 1_000_000 || countOfRepeat < 1) {
            throw new IllegalArgumentException(
                    "Entered count of repeat doesn't satisfy the conditions");
        }
    }

    public String getWord() {
        return word;
    }

    public int getCountOfRepeat() {
        return countOfRepeat;
    }

    @Override
    public int compareTo(PopularityOfWord other) {
        if (getCountOfRepeat() == other.getCountOfRepeat()) {
            return getWord().compareTo(other.getWord());
        }
        return getCountOfRepeat() > other.getCountOfRepeat() ? -1 : 1;
    }
}
