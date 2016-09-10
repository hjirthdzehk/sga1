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
        List<Map<String, Integer>> wordFreqList = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            wordFreqList.add(getWordFreq("./texts/train" + i + ".txt"));
        }
        Set<String> commonKeys = getCommonKeysFromMapList(wordFreqList);
        List<Map<String, Integer>> wordFreqListOnlyCommon = keepOnlyCommonKeys(wordFreqList, commonKeys);
        System.out.println("Danilov norm: " + new DanilovNorm().calculateNorm(wordFreqListOnlyCommon));
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

}