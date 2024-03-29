package edu.lysak.hypermetro.example_stage4;//package metro.example_stage4;
//
//import metro.Main;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Scanner;
//
//public class UserInterface {
//    Scanner scanner = new Scanner(System.in);
//    HashMap<String, LinkedList<Station>> metroData = new HashMap<>();
//    String[] command;
//    Matrix matrix = new Matrix();
//
//    public void start() {
//
//        InfoReader infoReader = new InfoReader();
//        metroData = infoReader.readFile(Main.FILE_NAME, metroData);
//
//        getCommand();
//
//        while (!command[0].equals("/exit")) {
//            switch (command[0]) {
//                case ("/append"):
//                    if (command[1] != null && command[2] != null) {
//                        appendStation(metroData, command[1], command[2]);
//                    } else {
//                        System.out.println("Invalid command");
//                    }
//                    break;
//                case ("/add-head"):
//                    if (command[1] != null && command[2] != null) {
//                        addStation(metroData, command[1], command[2]);
//                    } else {
//                        System.out.println("Invalid command");
//                    }
//                    break;
//                case ("/remove"):
//                    if (command[1] != null && command[2] != null) {
//                        removeStation(metroData, command[1], command[2]);
//                    } else {
//                        System.out.println("Invalid command");
//                    }
//                    break;
//                case ("/output"):
//                    if (command[1] != null) {
//                        printLineStations(metroData, command[1]);
//                    } else {
//                        System.out.println("Invalid command");
//                    }
//                    break;
//                case ("/connect"):
//                    connectStations(metroData, command[1], command[2], command[3], command[4]);
//                    break;
//                case ("/route"):
//                    matrix.generateAdjacency(metroData);
//                    matrix.findWays(command[1], command[2], command[3], command[4], metroData);
//                    matrix.outputWay(metroData);
//                    break;
//                default:
//                    System.out.println("Invalid command");
//                    break;
//
//            }
//            getCommand();
//        }
//    }
//
//
//    public void getCommand() {
//
//        String[] inputLine = scanner.nextLine().split(" ");
//        command = new String[5];
//        int indInCom = 0;
//        int indInLine = 0;
//
//        while (indInLine < inputLine.length) {
//            if (inputLine[indInLine].startsWith("\"")) {
//                command[indInCom] = inputLine[indInLine];
//                if (indInCom == inputLine.length - 1) {
//                    command[indInCom] = command[indInCom].replaceAll("\"", "");
//                    return;
//                }
//                if (!inputLine[indInLine].endsWith("\"")) {
//                    for (int j = indInLine + 1; j < inputLine.length; j++) {
//                        if (inputLine[j].endsWith("\"")) {
//                            command[indInCom] += " " + inputLine[j];
//                            command[indInCom] = command[indInCom].replaceAll("\"", "");
//                            indInCom++;
//                            indInLine = j + 1;
//                            break;
//                        } else {
//                            command[indInCom] += " " + inputLine[j];
//                        }
//                    }
//                } else {
//                    command[indInCom] = command[indInCom].replaceAll("\"", "");
//                    indInLine++;
//                    indInCom++;
//                }
//            } else if (!inputLine[indInLine].endsWith("\"")) {
//                command[indInCom] = inputLine[indInLine];
//                indInCom++;
//                indInLine++;
//            } else {
//                indInLine++;
//            }
//        }
//    }
//
//    public void appendStation(HashMap<String, LinkedList<Station>> _metroData, String _lineName, String _stationName) {
//        if (_metroData.containsKey(_lineName)) {
//            LinkedList<Station> stations = _metroData.get(_lineName);
//            stations.add(stations.size() - 1, new Station(_stationName));
//            _metroData.put(_lineName, stations);
//        } else {
//            System.out.println("Error: invalid line name");
//        }
//    }
//
//    public void addStation(HashMap<String, LinkedList<Station>> _metroData, String _lineName, String _stationName) {
//        if (_metroData.containsKey(_lineName)) {
//            LinkedList<Station> stations = _metroData.get(_lineName);
//            stations.add(1, new Station(_stationName));
//            _metroData.put(_lineName, stations);
//        } else {
//            System.out.println("Error: invalid line name");
//        }
//    }
//
//    public void removeStation(HashMap<String, LinkedList<Station>> _metroData, String _lineName, String _stationName) {
//        if (_metroData.containsKey(_lineName)) {
//            LinkedList<Station> stations = _metroData.get(_lineName);
//            for (int i = 0; i < stations.size(); i++) {
//                if (stations.get(i).name.equals(_stationName)) {
//                    stations.remove(i);
//                    break;
//                }
//            }
//            _metroData.put(_lineName, stations);
//        } else {
//            System.out.println("Error: invalid line name");
//        }
//    }
//
//
//    public void printLineStations(HashMap<String, LinkedList<Station>> _metroData, String _lineName) {
//        if (_metroData.containsKey(_lineName)) {
//
//            LinkedList<Station> stations = _metroData.get(_lineName);
//
//            for (Station station : stations) {
//                System.out.printf("%s\n", getStationOnOutput(station));
//            }
//
//        } else {
//            System.out.println("Error: invalid line name");
//        }
//    }
//
//
//    public String getStationOnOutput(Station _station) {
//        if (_station.transfer.isEmpty()) {
//            return _station.name;
//        } else {
//            String transfer = "";
//            for (String lines : _station.transfer.keySet()) {
//                for (String station : _station.transfer.get(lines)) {
//                    transfer = " - " + station + " (" + lines + " lines)";
//                }
//            }
//            return _station.name + transfer;
//        }
//    }
//
//    public void connectStations(HashMap<String, LinkedList<Station>> _metroData, String _line1, String _stationName1,
//                                String _line2, String _stationName2) {
//
//        LinkedList<Station> lineStations1 = _metroData.get(_line1);
//        LinkedList<Station> lineStations2 = _metroData.get(_line2);
//        for (Station eachStation : lineStations1) {
//            if (eachStation.name.equals(_stationName1)) {
//                HashMap<String, ArrayList<String>> newTransfer = eachStation.transfer;
//                ArrayList<String> currentTransfer;
//                if (newTransfer.containsKey(_line2)) {
//                    currentTransfer = newTransfer.get(_line2);
//                } else {
//                    currentTransfer = new ArrayList<>();
//                }
//                currentTransfer.add(_stationName2);
//                newTransfer.put(_line2, currentTransfer);
//                eachStation.transfer = newTransfer;
//            }
//        }
//
//        for (Station eachStation : lineStations2) {
//            if (eachStation.name.equals(_stationName2)) {
//                HashMap<String, ArrayList<String>> newTransfer = eachStation.transfer;
//                ArrayList<String> currentTransfer;
//                if (newTransfer.containsKey(_line1)) {
//                    currentTransfer = newTransfer.get(_line1);
//                } else {
//                    currentTransfer = new ArrayList<>();
//                }
//                currentTransfer.add(_stationName1);
//                newTransfer.put(_line1, currentTransfer);
//                eachStation.transfer = newTransfer;
//            }
//        }
//    }
//}
