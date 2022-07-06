package edu.lysak.solver;

public final class Utils {
    private Utils() {
    }

    public static String getInputFile(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-in".equals(args[i]) && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        return "";
    }

    public static String getOutputFile(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-out".equals(args[i]) && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        return "";
    }
}
