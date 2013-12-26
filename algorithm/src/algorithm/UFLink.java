package algorithm;

/**
 * User: sunluning
 * Date: 12-12-1 下午5:37
 */
public class UFLink {
    private int[] id;
    private int count;

    public UFLink(int N) {
        count = N;
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    public int count() {
        return count;
    }

    public boolean connected(int p,int q) {
        return find(p) == find(q);
    }

    private int find(int p) {
        while (p != id[p]) {
            p = id[p];
        }
        return p;
    }

    public void union(int p,int q) {
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot) {
            return;
        }
        id[pRoot] = qRoot;

        count--;
    }

    public static void main(String[] args) {
        UFLink uf = new UFLink(10);
        uf.union(2,5);
        uf.union(2,4);
        System.out.println(uf);
    }
}
