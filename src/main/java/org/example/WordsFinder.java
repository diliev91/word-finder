package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordsFinder {
    private static final String WORDS_URL = "https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt";

    public void solve(int wordLength) throws Exception {
        Map<Integer, Set<String>> wordsMap = readWords(wordLength);
        Set<String> startingWords = wordsMap.get(wordLength);
        Set<String> reusableWords = new HashSet<>();
        for(String word : startingWords) {
            if(canBeReduced(word, wordLength, wordsMap, new HashSet<>())) {
                reusableWords.add(word);
            }
        }
        System.out.println("Valid words:");
        for (String word : reusableWords) {
            System.out.println(word);
        }
        System.out.println("Word count: " + reusableWords.size());
    }


    private boolean canBeReduced(String word, int length, Map<Integer, Set<String>> wordMap, Set<String> visited) {
        if (length == 1) {
            return word.equals("I") || word.equals("A");
        }
        if (visited.contains(word)) {
            return false;
        }
        visited.add(word);
        for (int i = 0; i < word.length(); i++) {
            String nextWord = word.substring(0, i) + word.substring(i + 1);
            if (wordMap.getOrDefault(length - 1, new HashSet<>()).contains(nextWord)  || length - 1 == 1) {
                if (canBeReduced(nextWord, length - 1, wordMap, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Map<Integer, Set<String>> readWords(int wordLength) throws Exception {
        Map<Integer, Set<String>> wordMap = new HashMap<>();
        URL url = new URL(WordsFinder.WORDS_URL);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))){
            String word;
            //Skip the first two lines
            in.readLine();
            in.readLine();

            while ((word = in.readLine()) != null) {
                int length = word.length();
                if(length >wordLength) {
                    continue;
                }
                wordMap.computeIfAbsent(length, k -> new HashSet<>()).add(word);
            }
            return wordMap;
        }

    }
}
