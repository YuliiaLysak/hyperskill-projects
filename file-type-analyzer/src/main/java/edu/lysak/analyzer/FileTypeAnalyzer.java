package edu.lysak.analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class FileTypeAnalyzer {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);

    public String analyzeFile(String algorithm, String filePath, String pattern, String result) throws IOException {
        return switch (Algorithm.valueFrom(algorithm)) {
            case NAIVE -> analyzeNaive(filePath, pattern, result);
            case KMP -> analyzeKMP(filePath, pattern, result);
//            case KMP -> analyzeUsingCollections(filePath, pattern, result);
            case RK -> analyzeRK(filePath, pattern, result);
            case UNKNOWN_ALGORITHM -> FileType.UNKNOWN_FILE_TYPE.getResultString();
        };
    }

    private String analyzeRK(String filePath, String pattern, String result) throws IOException {
        String txt = new String(Files.readAllBytes(Path.of(filePath)));
        int m = pattern.length();
        int n = txt.length();
        int i, j;
        int p = 0;
        int t = 0;
        int h = 1;

        int q = 13;
        int d = txt.length();

        for (i = 0; i < m - 1; i++)
            h = (h * d) % q;

        // Calculate hash value for pattern and text
        for (i = 0; i < m; i++) {
            p = (d * p + pattern.charAt(i)) % q;
            t = (d * t + txt.charAt(i)) % q;
        }

        // Find the match
        for (i = 0; i <= n - m; i++) {
            if (p == t) {
                for (j = 0; j < m; j++) {
                    if (txt.charAt(i + j) != pattern.charAt(j))
                        break;
                }

                if (j == m) {
                    System.out.println("Pattern is found at position: " + (i + 1));
                    return result;
                }
            }

            if (i < n - m) {
                t = (d * (t - txt.charAt(i) * h) + txt.charAt(i + m)) % q;
                if (t < 0)
                    t = (t + q);
            }
        }
        return FileType.UNKNOWN_FILE_TYPE.getResultString();
    }

    public List<FileResult> analyzeFilesAsyncWithDb(String folderPath, String dbPath) throws IOException, InterruptedException {
        List<PatternInfo> patternInfoList = initDb(dbPath);

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        List<AnalyzerDb> analyzers = Arrays.stream(Objects.requireNonNull(files))
                .map(it -> new AnalyzerDb(it, patternInfoList))
                .toList();

        List<Future<FileResult>> futures = EXECUTOR.invokeAll(analyzers);
        EXECUTOR.shutdown();

        return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new FileResult();
                    }
                })
                .toList();
    }

    private List<PatternInfo> initDb(String dbPath) throws IOException {
        List<String> strings = Files.readAllLines(Path.of(dbPath));
        return strings.stream()
                .map(line -> line.split(";"))
                .map(FileTypeAnalyzer::buildPatternInfo)
                .toList();
    }

    public List<FileResult> analyzeFilesAsync(String folderPath, String pattern, String resultString) throws InterruptedException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        List<Analyzer> analyzers = Arrays.stream(Objects.requireNonNull(files))
                .map(file -> new Analyzer(file, pattern, resultString))
                .toList();

        List<Future<FileResult>> futures = EXECUTOR.invokeAll(analyzers);
        EXECUTOR.shutdown();

        return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new FileResult();
                    }
                })
                .toList();
    }

    private String analyzeKMPWithDb(String filePath, List<PatternInfo> patternInfoList) {
        try {
            String target = new String(Files.readAllBytes(Path.of(filePath)));
            return patternInfoList.stream()
                    .filter(it -> findPatternInText(it.getPattern(), target) >= 0)
                    .max(Comparator.comparing(PatternInfo::getPriority))
                    .map(PatternInfo::getResultString)
                    .orElse(FileType.UNKNOWN_FILE_TYPE.getResultString());
        } catch (IOException e) {
            e.printStackTrace();
            return FileType.UNKNOWN_FILE_TYPE.getResultString();
        }
    }

    private String analyzeKMP(String filePath, String pattern, String result) {
        try {
            String target = new String(Files.readAllBytes(Path.of(filePath)));
            if (findPatternInText(pattern, target) < 0) {
                return FileType.UNKNOWN_FILE_TYPE.getResultString();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private static PatternInfo buildPatternInfo(String[] array) {
        int priority = Integer.parseInt(array[0]);
        String pattern = array[1].substring(1, array[1].length() - 1);
        String resultString = array[2].substring(1, array[2].length() - 1);
        return new PatternInfo(priority, pattern, resultString);
    }

    class Analyzer implements Callable<FileResult> {
        private final File file;
        private final String pattern;
        private final String resultString;

        Analyzer(File file, String pattern, String resultString) {
            this.file = file;
            this.pattern = pattern;
            this.resultString = resultString;
        }

        @Override
        public FileResult call() {
            String output = analyzeKMP(file.getPath(), pattern, resultString);
            return new FileResult(file.getName(), output);
        }
    }

    class AnalyzerDb implements Callable<FileResult> {
        private final File file;
        private final List<PatternInfo> patternInfoList;

        AnalyzerDb(File file, List<PatternInfo> patternInfoList) {
            this.file = file;
            this.patternInfoList = patternInfoList;
        }

        @Override
        public FileResult call() {
            String output = analyzeKMPWithDb(file.getPath(), patternInfoList);
            return new FileResult(file.getName(), output);
        }
    }
}
