package sedykh.dictionary.trie;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import sedykh.dictionary.PopularityOfWord;

/**
 * Представляет префиксное дерево для хранения словаря автодополнения
 *
 * @author Седых Д.
 */
public class Trie {
    TrieNode root = new TrieNode();

    /**
     * Создает узел дерева, содержащий узлы потомков и словарь автодополнения
     */
    private class TrieNode {
        private Map<Character, TrieNode> children = new TreeMap<>();
        private PriorityQueue<PopularityOfWord> dictionary = new PriorityQueue<>();

        /**
         * Возвращает словарь автодополнения, с упорядочеными по убыванию словами и их
         * популярностью
         */
        public PriorityQueue<PopularityOfWord> getDictionary() {
            return dictionary;
        }

        public void setDictionary(PriorityQueue<PopularityOfWord> dictionary) {
            this.dictionary = dictionary;
        }
    }

    /**
     * Заполняет узлы дерева, при необходимости создает новые
     *
     * @param popularityOfWord слово для заполнения узлов
     */
    private void put(PopularityOfWord popularityOfWord) {
        TrieNode curNode = root;

        for (char key : popularityOfWord.getWord().toCharArray()) {
            if (!curNode.children.containsKey(key)) {
                curNode.children.put(key, new TrieNode());
            }
            curNode = curNode.children.get(key);
            curNode.dictionary.add(popularityOfWord);
        }
    }

    /**
     * Находит необходимый узел дерева
     *
     * @param prefix префикс для нахождения узла
     * @return TrieNode узел дерева
     */
    private TrieNode find(String prefix) {
        TrieNode curNode = root;

        for (char key : prefix.toCharArray()) {
            TrieNode child = curNode.children.get(key);
            if (child == null) {
                return null;
            }
            curNode = child;
        }

        return curNode;
    }

    /**
     * Возвращает результаты автодополнения для префиксов, отсортированных в порядке убывания по
     * популярности.
     *
     * @param prefixArray массив префиксов или единичный префикс
     * @return не более 10 автодополнений для каждого префикса
     */
    public StringBuilder getResultFor(String... prefixArray) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < prefixArray.length; i++) {
            TrieNode curNode = find(prefixArray[i]);

            if (curNode == null || curNode.getDictionary().isEmpty()) {
                continue;
            }

            int size = getSize(curNode);

            PriorityQueue<PopularityOfWord> cacheQueue = new PriorityQueue<>();
            for (int k = 0; k < size; k++) {
                PopularityOfWord curPopularityOfWord = curNode.getDictionary().poll();
                result.append(curPopularityOfWord.getWord() + "\n");
                cacheQueue.add(curPopularityOfWord);
            }
            curNode.setDictionary(cacheQueue);

            result.append("\n");
        }
        return result;
    }

    /**
     * Заполняет префиксное дерево словами из словаря
     *
     * @param popularityOfWords словарь
     */
    public void fillTrie(ArrayList<PopularityOfWord> popularityOfWords) {
        for (int i = 0; i < popularityOfWords.size(); i++) {
            put(popularityOfWords.get(i));
        }
    }

    private int getSize(Trie.TrieNode curNode) {
        return curNode.getDictionary().size() > 10 ? 10 : curNode.getDictionary().size();
    }
}
