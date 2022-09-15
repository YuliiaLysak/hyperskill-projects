package edu.lysak.hypermetro;

import edu.lysak.hypermetro.model.MetroLine;
import edu.lysak.hypermetro.model.Station;
import edu.lysak.hypermetro.service.MetroJsonParser;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HyperMetro {
    private List<MetroLine> metroLines;
    private final MetroJsonParser metroJsonParser;

    public HyperMetro(MetroJsonParser metroJsonParser, String filePath) {
        this.metroJsonParser = metroJsonParser;
        initMetroLines(filePath);
    }

    private void initMetroLines(String filePath) {
        metroLines = metroJsonParser.parseJsonFile(filePath);
        System.out.println("Metro lines initialization finished, size = " + metroLines.size());
    }

    public void append(String lineName, String stationName) {
        Optional<MetroLine> metroLine = getMetroLine(lineName);
        if (metroLine.isPresent()) {
            LinkedList<Station> stations = metroLine.get().getStations();
            int lastId = stations.getLast().getId();
            stations.addLast(new Station(++lastId, stationName));
        }
    }

    public void addHead(String lineName, String stationName) {
        Optional<MetroLine> metroLine = getMetroLine(lineName);
        if (metroLine.isPresent()) {
            LinkedList<Station> stations = metroLine.get().getStations();
            int lastId = stations.getLast().getId();
            stations.addFirst(new Station(++lastId, stationName));
        }
    }

    public void remove(String lineName, String stationName) {
        Optional<MetroLine> metroLine = getMetroLine(lineName);
        if (metroLine.isPresent()) {
            LinkedList<Station> stations = metroLine.get().getStations();
            Optional<Station> station = stations.stream()
                    .filter(it -> stationName.equals(it.getName()))
                    .findFirst();
            station.ifPresent(stations::remove);
        }
    }

    public void output(String lineName) {
        Optional<MetroLine> metroLine = getMetroLine(lineName);
        if (metroLine.isPresent()) {
            LinkedList<Station> stations = new LinkedList<>(metroLine.get().getStations());
            stations.sort(Comparator.comparing(Station::getId));
            Station depot = new Station(0, "depot");
            stations.addFirst(depot);
            stations.addLast(depot);
            for (int i = 0; i < stations.size() - 2; i++) {
                System.out.printf("%s - %s - %s%n",
                        stations.get(i).getName(),
                        stations.get(i + 1).getName(),
                        stations.get(i + 2).getName()
                );
            }
        }
    }

    private Optional<MetroLine> getMetroLine(String lineName) {
        return metroLines.stream()
                .filter(it -> lineName.equals(it.getLineName()))
                .findFirst();
    }
}
