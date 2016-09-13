package ru.innopolis.university.hjirthdzehk;

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


    }

    private static void mergeSort(double[] data, int leftPos, int rightPos, int endPos) {
        while (rightPos < endPos) {

        }
    }

}
