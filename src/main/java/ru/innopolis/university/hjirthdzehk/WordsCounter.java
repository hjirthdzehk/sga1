package ru.innopolis.university.hjirthdzehk;

import norm.DanilovNorm;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.russianStemmer;

import javax.xml.bind.SchemaOutputResolver;
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

        double mean = 0;
        for (Double d : deviances) {
            mean += d;
        }
        mean = mean/deviances.size();
        System.out.println("Mean for deviances is :" + mean);

        double std = 0;
        for (Double d : deviances) {
            std += Math.pow((d - mean), 2);
        }
        System.out.println("Standard deviation is: " + Math.sqrt(std/deviances.size()));
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

    private static Set<String> getAllKeysFromMapList(List<Map<String, Integer>> list) {
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

    private static List<Map<String, Integer>> complementToAllKeys(
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
            int accum = 0;
            for (Map<String, Integer> map : list) {
                accum += map.get(key);
            }
            result.put(key, (double) (accum / length));
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

    // ------------------------ Tests ------------------------------
    // TODO: extract to JUnit tests

    private static void testGetAllKeys() {
        List<Map<String, Integer>> mapList = getTestList();
        Set<String> allKeys = getAllKeysFromMapList(mapList);
        allKeys.forEach(System.out::println);
    }

    private static void testComplementToAllKeys() {
        List<Map<String, Integer>> mapList = getTestList();
        Set<String> allKeys = getAllKeysFromMapList(mapList);
        List<Map<String, Integer>> newList = complementToAllKeys(mapList, allKeys);
        for (Map<String, Integer> map : newList) {
            System.out.println(map);
        }
    }

    private static List<Map<String, Integer>> getTestList() {
        List<Map<String, Integer>> map = new ArrayList<>();
        Map<String, Integer> m1 = new HashMap<>();
        m1.put("a", 1);
        m1.put("b", 1);
        m1.put("c", 1);
        map.add(m1);
        Map<String, Integer> m2 = new HashMap<>();
        m2.put("a", 2);
        m2.put("d", 2);
        m2.put("c", 2);
        map.add(m2);
        Map<String, Integer> m3 = new HashMap<>();
        m3.put("e", 3);
        m3.put("b", 3);
        m3.put("f", 3);
        map.add(m3);
        return map;
    }

}