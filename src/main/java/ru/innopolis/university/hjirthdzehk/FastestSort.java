package ru.innopolis.university.hjirthdzehk;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FastestSort {

    private static final int BITS_PER_BYTE = 8;
    double[] values;
    double[] copy;

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
//        s.quickSort();
//        s.radixSort(s.values);
//        Arrays.sort(s.values);
//        double[] result = countingSort(s.values, 1000000000);
        System.out.println("Done!");
    }

    private static double[] readValues(String filename) throws IOException {
        return Files.lines(Paths.get(filename)).parallel().
                mapToDouble(Double::parseDouble).toArray();
    }

    public static double[] radixSort(double[] numbers) {
        final int R = 1 << BITS_PER_BYTE;    // each bytes is between 0 and 255
        final int MASK = R - 1;              // 0xFF
        final int BYTES_COUNT = Integer.SIZE / BITS_PER_BYTE;  // each int is 4 bytes

        final int numbersCount = numbers.length;
        double[] aux = new double[numbersCount];

        for (byte currentByte = 0; currentByte < BYTES_COUNT; currentByte++) {
            int[] count = new int[R + 1];
            for (double number : numbers) {
                short c = (short) ((convertTo(number) >> BITS_PER_BYTE * currentByte) & MASK);
                count[c + 1]++;
            }

            for (short r = 0; r < R; r++) {
                count[r + 1] += count[r];
            }

            if (currentByte == BYTES_COUNT - 1) {
                int shift1 = count[R] - count[R >> 1];
                int shift2 = count[R >> 1];
                for (short r = 0; r < (R >> 1); r++) {
                    count[r] += shift1;
                    count[(R >> 1) + r] -= shift2;
                }
            }

            for (double number : numbers) {
                short c = (short) ((convertTo(number) >> BITS_PER_BYTE * currentByte) & MASK);
                aux[count[c]++] = number;
            }

            System.arraycopy(aux, 0, numbers, 0, numbersCount);
        }
        return numbers;
    }

    public static double[] quickSort(double[] numbers) {
        quickSort(numbers, 0, numbers.length);
        return numbers;
    }

    private static void quickSort(double[] numbers, int from, int to) {
        if (to - from <= 46) { // 47 is an insertion sort threshold in standard library
            Arrays.sort(numbers, from, to);
            return;
        }

        double median = median(numbers, from, to);

        int left = from;
        int right = to - 1;

        while (left < right) {
            while (numbers[left] <= median) {
                left++;
            }
            while (numbers[right] > median) {
                right--;
            }
            if (left < right) {
                swap(numbers, left, right);
            }
        }
        quickSort(numbers, from, left);
        quickSort(numbers, left, to);
    }

    private static void swap(double[] numbers, int from, int to) {
        double tmp = numbers[from];
        numbers[from] = numbers[to];
        numbers[to] = tmp;
    }

    private static double median(double[] numbers, int from, int to) {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        for (int i = from; i < to; ++i) {
            double current = numbers[i];
            max = current > max ? current : max;
            min = current < min ? current : min;
        }
        return (min + max) / 2;
    }

    @BeforeExperiment
    private void setUp() throws IOException {
        values = readValues("./data.txt");
        copy = new double[values.length];
    }

    @Benchmark
    double[] timeStandartSort(int reps) {
        for (int i = 0; i < reps; ++i) {
            System.arraycopy(values, 0, copy, 0, values.length);
            Arrays.sort(copy);
        }
        return copy;
    }

    @Benchmark
    double[] timeQuickSort(int reps) {
        for (int i = 0; i < reps; ++i) {
            System.arraycopy(values, 0, copy, 0, values.length);
            quickSort(copy);
        }
        return copy;
    }

    @Benchmark
    double[] timeCountingSort(int reps) {
        for (int i = 0; i < reps; ++i) {
            System.arraycopy(values, 0, copy, 0, values.length);
            countingSort(copy, 1000000000);
        }
        return copy;
    }

    @Benchmark
    double[] timeRadixSort(int reps) {
        for (int i = 0; i < reps; ++i) {
            System.arraycopy(values, 0, copy, 0, values.length);
            radixSort(copy);
        }
        return copy;
    }

}
