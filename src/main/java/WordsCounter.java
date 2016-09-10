import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.russianStemmer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WordsCounter {

    public static void main(String[] args) {

        String filename = args.length > 0 ? args[0] : "input.txt";

        try {
            Map<String, Integer> counter = new HashMap<>();
            Map<String, List<String>> wordsVariants = new HashMap<>();

            Files.lines(Paths.get(filename)).forEach(
                    (line) -> {
                        SnowballStemmer stemmer = new russianStemmer();
                        String[] words = line.
                                replaceAll("[();:?!,.«»'—\\{\\}\"-]", "").
                                split(" +");
                        for (String word : words) {
                            if (word.equals("")) continue;
                            word = word.toLowerCase();
                            String originalWord = "" + word;
                            stemmer.setCurrent(word);
                            stemmer.stem();
                            word = stemmer.getCurrent();
                            if (!counter.containsKey(word)) {
                                counter.put(word, 1);
                                List<String> variants = new LinkedList<>();
                                variants.add(originalWord);
                                wordsVariants.put(word, variants);
                            } else {
                                counter.put(word, counter.get(word) + 1);
                                wordsVariants.get(word).add(originalWord);
                            }
                        }
                    }
            );

            for (String word : counter.keySet()) {
                System.out.println("(" + word + "): "
                        + wordsVariants.get(word) + " : " + counter.get(word));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + filename);
        }
    }

}