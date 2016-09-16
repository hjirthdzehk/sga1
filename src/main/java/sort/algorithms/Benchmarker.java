package sort.algorithms;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static sort.algorithms.FastestSort.countingSort;

public class Benchmarker {
    double[] values;
    TimSort timSort;

    public static void main(String[] args) throws IOException {
        TimSort timSort = new TimSort();
        timSort.sort(readValues("./data.txt"));
    }

    @BeforeExperiment
    void setUp() throws IOException {
        values = readValues("./data.txt");
        timSort = new TimSort();
    }

    @Benchmark
    void timeCountingSort(int reps) {
        countingSort(values, 1000000000);
    }

    @Benchmark
    void timeTimSort(int reps){
        timSort.sort(values);
    }

    private static double[] readValues(String filename) throws IOException {
        return Files.lines(Paths.get(filename)).parallel().
                mapToDouble(Double::parseDouble).toArray();
    }
}
