package bearmaps.proj2d;

import org.eclipse.jetty.util.Trie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** @source https://algs4.cs.princeton.edu/52trie/TrieST.java.html
 * used as reference
 */
public class TrieSet {

    private Node root;

    public TrieSet() {
        root = new Node(false);
    }

    private class Node {
        boolean isKey;
        HashMap<Character, Node> chars;

        Node(boolean isKey) {
            this.isKey = isKey;
            chars = new HashMap<>();
        }
    }

    // add the string to the trie set
    public void add(String key) {
        if (key == null) {
            return;
        }
        Node current = root;
        int length = key.length();
        for (int i = 0; i < length; i++) {
            char c = key.charAt(i);
            if (!current.chars.containsKey(c)) {
                current.chars.put(c, new Node(false));
            }
            current = current.chars.get(c);
        }
        current.isKey = true;
    }


    // return a list of all strings with the prefix
    public List<String> keysWithPrefix(String prefix) {
        Node key = getKey(prefix);
        List<String> strings = new ArrayList<>();
        getStrings(key, prefix, strings);
        return strings;
    }


    // returns node with last letter in prefix
    public Node getKey(String prefix) {
        Node node = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (node.chars.containsKey(prefix.charAt(i))) {
                node = node.chars.get(prefix.charAt(i));
            } else {
                break;
            }
        }
        return node;
    }

    // get all strings with the prefix
    public void getStrings(Node key, String string, List<String> strings) {
        if (key.isKey) {
            strings.add(string);
        }

        for (Character c : key.chars.keySet()) {
            getStrings(key.chars.get(c), string + c, strings);
        }
    }
}
