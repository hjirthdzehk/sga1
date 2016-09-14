package ru.innopolis.university.hjirthdzehk;

import norm.DanilovNorm;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.russianStemmer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class WordsCounter {

    public static void main(String[] args) {
        testDanilovNorm();
//        testGetAllKeys();
//        testComplementToAllKeys();
    }

    private static void testDanilovNorm() {
        List<Map<String, Integer>> wordFreqList = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            wordFreqList.add(getWordFreq("./texts/train" + i + ".txt"));
        }

        Set<String> allKeys = getAllKeysFromMapList(wordFreqList);
        List<Map<String, Integer>> wordFreqListAllKeys = complementToAllKeys(wordFreqList, allKeys);
        System.out.println("Danilov norm: " + DanilovNorm.calculateNorm(wordFreqListAllKeys));
        Map<String, Double> meanFreqMap = getMeanFreqMap(wordFreqListAllKeys);

        List<Double> deviances = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            List<Map<String, Integer>> testSample = new ArrayList<>();
            if (i == 11) {
                testSample.add(getWordFreq("./texts/test1.txt"));
            } else {
                testSample.add(getWordFreq("./texts/train" + i + ".txt"));
            }
            Map<String, Integer> testSampleAllKeys = complementToAllKeys(testSample, allKeys).get(0);
            double devianceNorm = DanilovNorm.calculateDevianceNorm(meanFreqMap, testSampleAllKeys);
            String name;
            if (i == 11) {
                name = "test text\n";
            } else {
                name = "train text " + i;
            }
            if (i != 11) {
                deviances.add(devianceNorm);
            }
            System.out.println("Deviance norm is: " + devianceNorm + " for " + name);
        }

        double mean = mean(deviances);
        System.out.println("Mean for deviances is :" + mean);

        double std = std_deviation(deviances);
        System.out.println("Standard deviation is: " + std);
    }

    private static Map<String, Integer> getWordFreq(String path) {
        Map<String, Integer> counter = new TreeMap<>();
        try {
            Files.lines(Paths.get(path)).forEach(
                (line) -> {
                    SnowballStemmer stemmer = new russianStemmer();
                    String[] words = line.replaceAll("[();:?!,.«»'—\\{\\}\"-]", "").split(" +");
                    for (String word : words) {
                        if (word.equals("")) {
                            continue;
                        }
                        word = word.toLowerCase();
                        stemmer.setCurrent(word);
                        stemmer.stem();
                        word = stemmer.getCurrent();
                        if (!counter.containsKey(word)) {
                            counter.put(word, 1);
                        } else {
                            counter.put(word, counter.get(word) + 1);
                        }
                    }
                }
            );
        } catch (IOException e) {
            System.out.println("File not found: " + path);
        }
        return counter;
    }

    private static double mean(List<Double> list) {
        return list.parallelStream().mapToDouble(Double::doubleValue).
                sum() / list.size();
    }

    private static double std_deviation(List<Double> list) {
        double mean = mean(list);
        double sum = list.parallelStream().mapToDouble(Double::doubleValue).
                map((d) -> Math.pow((d - mean), 2)).sum();
        return Math.sqrt(sum / list.size());
    }

    private static Set<String> getCommonKeys(List<Set<String>> list) {
        if (list == null) return null;
        Set<String> result = new HashSet<>(list.get(0));
        list.forEach(result::retainAll);
        return result;
    }

    public static Set<String> getAllKeysFromMapList(List<Map<String, Integer>> list) {
        List<Set<String>> allKeys = getKeysFromMapList(list);
        Set<String> result = new HashSet<>();
        allKeys.forEach(result::addAll);
        return result;
    }

    private static List<Set<String>> getKeysFromMapList(List<Map<String, Integer>> list) {
        return list.stream().
                map(Map::keySet).
                collect(Collectors.toList());
    }

    public static List<Map<String, Integer>> complementToAllKeys(
            List<Map<String, Integer>> list, Set<String> allKeys) {
        List<Map<String, Integer>> result = new ArrayList<>();
        for (Map<String, Integer> map : list) {
            Map<String, Integer> resMap = new HashMap<>();
            for (String key : allKeys) {
                if (map.containsKey(key)) {
                    resMap.put(key, map.get(key));
                } else {
                    resMap.put(key, 0);
                }
            }
            result.add(resMap);
        }
        return result;
    }

    private static Map<String, Double> getMeanFreqMap(List<Map<String, Integer>> list) {
        int length = list.size();
        Map<String, Double> result = new TreeMap<>();
        for (String key : list.get(0).keySet()) {
            double mean = mean(list.parallelStream().
                    map((m) -> (double) m.get(key)).
                    collect(Collectors.toList())
            );
            result.put(key, mean);
        }
        return result;
    }

    @Deprecated
    private static Set<String> getCommonKeysFromMapList(List<Map<String, Integer>> list) {
        return getCommonKeys(getKeysFromMapList(list));
    }

    @Deprecated
    private static List<Map<String, Integer>> keepOnlyCommonKeys(
            List<Map<String, Integer>> list, Set<String> commonKeys) {
        for (Map<String, Integer> map : list) {
            map.keySet().retainAll(commonKeys);
        }
        return list;
    }

}