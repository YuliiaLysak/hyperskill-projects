package edu.lysak.hypermetro;

import edu.lysak.hypermetro.model.MetroLine;
import edu.lysak.hypermetro.model.Station;
import edu.lysak.hypermetro.model.Transfer;
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
        System.out.println("Metro lines initialization finished, lines count = " + metroLines.size());
    }

    public void append(String lineName, String stationName) {
        Optional<MetroLine> metroLine = getMetroLine(lineName);
        if (metroLine.isPresent()) {
            LinkedList<Station> stations = metroLine.get().getStations();
            int lastId = stations.getLast().getId();
            stations.addLast(new Station(++lastId, stationName, List.of()));
        }
    }

    public void addHead(String lineName, String stationName) {
        Optional<MetroLine> metroLine = getMetroLine(lineName);
        if (metroLine.isPresent()) {
            LinkedList<Station> stations = metroLine.get().getStations();
            int firstId = stations.getFirst().getId();
            stations.addFirst(new Station(--firstId, stationName, List.of()));
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

    public void connect(String lineName1, String stationName1, String lineName2, String stationName2) {
        Optional<MetroLine> metroLine1 = getMetroLine(lineName1);
        Optional<MetroLine> metroLine2 = getMetroLine(lineName2);
        if (metroLine1.isPresent() && metroLine2.isPresent()) {
            connectStation(lineName2, stationName2, stationName1, metroLine1.get());
            connectStation(lineName1, stationName1, stationName2, metroLine2.get());
        }
    }

    private void connectStation(String lineNameToAdd, String stationNameToAdd, String srcStationName, MetroLine srcMetroLine) {
        LinkedList<Station> srcStations = srcMetroLine.getStations();
        Optional<Station> srcStation = srcStations.stream()
                .filter(it -> srcStationName.equals(it.getName()))
                .findFirst();
        if (srcStation.isPresent()) {
            List<Transfer> srcTransfer2 = srcStation.get().getTransfer();
            srcTransfer2.add(new Transfer(lineNameToAdd, stationNameToAdd));
        }
    }

//    public void output(String lineName) {
//        Optional<MetroLine> metroLine = getMetroLine(lineName);
//        if (metroLine.isPresent()) {
//            LinkedList<Station> stations = new LinkedList<>(metroLine.get().getStations());
//            stations.sort(Comparator.comparing(Station::getId));
//            Station depot = new Station(0, "depot", List.of());
//            stations.addFirst(depot);
//            stations.addLast(depot);
//            for (int i = 0; i < stations.size() - 2; i++) {
//                System.out.printf("%s - %s - %s%n",
//                        stations.get(i).getName(),
//                        stations.get(i + 1).getName(),
//                        stations.get(i + 2).getName()
//                );
//            }
//        }
//    }

    public void outputWithTransfer(String lineName) {
        Optional<MetroLine> metroLine = getMetroLine(lineName);
        if (metroLine.isPresent()) {
            LinkedList<Station> stations = new LinkedList<>(metroLine.get().getStations());
            stations.sort(Comparator.comparing(Station::getId));
            System.out.println("depot");
            for (Station station : stations) {
                StringBuilder output = new StringBuilder(station.getName());
                List<Transfer> transfer = station.getTransfer();
                if (transfer.isEmpty()) {
                    System.out.println(output);
                    continue;
                }
                transfer.forEach(t -> {
                    output.append(String.format(" - %s (%s line)",
                            t.getStation(),
                            t.getLine()
                    ));
                    System.out.println(output);
                });
            }
            System.out.println("depot");
        }
    }

    private Optional<MetroLine> getMetroLine(String lineName) {
        return metroLines.stream()
                .filter(it -> lineName.equals(it.getLineName()))
                .findFirst();
    }
}
