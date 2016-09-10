package norm;

import java.util.*;

/**
 * Created by lemhell on 10.09.16.
 */
public class DanilovNorm {
    public float calculateNorm(List<Map<String, Integer>> mapList) {
        int N = mapList.size();
        int M = mapList.get(0).keySet().size();
        List<String> commonKeys = new ArrayList<>(mapList.get(0).keySet());
        float g = 0;
        for (int i = 0; i < N; i++) {
            for (int k = i + 1; k < N; k++) {
                for (int m = 0; m < M; m++) {
                    g += Math.pow(mapList.get(i).get(commonKeys.get(m)) - mapList.get(k).get(commonKeys.get(m)), 2);
                }
            }
        }
        return (float)Math.sqrt(g)/(N * (N - 1) * M);
    }
}
