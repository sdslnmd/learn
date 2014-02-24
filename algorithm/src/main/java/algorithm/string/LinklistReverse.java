package algorithm.string;

public class LinklistReverse {

    public static LinkeNode reverse(LinkeNode list) {

        if (list == null) {
            return null;
        }
        if (list.next == null) {
            return list;
        }

        LinkeNode next = list.next;
        list.next = null;

        LinkeNode reverse = reverse(next);

        next.next = list;

        return reverse;
    }

    public static class LinkeNode {
        public int val;
        public LinkeNode pre;
        public LinkeNode next;
    }

    public static void main(String[] args) {
        LinkeNode node = new LinkeNode();
        node.val = 1;

        LinkeNode node2 = new LinkeNode();
        node2.val = 2;

        LinkeNode node3 = new LinkeNode();
        node3.val = 3;


        node.next = node2;

        node2.pre = node;
        node2.next = node3;

        node3.pre = node2;


        LinkeNode reverse = LinklistReverse.reverse(node);

        System.out.println(reverse);

    }
}
