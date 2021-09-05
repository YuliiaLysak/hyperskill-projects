package edu.lysak.search;

import java.util.*;
import java.util.stream.Collectors;

public class SearchEngine {

    public List<Integer> findMatches(Strategy strategy, List<String> people, List<String> searchData) {
        Map<String, List<Integer>> allIndexes = getInvertedIndexes(people);

        List<Integer> result = new ArrayList<>();

        switch (strategy) {
            case ALL:
                for (String data : searchData) {
                    List<Integer> dataIndexes = allIndexes.get(data);
                    if (dataIndexes == null) {
                        continue;
                    }
                    if (result.isEmpty()) {
                        result.addAll(dataIndexes);
                    } else {
                        result.retainAll(dataIndexes);
                    }
                }
                break;
            case ANY:
                for (String data : searchData) {
                    List<Integer> dataIndexes = allIndexes.get(data);
                    if (dataIndexes == null) {
                        continue;
                    }
                    result.addAll(dataIndexes);
                }
                break;
            case NONE:
                result = allIndexes.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                for (String data : searchData) {
                    List<Integer> dataIndexes = allIndexes.get(data);
                    if (dataIndexes == null) {
                        continue;
                    }
                    result.removeAll(dataIndexes);
                }
                result = result.stream()
                        .distinct()
                        .collect(Collectors.toList());
                break;
        }
        return result;
    }

    private Map<String, List<Integer>> getInvertedIndexes(List<String> people) {
        Map<String, List<Integer>> invertedIndexes = new HashMap<>();
        for (int i = 0; i < people.size(); i++) {
            String[] words = people.get(i).toLowerCase().split("\\s+");
            for (String word : words) {
                List<Integer> indexes = invertedIndexes.getOrDefault(word, new ArrayList<>());
                indexes.add(i);
                invertedIndexes.put(word, indexes);
            }
        }
        return invertedIndexes;
    }
}
