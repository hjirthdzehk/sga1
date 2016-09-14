package ru.innopolis.university.hjirthdzehk;

import junit.framework.TestCase;

import java.util.*;

import static ru.innopolis.university.hjirthdzehk.WordsCounter.complementToAllKeys;
import static ru.innopolis.university.hjirthdzehk.WordsCounter.getAllKeysFromMapList;


public class WordsCounterTest extends TestCase {

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

    public void testGetAllKeys() {
        List<Map<String, Integer>> mapList = getTestList();
        Set<String> allKeys = getAllKeysFromMapList(mapList);
        allKeys.forEach(System.out::println);
    }

    public void testComplementToAllKeys() {
        List<Map<String, Integer>> mapList = getTestList();
        Set<String> allKeys = getAllKeysFromMapList(mapList);
        List<Map<String, Integer>> newList = complementToAllKeys(mapList, allKeys);
        newList.forEach(System.out::println);
    }

}