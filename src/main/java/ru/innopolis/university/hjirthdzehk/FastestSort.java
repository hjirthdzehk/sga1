package ru.innopolis.university.hjirthdzehk;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FastestSort {

    double[] values;

    private static int convertTo(double value) {
        return (int) Math.round(value * 1000000 + 500000000);
    }

    private static double convertFrom(int value) {
        return (value - 500000000) / 1000000.0;
    }

    public static double[] countingSort(double[] input, int bucketsCount) {
        byte[] counts = new byte[bucketsCount];
        Arrays.stream(input).forEach(v -> counts[convertTo(v)]++);

        int currentInsertPos = 0;
        for (int currentBucket = 0; currentBucket < bucketsCount; ++currentBucket) {
            if (counts[currentBucket] == 0) continue;
            for (byte i = 0; i < counts[currentBucket]; ++i) {
                input[currentInsertPos++] = convertFrom(currentBucket);
            }
        }

        return input;
    }

    public static void main(String[] args) throws IOException {
        FastestSort s = new FastestSort();
        s.setUp();
        System.out.println("Started!");
        double[] result = countingSort(s.values, 1000000000);
        System.out.println("Done!");
    }

    private static double[] readValues(String filename) throws IOException {
        return Files.lines(Paths.get(filename)).parallel().
                mapToDouble(Double::parseDouble).toArray();
    }

    @BeforeExperiment
    void setUp() throws IOException {
        values = readValues("./data.txt");
    }

    @Benchmark
    double[] timeSort(int reps) {
        return countingSort(values, 1000000000);
    }

}
