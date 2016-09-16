package sort.algorithms;

import java.util.Arrays;

class FastestSort {

    private static int convertTo(double value) {
        return (int) Math.round(value * 1000000 + 500000000);
    }

    private static double convertFrom(int value) {
        return (value - 500000000) / 1000000.0;
    }

    static void countingSort(double[] input, int bucketsCount) {
        byte[] counts = new byte[bucketsCount];
        Arrays.stream(input).forEach(v -> counts[convertTo(v)]++);

        int currentInsertPos = 0;
        for (int currentBucket = 0; currentBucket < bucketsCount; ++currentBucket) {
            if (counts[currentBucket] == 0) continue;
            for (byte i = 0; i < counts[currentBucket]; ++i) {
                input[currentInsertPos++] = convertFrom(currentBucket);
            }
        }
    }
}
