import sort.algorithms.TimSort;

public class EntryPoint {
    public static void main(String[] args) {
        TimSort timSort = new TimSort();
        long[] result = new long[5000];
        for (int i = 0; i < result.length; i++) {
            result[i] = result.length - i;
        }
        timSort.sort(result);
        for (long aResult : result) {
            System.out.println(aResult);
        }
    }
}
