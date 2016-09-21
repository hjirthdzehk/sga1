package norm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DanilovNorm {
    public static double calculateNorm(List<Map<String, Integer>> mapList) {
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
        return  Math.sqrt(2 * g / (N * (N - 1) * M));
    }

    public static double calculateDevianceNorm(Map<String, Double> meanFreq, Map<String, Integer> newSample) {
        int M = meanFreq.size();
        double g = meanFreq.keySet().parallelStream().
                map((word) -> {
                    int newSampleValue = 0;
                    if (newSample.containsKey(word)) newSampleValue = newSample.get(word);
                    return Math.pow(meanFreq.get(word) - newSampleValue, 2);
                }).
                reduce(Double::sum).get();
        return Math.sqrt(g/M);
    }
}
