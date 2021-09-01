package edu.lysak.sorting2;

import edu.lysak.sorting2.reader.DataReader;
import edu.lysak.sorting2.sorter.Sorter;
import edu.lysak.sorting2.writer.DataWriter;

import java.util.List;

public class SortingTool {
    private final String sortingType;
    private final DataReader dataReader;
    private final DataWriter dataWriter;
    private final Sorter sorter;

    public void proceed() {
        List sortedData = sorter.sort(dataReader.readData());

        switch (sortingType) {
            case "natural":
                dataWriter.writeDataNatural(sortedData);
                break;
            case "byCount":
                dataWriter.writeDataByCount(sortedData);
                break;
            default:
                break;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private SortingTool(Builder builder) {
        this.sortingType = builder.sortingType;
        this.dataReader = builder.dataReader;
        this.dataWriter = builder.dataWriter;
        this.sorter = builder.sorter;
    }

    public static class Builder {
        private String sortingType;
        private DataReader dataReader;
        private DataWriter dataWriter;
        private Sorter sorter;

        private Builder() {
        }

        public Builder sortingType(String sortingType) {
            this.sortingType = sortingType;
            return this;
        }

        public Builder dataReader(DataReader dataReader) {
            this.dataReader = dataReader;
            return this;
        }

        public Builder dataWriter(DataWriter dataWriter) {
            this.dataWriter = dataWriter;
            return this;
        }

        public Builder sorter(Sorter sorter) {
            this.sorter = sorter;
            return this;
        }

        public SortingTool build() {
            return new SortingTool(this);
        }
    }
}
