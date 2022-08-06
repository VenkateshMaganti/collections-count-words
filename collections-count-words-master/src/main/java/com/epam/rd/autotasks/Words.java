package com.epam.rd.autotasks;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class Words {

    public String countWords(List<String> lines) {
        /*
         * Calculate the frequency of each word
         */
        Map<String, Integer> frequency = new HashMap<>();
        for (String each : lines) {
            each = each.replaceAll("[“”]", "\"").replaceAll("[‘’]", "'").replaceAll("„", ",").replaceAll("—",
                    "-");
            String[] words = each.split("[ -/]");
            for (String ech : words) {
                if (ech != null && !ech.trim().isEmpty() && ech.length() > 2) {
                    ech = normalizeWord(ech);
                    if (frequency.containsKey(ech)) {
                        int count = frequency.get(ech) + 1;
                        frequency.put(ech, count);
                    } else {
                        frequency.put(ech, 1);
                    }
                }
            }
        }

        /*
         * Remove the word if its rare or length is too small
         */
        Iterator<Map.Entry<String, Integer>> it = frequency.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = it.next();
            if (pair.getKey().length() < 4 || pair.getValue() < 10) {
                it.remove();
            }
        }
        /*
         * Sort the words by value and if they are equal, sort by name
         */
        Locale locale = Locale.US;
        Collator col = Collator.getInstance(locale);

        List<Entry<String, Integer>> wordList = new LinkedList<>(frequency.entrySet());
        wordList.sort(new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                int i;
                if (o1.getValue().equals(o2.getValue())) {
                    i = o1.getKey().compareTo(o2.getKey());
                } else {
                    i = o2.getValue() - o1.getValue();
                }
                return i;
            }
        });

        /*
         * Build the output string
         */
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, Integer> each : wordList) {
            output.append(each.getKey()).append(" - ").append(each.getValue()).append("\n");
        }
        output = new StringBuilder(output.substring(0, output.length() - 1));
        return output.toString();
    }

    private String normalizeWord(String ech) {
        if (ech.contains("'s") || ech.contains("'es") || ech.contains("'t") || ech.contains("'ll")) {
            ech = ech.replaceAll("'s", "").replaceAll("'es", "").replaceAll("'t", "").replaceAll("'ll", "");
        }
        ech = ech.replaceAll("[.\" '<>()\\[\\]!@#$%^&*;:,?“”‘’„—]", "").toLowerCase();
        ech = ech.trim();
        return ech;
    }

}