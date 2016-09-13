package norm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DanilovNorm {
    public double calculateNorm(List<Map<String, Integer>> mapList) {
        // Assumes that every map in mapList has the same set of keys
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

    public double calculateDevianceNorm(Map<String, Float> meanFreq, Map<String, Integer> newSample) {
        int M = meanFreq.size();
        double g = 0;
        List<String> commonKeys = new ArrayList<>(meanFreq.keySet());

        for (int i = 0; i < M; i++) {
            int newSampleValue = 0;
            if (newSample.containsKey(commonKeys.get(i))) newSampleValue = newSample.get(commonKeys.get(i));
            g += Math.pow(meanFreq.get(commonKeys.get(i)) - newSampleValue, 2);
        }
        return Math.sqrt(g)/M;
    }
}
