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
        DanilovNorm norm = new DanilovNorm();
        List<Map<String, Integer>> wordFreqList = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            wordFreqList.add(getWordFreq("./texts/train" + i + ".txt"));
        }
        Set<String> commonKeys = getCommonKeysFromMapList(wordFreqList);
        List<Map<String, Integer>> wordFreqListOnlyCommon = keepOnlyCommonKeys(wordFreqList, commonKeys);
        System.out.println("Danilov norm: " + norm.calculateNorm(wordFreqListOnlyCommon));
        Map<String, Float> meanFreqMap = getMeanFreqMap(wordFreqListOnlyCommon);

        List<Map<String, Integer>> testSample = new ArrayList<>();
        testSample.add(getWordFreq("./texts/train3.txt"));
        Map<String, Integer> testSampleOnlyCommon = keepOnlyCommonKeys(testSample, commonKeys).get(0);
        double devianceNorm = norm.calculateDevianceNorm(meanFreqMap, testSampleOnlyCommon);
        System.out.println("Deviance norm for train: " + devianceNorm);
    }

    private static Map<String, Integer> getWordFreq(String path) {
        Map<String, Integer> counter = new TreeMap<>();
        try {
            Files.lines(Paths.get(path)).forEach(
                (line) -> {
                    SnowballStemmer stemmer = new russianStemmer();
                    String[] words = line.replaceAll("[();:?!,.«»'—\\{\\}\"-]", "").split(" +");
                    for (String word : words) {
                        if (word.equals("")) continue;
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

    private static Set<String> getCommonKeys(List<Set<String>> list) {
        if (list == null) return null;
        Set<String> result = new HashSet<>(list.get(0));
        list.forEach(result::retainAll);
        return result;
    }

    private static Set<String> getCommonKeysFromMapList(List<Map<String, Integer>> list) {
        List<Set<String>> wordsList = list.stream().
                map(Map::keySet).
                collect(Collectors.toList());
        return getCommonKeys(wordsList);
    }

    private static List<Map<String, Integer>> keepOnlyCommonKeys(
            List<Map<String, Integer>> list, Set<String> commonKeys) {
        for (Map<String, Integer> map : list) {
            map.keySet().retainAll(commonKeys);
        }
        return list;
    }

    private static Map<String, Float> getMeanFreqMap(List<Map<String, Integer>> list) {
        int length = list.size();
        Map<String, Float> result = new TreeMap<>();
        for (String key : list.get(0).keySet()) {
            int accum = 0;
            for (Map<String, Integer> map : list) {
                accum += map.get(key);
            }
            result.put(key, (float)accum/length);
        }
        return result;
    }

}