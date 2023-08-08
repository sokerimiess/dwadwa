import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class BST<K extends Comparable<K>, V> implements Iterable<K> {
    private Node root;
    private int size;

    public class Node {
        private K key;
        private V val;
        private Node left, right;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public void put(K key, V val) { //ключ - значение
        root = put(root, key, val);
    }

    private Node put(Node node, K key, V val) {
        if (node == null) {
            size++;
            return new Node(key, val);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, val);
        } else if (cmp > 0) {
            node.right = put(node.right, key, val);
        } else {
            node.val = val;
        }
        return node;
    }

    public V get(K key) {
        Node node = get(root, key);
        return node == null ? null : node.val;
    }

    private Node get(Node node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node;
        }
    }

    public void delete(K key) { //удаление по ключу
        root = delete(root, key);
    }

    private Node delete(Node node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            if (node.right == null) {
                size--;
                return node.left;
            }
            if (node.left == null) {
                size--;
                return node.right;
            }
            Node temp = node;
            node = min(temp.right);
            node.right = deleteMin(temp.right);
            node.left = temp.left;
            size--;
        }
        return node;
    }

    private Node min(Node node) {
        if (node.left == null) {
            return node;
        }
        return min(node.left);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        return node;
    }

    public boolean containsValue(V val) { //на наличие
        return containsValue(root, val);
    }

    private boolean containsValue(Node node, V val) {
        if (node == null) {
            return false;
        }

        if (node.val.equals(val)) {
            return true;
        }

        return containsValue(node.left, val) || containsValue(node.right, val);
    }

    public Iterator<K> iterator() { //возр
        return new InOrderIterator();
    }

    private class InOrderIterator implements Iterator<K> {
        private Node current;
        private Stack<Node> stack = new Stack<>();

        public InOrderIterator() {
            current = root;
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node node = stack.pop();
            current = node.right;
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            return node.key;
        }
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        BST<Integer, String> tree = new BST<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("число) или \"exit\" для выхода: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                int key = Integer.parseInt(input);
                System.out.print("Введите значение: ");
                String value = scanner.nextLine();
                tree.put(key, value);
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }

        for (Integer key : tree) {
            System.out.println("Ключ: " + key + ", Значение: " + tree.get(key));
        }

        System.out.print("Введите значение для поиска: ");
        String searchValue = scanner.nextLine();
        System.out.println("Значение \"" + searchValue + "\" доступно: " + tree.containsValue(searchValue));

        scanner.close();
    }
}