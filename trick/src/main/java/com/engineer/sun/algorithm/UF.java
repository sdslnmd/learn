package com.engineer.sun.algorithm;

/**
 * User: sunluning
 * Date: 12-12-1 下午5:37
 */
public class UF {
    private int[] id;
    private int count;

    public UF(int N) {
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

    private int find(int q) {
        return id[q];
    }

    public void union(int p,int q) {
        //将p和q归并到相同的分量中
        int pID = find(p);
        int qID = find(q);
        //如果p和q已经在相同的分量之中则不需要采取任何行动
        if (pID == qID) {
            return;
        }
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pID) {
                id[i] = qID;
            }
        }
    }

    public static void main(String[] args) {
        UF uf = new UF(10);
        uf.union(2,5);
        System.out.println(uf);
    }
}
