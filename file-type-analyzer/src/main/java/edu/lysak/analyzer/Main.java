package edu.lysak.analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
        byte[] fileByteArray = Files.readAllBytes(Path.of(args[0]));
        List<Byte> fileByteList = convertToList(fileByteArray);
        byte[] patternByteArray = args[1].getBytes();
        List<Byte> patternByteList = convertToList(patternByteArray);
        String result = args[2];
        if (Collections.indexOfSubList(fileByteList, patternByteList) < 0) {
            System.out.println("Unknown file type");
        } else {
            System.out.println(result);
        }
    }

    private static List<Byte> convertToList(byte[] byteArray) {
        return IntStream.range(0, byteArray.length)
                .mapToObj(i -> byteArray[i])
                .toList();
    }
}
