package algorithm;

public class BST {

    private Node root;

    private class Node {
        public int key;
        public int value;
        public Node left, right;
        public int N;

        public Node(int key, int value, int N) {
            this.key = key;
            this.value = value;
            this.N = N;
        }

    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        return null == node ? 0 : node.N;
    }

    public int get(int key) {
        return get(root, key);
    }

    private int get(Node node, int key) {
        if (node == null) {
            return 0;
        }
        if (node.key > key) {
            return get(node.right, key);
        } else if (node.key < key) {
            return get(node.left, key);
        } else return node.value;

    }

    public void put(int key, int value) {
        root = put(root, key, value);
    }

    private Node put(Node node, int key, int value) {

        if (node == null) {
            return new Node(key, value, 1);
        }

        if (key > node.key) {
            put(node.right, key, value);
        } else if (key < node.key) {
            put(node.left, key, value);
        } else {
            node.value = value;
        }
        node.N = size(node.left) + size(node.right) + 1;

        return node;
    }

    public static void main(String[] args) {
        BST bst = new BST();
        bst.put(10, 10);
        bst.put(1, 1);
        bst.put(12, 12);
        bst.put(4, 4);

        int i = bst.size();
        System.out.println(i);

    }
}
