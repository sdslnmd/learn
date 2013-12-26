package com.engineer.sun.algorithm;

public class LCA {

    public Node solution(Node root,int v1,int v2) {
        if (root == null) {
            return null;
        }
        if (root.value > v1 && root.value > v2) {
            return solution(root.left, v1, v2);
        }
        else if (root.value < v1 && root.value < v2) {
            return solution(root.right, v1, v2);
        } else {
            return root;
        }
    }

    private class Node{
        public int value;
        public Node left;
        public Node right;
    }
}
