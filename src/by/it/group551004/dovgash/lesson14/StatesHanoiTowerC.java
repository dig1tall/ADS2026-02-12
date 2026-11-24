package by.it.group551004.dovgash.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StatesHanoiTowerC {

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
        String s = reader.readLine();
        if (s == null) return;
        int n = Integer.parseInt(s.trim());

        int totalSteps = (1 << n) - 1;
        if (totalSteps <= 0) {
            System.out.println("");
            return;
        }

        int[] maxHeightAtStep = new int[totalSteps];

        int[] pos = new int[n + 1];
        Arrays.fill(pos, 0);

        for (int step = 1; step <= totalSteps; step++) {
            int disk = 1;
            int temp = step;
            while (temp % 2 == 0) {
                disk++;
                temp /= 2;
            }

        }


        int[] rodA = new int[n + 1];
        int[] rodB = new int[n + 1];
        int[] rodC = new int[n + 1];
        int hA = n, hB = 0, hC = 0;

        for (int i = 1; i <= n; i++) {
            rodA[i - 1] = n - i + 1; // от большего к меньшему, вершина в конце
        }

        int from = 0, to = 1, aux = 2; // 0: A, 1: B, 2: C
        if (n % 2 == 0) {
            to = 2;
            aux = 1;
        }

        int currentHA = n, currentHB = 0, currentHC = 0;

        class SimpleRod {
            int[] elements = new int[n + 5];
            int size = 0;
            void push(int x) { elements[size++] = x; }
            int pop() { return elements[--size]; }
            int peek() { return size > 0 ? elements[size - 1] : 100; }
        }

        SimpleRod A = new SimpleRod();
        SimpleRod B = new SimpleRod();
        SimpleRod C = new SimpleRod();

        for (int i = n; i >= 1; i--) {
            A.push(i);
        }

        SimpleRod[] rods = {A, B, C};
        int f = 0, t = (n % 2 == 0 ? 2 : 1), au = (n % 2 == 0 ? 1 : 2);

        for (int step = 1; step <= totalSteps; step++) {
            maxHeightAtStep[step - 1] = Math.max(A.size, Math.max(B.size, C.size));

            int targetRodFrom, targetRodTo;
            if (step % 3 == 1) {
                targetRodFrom = f; targetRodTo = t;
            } else if (step % 3 == 2) {
                targetRodFrom = f; targetRodTo = au;
            } else {
                targetRodFrom = au; targetRodTo = t;
            }

            SimpleRod r1 = rods[targetRodFrom];
            SimpleRod r2 = rods[targetRodTo];

            if (r1.size == 0) {
                r1.push(r2.pop());
            } else if (r2.size == 0) {
                r2.push(r1.pop());
            } else if (r1.peek() < r2.peek()) {
                r2.push(r1.pop());
            } else {
                r1.push(r2.pop());
            }
        }

        DSU dsu = new DSU(totalSteps);
        int[] firstOccurrence = new int[n + 2];
        Arrays.fill(firstOccurrence, -1);

        for (int i = 0; i < totalSteps; i++) {
            int h = maxHeightAtStep[i];
            if (firstOccurrence[h] == -1) {
                firstOccurrence[h] = i;
            } else {
                dsu.union(firstOccurrence[h], i);
            }
        }

        int[] sizes = new int[totalSteps];
        int count = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (dsu.parent[i] == i) {
                sizes[count++] = dsu.size[i];
            }
        }

        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (sizes[j] > sizes[j + 1]) {
                    int temp = sizes[j];
                    sizes[j] = sizes[j + 1];
                    sizes[j + 1] = temp;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(sizes[i]);
            if (i < count - 1) {
                sb.append(" ");
            }
        }
        System.out.println(sb.toString());
    }
}