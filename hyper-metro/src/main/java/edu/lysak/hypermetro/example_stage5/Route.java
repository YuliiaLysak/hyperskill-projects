package edu.lysak.hypermetro.example_stage5;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Route {
    private Station start;
    private Station dest;
    private Tracker tracker;


    public Route(Station start, Station dest) {
        this.start = start;
        this.dest = dest;
        this.tracker = initTracker();
    }

    private Tracker initTracker() {
        Tracker tracker = new Tracker();
        Deque<Station> queue = new ArrayDeque<>();

        queue.add(start);
        while (!queue.isEmpty()) {
            Station station = queue.pollLast();

            if (!tracker.containsStation(station)) {
                tracker.addStation(station);

                for (Station neighbor : station.getNeighbors()) {
                    queue.offerFirst(neighbor);
                }
            }
        }

        return tracker;
    }

    public void findAndPrintFastestRoute() {
        runDijkstraAlgo();
        List<Station> path = constructPath();
        printFastestRoute(path);
    }

    private void runDijkstraAlgo() {
        while (!tracker.hasVisitedAllStations()) {
            Station station = tracker.getUnvisitedMinTimeStation();

            for (Station neighbor : station.getNeighbors()) {
                int newTime = tracker.getTimeFromStart(station) + station.getTimeTo(neighbor);
                tracker.updateTimeAndSourceIfBetter(neighbor, newTime, station);
            }

            tracker.visit(station);
        }
    }

    private void printFastestRoute(List<Station> path) {

        printRoute(path);

        int timeFromStart = tracker.getTimeFromStart(dest);
        System.out.printf("Total: %d minutes in the way\n", timeFromStart);
    }

    public void findAndPrintRoute() {
        runBFSAlgo();
        List<Station> path = constructPath();
        printRoute(path);
    }

    private void runBFSAlgo() {
        Deque<Station> queue = new ArrayDeque<>();
        Map<Station, Station> sourceStation = new HashMap<>();

        queue.add(start);
        sourceStation.put(start, null);


        while (!queue.isEmpty()) {
            Station station = queue.pollLast();

            if (!tracker.visited(station)) {

                for (Station neighbor : station.getNeighbors()) {
                    queue.offerFirst(neighbor);
                    sourceStation.putIfAbsent(neighbor, station);
                }

                tracker.setSource(station, sourceStation.get(station));
                tracker.visit(station);
            }
        }
    }

    private void printRoute(List<Station> path) {
        String currentLine = start.getLineName();

        for (Station station : path) {
            if (!station.getLineName().equals(currentLine)) {
                currentLine = station.getLineName();
                System.out.printf("Transition to line %s\n", currentLine);
            }
            System.out.println(station.getStationName());
        }
    }

    private List<Station> constructPath() {
        List<Station> list = new ArrayList<>();

        Station currentStation = dest;

        while (currentStation != null) {
            list.add(currentStation);
            currentStation = tracker.getSource(currentStation);
        }

        Collections.reverse(list);

        return list;
    }

}
