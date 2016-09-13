package ru.innopolis.university.hjirthdzehk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastestSort {

    private static boolean isSorted(double[] data) {
        for (int i = 1; i < data.length; ++i) {
            double left = data[i - 1];
            double right = data[i];
            if (left > right) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSortedReverse(double[] data) {
        for (int i = 1; i < data.length; ++i) {
            double left = data[i - 1];
            double right = data[i];
            if (left < right) {
                return false;
            }
        }
        return true;
    }

    public static void sort(double[] data) {
        Arrays.sort(data);
    }

    private static double[] bucketSort(double[] data, int bucketsCount) {

        int lowerBound = -500;
        int upperBound = 500;

        List<List<Double>> buckets = new ArrayList<>(bucketsCount);
        for (int i = 0; i < bucketsCount; ++i) {
            buckets.add(new ArrayList<>(data.length / bucketsCount));
        }

        for (double currentElement : data) {
            assert currentElement >= lowerBound && currentElement <= upperBound;
            int bucketIndex = (int) Math.floor(bucketsCount * currentElement / (2 * upperBound) + bucketsCount / 2); // TODO might not work for other bounds
            buckets.get(bucketIndex).add(currentElement);
        }

        double[] result = new double[data.length];
        int currentPosition = 0;

        for (List<Double> bucket : buckets) {
            double[] temp = new double[bucket.size()];
            for (int i = 0; i < bucket.size(); ++i) {
                temp[i] = bucket.get(i);
            }
            Arrays.sort(temp); // todo sort in parallel
            for (int i = 0; i < bucket.size(); ++i) {
                result[currentPosition] = temp[i];
                currentPosition++;
            }
        }

        return result;

    }

    public static void main(String[] args) throws IOException {
        double[] values = readValues("./data.txt");
        System.out.println("Started!");
        double[] result = bucketSort(values, 50000);
        Arrays.sort(values);
        System.out.println("Done!");
    }

    private static double[] readValues(String filename) throws IOException {
        return Files.lines(Paths.get(filename)).parallel().
                mapToDouble(Float::parseFloat).toArray();
    }

}
