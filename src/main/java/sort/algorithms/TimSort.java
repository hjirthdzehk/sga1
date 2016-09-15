package sort.algorithms;

import java.util.Stack;

public class TimSort {
    private class Range{
        int start;
        int length;

        Range(int start, int length) {
            this.start = start;
            this.length = length;
        }
    }

    public void sort(long[] list){
        int minrun = getMinrun(list.length);
        Stack<Range> runs = new Stack<>();
        int currentIndex = 0;
        while ((currentIndex) < list.length){
            int runStart = currentIndex;
            currentIndex++;
            int runLength = 1;
            while (currentIndex < list.length && (list[currentIndex - 1] < list[currentIndex] || runLength < minrun)) {
                currentIndex++;
                runLength++;
            }
            runs.push(new Range(runStart, runLength));
            insertionSort(list, runStart, runStart+runLength);
        }

        while (!runs.empty()) {
            Range x = runs.pop();
            if (runs.empty()){
                break;
            }
            Range y = runs.pop();
            if (runs.empty()){
                mergeRuns(list, x, y);
                break;
            }
            Range z = runs.pop();
            if (x.length < y.length+z.length || y.length < z.length) {
                if (x.length < z.length) {
                    mergeRuns(list, x, y);
                    runs.push(z);
                    runs.push(new Range(y.start, y.length + x.length));
                } else {
                    mergeRuns(list, z, y);
                    runs.push(new Range(z.start, z.length + y.length));
                    runs.push(x);
                }
            } else {
                runs.push(z);
                runs.push(y);
                runs.push(x);
            }
        }
    }

    private void mergeRuns(long[] list, Range first, Range second) {
        if (first.start > second.start) {
            Range temp = first;
            first = second;
            second = temp;
        }
        long[] tempList = new long[first.length];
        System.arraycopy(list, first.start, tempList, 0, first.length);
        int tempIndex = 0, secondIndex = second.start;
        int secondEndIndex = second.start + second.length;
        int resultIndex = first.start;
        while (tempIndex < first.length && secondIndex < secondEndIndex && resultIndex < list.length) {
            if (tempList[tempIndex] < list[secondIndex]) {
                list[resultIndex] = tempList[tempIndex++];
            } else {
                list[resultIndex] = list[secondIndex++];
            }
            resultIndex++;
        }
        if (tempIndex < first.length &&  resultIndex < list.length){
            System.arraycopy(tempList, tempIndex, list, resultIndex, tempList.length - tempIndex);
        } else if (secondIndex < secondEndIndex &&  resultIndex < list.length){
            System.arraycopy(list, secondIndex, list, resultIndex, secondEndIndex - secondIndex);
        }
    }

    private void insertionSort(long[] arr, int from, int to){
        int i, j;
        long newValue;
        for (i = from+1; i <= to && i<arr.length; i++) {
            newValue = arr[i];
            j = i;
            while (j > from && arr[j - 1] > newValue) {
                arr[j] = arr[j - 1];
                j--;
            }
            arr[j] = newValue;
        }
    }

    private int getMinrun(int n)
    {
        int r = 0;
        while (n >= 64) {
            r |= n & 1;
            n >>= 1;
        }
        return n + r;
    }
}
