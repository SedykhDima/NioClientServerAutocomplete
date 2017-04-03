package sedykh.dictionary;

import java.io.IOException;
import java.io.InputStreamReader;

import sedykh.dictionary.factory.HandlerOfExternalDataFactory;
import sedykh.dictionary.handler.HandlerOfExternalData;
import sedykh.dictionary.trie.Trie;

public class Main {
    public static void main(String[] args) throws IOException {
        HandlerOfExternalData handlerOfExternalData =
                HandlerOfExternalDataFactory.getInstance(new InputStreamReader(System.in));
        Trie trie = new Trie();
        trie.fillTrie(handlerOfExternalData.getDictionary());
        System.out.println(trie.getResultFor(handlerOfExternalData.getPrefixArray()));
    }
}
