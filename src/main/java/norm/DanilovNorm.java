package norm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DanilovNorm {
    public double calculateNorm(List<Map<String, Integer>> mapList) {
        // assume that every map in mapList has the same keys
        int N = mapList.size();
        int M = mapList.get(0).keySet().size();
        List<String> commonKeys = new ArrayList<>(mapList.get(0).keySet());
        double g = 0;
        for (int i = 0; i < N; i++) {
            for (int k = i + 1; k < N; k++) {
                for (int m = 0; m < M; m++) {
                    g += Math.pow(mapList.get(i).get(commonKeys.get(m)) -
                            mapList.get(k).get(commonKeys.get(m)), 2);
                }
            }
        }
        return 2 * Math.sqrt(g) / (N * (N - 1) * M);
    }
}
