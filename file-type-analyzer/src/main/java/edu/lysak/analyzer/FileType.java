package edu.lysak.analyzer;

public enum FileType {
    PDF("PDF document"),
    PEM("PEM certificate"),
    DOC("DOC document"),
    UNKNOWN_FILE_TYPE("Unknown file type");

    private final String resultString;

    FileType(String resultString) {
        this.resultString = resultString;
    }

    public String getResultString() {
        return resultString;
    }

    public static FileType valueFrom(String fileType) {
        for (FileType type : values()) {
            if (fileType.equalsIgnoreCase(type.getResultString())) {
                return type;
            }
        }
        return UNKNOWN_FILE_TYPE;
    }
}
