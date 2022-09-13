package edu.lysak.analyzer;

public class FileResult {
    private String fileName;
    private String fileType;

    public FileResult() {
    }

    public FileResult(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }
}
