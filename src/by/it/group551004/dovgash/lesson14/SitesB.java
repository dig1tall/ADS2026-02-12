package by.it.group551004.dovgash.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class SitesB {

    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        public void makeSet(String site) {
            if (!parent.containsKey(site)) {
                parent.put(site, site);
                size.put(site, 1);
            }
        }

        public String find(String i) {
            if (!parent.containsKey(i)) {
                return null;
            }
            String p = parent.get(i);
            if (p.equals(i)) {
                return i;
            }
            String root = find(p);
            parent.put(i, root);
            return root;
        }

        public void union(String i, String j) {
            makeSet(i);
            makeSet(j);
            String rootI = find(i);
            String rootJ = find(j);

            if (!rootI.equals(rootJ)) {
                int sizeI = size.get(rootI);
                int sizeJ = size.get(rootJ);
                if (sizeI < sizeJ) {
                    parent.put(rootI, rootJ);
                    size.put(rootJ, sizeI + sizeJ);
                } else {
                    parent.put(rootJ, rootI);
                    size.put(rootI, sizeI + sizeJ);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DSU dsu = new DSU();

        while (true) {
            String line = reader.readLine();
            if (line == null || line.equals("end")) {
                break;
            }
            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                dsu.union(parts[0], parts[1]);
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (String site : dsu.parent.keySet()) {
            if (dsu.parent.get(site).equals(site)) {
                clusterSizes.add(dsu.size.get(site));
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