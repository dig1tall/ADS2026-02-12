package by.it.group551004.dovgash.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;
    private String lastPutResult;

    private boolean isRed(Node x) {
        return x != null && x.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
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
        return get(key) != null && key instanceof Integer && containsKeyInternal((Integer) key);
    }

    private boolean containsKeyInternal(Integer key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node x, Object val) {
        if (x == null) return false;
        if (val == null ? x.value == null : val.equals(x.value)) return true;
        return containsValueRec(x.left, val) || containsValueRec(x.right, val);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node x = root;
        while (x != null) {
            int cmp = k.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.value;
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        lastPutResult = null;
        root = put(root, key, value);
        root.color = BLACK;
        return lastPutResult;
    }

    private Node put(Node h, Integer key, String value) {
        if (h == null) {
            size++;
            return new Node(key, value, RED);
        }

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, value);
        else if (cmp > 0) h.right = put(h.right, key, value);
        else {
            lastPutResult = h.value;
            h.value = value;
            return h;
        }

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        if (!containsKeyInternal(key)) return null;

        String val = get(key);
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = delete(root, key);
        if (root != null) root.color = BLACK;
        size--;
        return val;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node deleteMin(Node h) {
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);
        h.left = deleteMin(h.left);
        return balance(h);
    }

    private Node delete(Node h, Integer key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.value = x.value;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }
        return balance(h);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.left != null) x = x.left;
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node x = root;
        while (x.right != null) x = x.right;
        return x.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        fillHeadMap(root, toKey, sub);
        return sub;
    }

    private void fillHeadMap(Node x, Integer toKey, MyRbMap sub) {
        if (x == null) return;
        int cmp = toKey.compareTo(x.key);
        if (cmp > 0) {
            fillHeadMap(x.left, toKey, sub);
            sub.put(x.key, x.value);
            fillHeadMap(x.right, toKey, sub);
        } else {
            fillHeadMap(x.left, toKey, sub);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        fillTailMap(root, fromKey, sub);
        return sub;
    }

    private void fillTailMap(Node x, Integer fromKey, MyRbMap sub) {
        if (x == null) return;
        int cmp = fromKey.compareTo(x.key);
        if (cmp <= 0) {
            fillTailMap(x.left, fromKey, sub);
            sub.put(x.key, x.value);
            fillTailMap(x.right, fromKey, sub);
        } else {
            fillTailMap(x.right, fromKey, sub);
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb, new boolean[]{true});
        return sb.append("}").toString();
    }

    private void buildString(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        buildString(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        first[0] = false;
        sb.append(n.key).append("=").append(n.value);
        buildString(n.right, sb, first);
    }

    // Нереализуемые методы интерфейса SortedMap (заглушки)
    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue()); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}