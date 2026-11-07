package by.it.group551004.dovgash.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;
    private String lastPutResult; // для возврата старого значения при put
    private String lastRemoveResult;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.right) - height(n.left);
    }

    private void fixHeight(Node n) {
        int hl = height(n.left);
        int hr = height(n.right);
        n.height = (hl > hr ? hl : hr) + 1;
    }

    private Node rotateRight(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;
        fixHeight(p);
        fixHeight(q);
        return q;
    }

    private Node rotateLeft(Node q) {
        Node p = q.right;
        q.right = p.left;
        p.left = q;
        fixHeight(q);
        fixHeight(p);
        return p;
    }

    private Node balance(Node p) {
        fixHeight(p);
        if (balanceFactor(p) == 2) {
            if (balanceFactor(p.right) < 0) {
                p.right = rotateRight(p.right);
            }
            return rotateLeft(p);
        }
        if (balanceFactor(p) == -2) {
            if (balanceFactor(p.left) > 0) {
                p.left = rotateLeft(p.left);
            }
            return rotateRight(p);
        }
        return p;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null || (key instanceof Integer && containsKeyNull((Integer) key));
    }

    private boolean containsKeyNull(Integer key) {
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return true;
        }
        return false;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node curr = root;
        while (curr != null) {
            int cmp = k.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return curr.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException();
        lastPutResult = null;
        root = putRec(root, key, value);
        return lastPutResult;
    }

    private Node putRec(Node p, Integer key, String value) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = putRec(p.left, key, value);
        } else if (cmp > 0) {
            p.right = putRec(p.right, key, value);
        } else {
            lastPutResult = p.value;
            p.value = value;
            return p;
        }
        return balance(p);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        lastRemoveResult = null;
        root = removeRec(root, (Integer) key);
        return lastRemoveResult;
    }

    private Node removeRec(Node p, Integer key) {
        if (p == null) return null;
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = removeRec(p.left, key);
        } else if (cmp > 0) {
            p.right = removeRec(p.right, key);
        } else {
            lastRemoveResult = p.value;
            size--;
            Node l = p.left;
            Node r = p.right;
            if (r == null) return l;
            Node min = findMin(r);
            min.right = removeMin(r);
            min.left = l;
            return balance(min);
        }
        return balance(p);
    }

    private Node findMin(Node p) {
        return p.left == null ? p : findMin(p.left);
    }

    private Node removeMin(Node p) {
        if (p.left == null) return p.right;
        p.left = removeMin(p.left);
        return balance(p);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void buildString(Node n, StringBuilder sb) {
        if (n == null) return;
        buildString(n.left, sb);
        sb.append(n.key).append("=").append(n.value);
        if (n != findMax(root)) { // запятая ставится после всех, кроме самого правого элемента
            sb.append(", ");
        }
        buildString(n.right, sb);
    }

    private Node findMax(Node p) {
        return p.right == null ? p : findMax(p.right);
    }

    // Остальные методы интерфейса Map (заглушки согласно стандарту подобных заданий)
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}