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
//        s.values = new double[] {-1, 2, 3, -4, 5, -6};
        System.out.println("Started!");
//        s.quickSort();
        s.radixSort();
//        double[] result = countingSort(s.values, 1000000000);
        System.out.println("Done!");
    }

    private static double[] readValues(String filename) throws IOException {
        return Files.lines(Paths.get(filename)).parallel().
                mapToDouble(Double::parseDouble).toArray();
    }

    public void radixSort() {
        for (int shift = Integer.SIZE - 1; shift > -1; shift--) {
            double[] tmp = new double[values.length];
            int j = 0;
            for (int i = 0; i < values.length; i++) {
                boolean move = convertTo(values[i]) << shift >= 0;
                if ((shift == 0) != move) {
                    tmp[j] = values[i];
                    j++;
                } else {
                    values[i - j] = values[i];
                }
            }
            System.arraycopy(values, 0, tmp, j, tmp.length - j);
            values = tmp;
        }
    }

    public void quickSort() {
        quickSort(0, values.length);
    }

    private void quickSort(int from, int to) {
        if (to - from <= 200) {
            Arrays.sort(values, from, to);
            return;
        }

        double median = median(from, to);

        int left = from;
        int right = to - 1;

        while (left < right) {
            while (values[left] <= median) {
                left++;
            }
            while (values[right] > median) {
                right--;
            }
            if (left < right) {
                swap(left, right);
            }
        }
        quickSort(from, left);
        quickSort(left, to);
    }

    private void swap(int from, int to) {
        double tmp = values[from];
        values[from] = values[to];
        values[to] = tmp;
    }

    private double median(int from, int to) {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        for (int i = from; i < to; ++i) {
            double current = values[i];
            max = current > max ? current : max;
            min = current < min ? current : min;
        }
        return (min + max) / 2;
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
