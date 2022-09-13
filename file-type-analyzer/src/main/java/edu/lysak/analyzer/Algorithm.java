package edu.lysak.analyzer;

public enum Algorithm {
    NAIVE("--naive"),
    KMP("--KMP"),
    UNKNOWN_ALGORITHM("Unknown algorithm");

    private final String algorithmName;

    Algorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public static Algorithm valueFrom(String algorithmName) {
        for (Algorithm algorithm : values()) {
            if (algorithmName.equalsIgnoreCase(algorithm.getAlgorithmName())) {
                return algorithm;
            }
        }
        return UNKNOWN_ALGORITHM;
    }
}
