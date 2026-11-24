package by.it.group551004.dovgash.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class PointsA {

    static class DSU {
        int[] parent;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null) return;
        StringTokenizer tokenizer = new StringTokenizer(line);

        double D = Double.parseDouble(tokenizer.nextToken());
        int N = Integer.parseInt(tokenizer.nextToken());

        double[][] points = new double[N][3];
        for (int i = 0; i < N; i++) {
            tokenizer = new StringTokenizer(reader.readLine());
            points[i][0] = Double.parseDouble(tokenizer.nextToken());
            points[i][1] = Double.parseDouble(tokenizer.nextToken());
            points[i][2] = Double.parseDouble(tokenizer.nextToken());
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dist = Math.sqrt(
                        Math.pow(points[i][0] - points[j][0], 2) +
                                Math.pow(points[i][1] - points[j][1], 2) +
                                Math.pow(points[i][2] - points[j][2], 2)
                );
                if (dist < D) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }

        clusterSizes.sort((a, b) -> b - a);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clusterSizes.size(); i++) {
            sb.append(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) {
                sb.append(" ");
            }
        }
        System.out.println(sb.toString());
    }
}