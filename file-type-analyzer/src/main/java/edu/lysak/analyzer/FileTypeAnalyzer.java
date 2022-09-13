package edu.lysak.analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class FileTypeAnalyzer {

    public String analyzeFileType(String algorithm, String filePath, String pattern, String result) throws IOException {
        return switch (Algorithm.valueFrom(algorithm)) {
            case NAIVE -> analyzeNaive(filePath, pattern, result);
            case KMP -> analyzeKMP(filePath, pattern, result);
//            case KMP -> analyzeUsingCollections(filePath, pattern, result);
            case UNKNOWN_ALGORITHM -> FileType.UNKNOWN_FILE_TYPE.getResultString();
        };
    }

    private String analyzeKMP(String filePath, String pattern, String result) throws IOException {
        System.out.println("Using KMP");
        String target = new String(Files.readAllBytes(Path.of(filePath)));
        if (findPatternInText(pattern, target) < 0) {
            return FileType.UNKNOWN_FILE_TYPE.getResultString();
        }

        return result;
    }

    private int findPatternInText(String pattern, String text) {
        int index = -1;
        int patternLength = pattern.length();
        int textLength = text.length();
        char[] textCharArray = text.toCharArray();
        char[] patternArray = pattern.toCharArray();

        int[] suffixArray = generateSuffixArray(patternLength, pattern);

        int i = 0;
        boolean isMatched = true;
        int j = 0;
        while (j < patternLength && i < textLength) {
            if (textCharArray[i] == patternArray[j]) {
                j = j + 1;
                i = i + 1;
            } else {
                int newIndex = i - j + 1;
                isMatched = false;
                j = j - 1;
                if (j < 0) {
                    j = 0;
                }
                j = suffixArray[j];
                if (j == 0) {
                    i = newIndex;
                } else {
                    i = i + 1;
                }
            }

            if (j == patternLength) {
                isMatched = true;
            }

        }
        if (isMatched) {
            index = i - patternLength;
        }

        return index;
    }

    private static int[] generateSuffixArray(int patternLength, String pattern) {
        int[] suffixArray = new int[patternLength];
        char[] patternArray = pattern.toCharArray();
        int i = 1;
        while (i < patternLength) {
            for (int j = 0; j < patternLength; j++) {
                if ((i < patternLength) && (patternArray[i] == patternArray[j])) {
                    suffixArray[i] = j + 1;
                    i = i + 1;
                } else {
                    i = i + 1;
                    break;
                }

                if (i == patternLength - 1) {
                    break;
                }
            }

        }

        return suffixArray;
    }

    private String analyzeNaive(String filePath, String pattern, String result) throws IOException {
        System.out.println("Using Naive");
        String text = new String(Files.readAllBytes(Path.of(filePath)));
        if (naiveSearch(text, pattern)) {
            return result;
        } else {
            return FileType.UNKNOWN_FILE_TYPE.getResultString();
        }
    }

    private boolean naiveSearch(String text, String pattern) {
        int textLength = text.length();
        int patternLength = pattern.length();
        for (int i = 0; i < textLength - patternLength + 1; i++) {
            for (int j = 0; j < patternLength; j++) {
                if (!(text.charAt(i + j) == pattern.charAt(j))) {
                    break;
                } else if (j == patternLength - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private String analyzeUsingCollections(String filePath, String pattern, String result) throws IOException {
        byte[] fileByteArray = Files.readAllBytes(Path.of(filePath));
        byte[] patternByteArray = pattern.getBytes();
        List<Byte> fileByteList = convertToList(fileByteArray);
        List<Byte> patternByteList = convertToList(patternByteArray);
        if (Collections.indexOfSubList(fileByteList, patternByteList) < 0) {
            return FileType.UNKNOWN_FILE_TYPE.getResultString();
        } else {
            return result;
        }
    }

    private List<Byte> convertToList(byte[] byteArray) {
        return IntStream.range(0, byteArray.length)
                .mapToObj(i -> byteArray[i])
                .toList();
    }
}
