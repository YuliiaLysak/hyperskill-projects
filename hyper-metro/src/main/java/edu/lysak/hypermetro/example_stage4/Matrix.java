package edu.lysak.hypermetro.example_stage4;//package metro.example_stage4;
//
//import metro.Main;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.LinkedList;
//
//public class Matrix {
//    int[][] adjacency;
//    LinkedList<Integer> resultSet;
//
//    public void generateAdjacency(HashMap<String, LinkedList<Station>> _metroData) {
//        int size = 0;
//
//        for (String each : _metroData.keySet()) {
//            size += _metroData.get(each).size() - 2;
//        }
//
//        adjacency = new int[size][size];
//        for (int i = 0; i < adjacency.length; i++) {
//            for (int j = 0; j < adjacency.length; j++) {
//                adjacency[i][j] = 0;
//            }
//        }
//
//        int index = 0;
//        int adjIndex = 0;
//
//        for (String eachLine : _metroData.keySet()) {
//            LinkedList<Station> eachStationList = _metroData.get(eachLine);
//            for (int i = 1; i < eachStationList.size() - 1; i++) {
//                if (!eachStationList.get(i).name.equals("depot")) {
//                    eachStationList.get(i).nodeNumber = index;
//                    index++;
//                }
//            }
//            _metroData.put(eachLine, eachStationList);
//        }
//
//        for (String eachLine : _metroData.keySet()) {
//            LinkedList<Station> eachStationList = _metroData.get(eachLine);
//
//            for (int i = 1; i < eachStationList.size() - 2; i++) {
//                if (!eachStationList.get(i).name.equals("depot")) {
//
//                    if (adjIndex + 1 < adjacency.length) {
//                        adjacency[adjIndex][adjIndex + 1] = 3;
//                        adjacency[adjIndex + 1][adjIndex] = 3;
//
//                        if (eachStationList.get(i).transfer.size() != 0) {
//
//                            for (String eachTransferLine : eachStationList.get(i).transfer.keySet()) {
//
//                                ArrayList<String> eachTransferStation = eachStationList.get(i)
//                                        .transfer
//                                        .get(eachTransferLine);
//
//                                for (String each : eachTransferStation) {
//                                    LinkedList<Station> stations = _metroData.get(eachTransferLine);
//                                    int transferIndex;
//                                    for (Station station : stations) {
//                                        if (station.name.equals(each)) {
//                                            transferIndex = station.nodeNumber;
//                                            adjacency[adjIndex][transferIndex] = 1;
//                                            adjacency[transferIndex][adjIndex] = 1;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        adjIndex++;
//                    }
//                }
//            }
//            adjIndex++;
//            _metroData.put(eachLine, eachStationList);
//        }
//
//
//        for (int i = 0; i < adjacency.length; i++) {
//            adjacency[i][i] = 0;
//        }
//
//        if (Main.DEBUG) {
//            System.out.println("ADJACENCY SIZE: " + adjacency.length);
//            for (int[] ints : adjacency) {
//                for (int j = 0; j < adjacency.length; j++) {
//                    System.out.print(ints[j] + ",");
//                }
//                System.out.println();
//            }
//        }
//
//    }
//
//    public void findWays(String _line1, String _station1, String _line2, String _station2, HashMap<String, LinkedList<Station>> _metroData) {
//        char[] known = new char[adjacency.length];
//        int[] cost = new int[adjacency.length];
//        int[] path = new int[adjacency.length];
//
//        Arrays.fill(cost, Integer.MAX_VALUE);
//        Arrays.fill(path, Integer.MAX_VALUE);
//
//        int index = findIndexInLine(_line1, _station1, _metroData);
//        if (Main.DEBUG) System.out.println("Начальный узел: " + index);
//        known[index] = 'T';
//        cost[index] = 0;
//        path[index] = -1;
//        for (int j = 0; j < adjacency.length; j++) {
//            for (int i = 1; i < adjacency.length; i++) {
//                if (adjacency[index][i] > 0 && known[i] != 'T') {
//                    if (cost[i] > cost[index] + adjacency[index][i]) {
//                        cost[i] = cost[index] + adjacency[index][i];
//                        path[i] = index;
//                    }
//                }
//            }
//            int min = Integer.MAX_VALUE;
//            for (int i = 0; i < cost.length; i++) {
//                if (known[i] != 'T' && cost[i] < min) {
//                    min = cost[i];
//                    index = i;
//                }
//            }
//            known[index] = 'T';
//        }
//
//        if (Main.DEBUG) {
//            System.out.println("Vertex  Known  Cost  Path");
//            for (int i = 0; i < cost.length; i++) {
//                System.out.println("  " + i + "      " + known[i] + "    " + cost[i] + "   " + path[i]);
//            }
//        }
//
//
//        index = findIndexInLine(_line2, _station2, _metroData);
//        if (Main.DEBUG) System.out.println("Конечный узел: " + index);
//        resultSet = new LinkedList<>();
//        resultSet.addFirst(index);
//        while (path[index] != -1) {
//            resultSet.addFirst(path[index]);
//            index = path[index];
//        }
//
//        if (Main.DEBUG) {
//            System.out.println("Путь:" + Arrays.toString(resultSet.toArray()));
//            System.out.println("К-во узлов:" + resultSet.size());
//        }
//    }
//
//    public void outputWay(HashMap<String, LinkedList<Station>> _metroData) {
//        String[][] resultWay = new String[resultSet.size()][2];
//
//        for (int i = 0; i < resultSet.size(); i++) {
//            boolean isFound = false;
//            for (String eachLine : _metroData.keySet()) {
//                LinkedList<Station> eachStationList = _metroData.get(eachLine);
//                for (Station eachStation : eachStationList) {
//                    if (eachStation.nodeNumber == resultSet.get(i) && !eachStation.name.equals("depot")) {
//                        resultWay[i][0] = eachLine;
//                        resultWay[i][1] = eachStation.name;
//                        isFound = true;
//                        break;
//                    }
//                }
//                if (isFound) {
//                    break;
//                }
//            }
//        }
//
//        String currentLine = resultWay[0][0];
//        for (int i = 0; i < resultWay.length; i++) {
//            if (i + 1 < resultWay.length) {
//                if (currentLine.equals(resultWay[i + 1][0])) {
//                    System.out.println(resultWay[i][1]);
//                } else {
//                    System.out.println(resultWay[i][1]);
//                    System.out.println("Transition to line " + resultWay[i + 1][0]);
//                    currentLine = resultWay[i + 1][0];
//                }
//            } else {
//                System.out.println(resultWay[i][1]);
//            }
//        }
//    }
//
//    public int findIndexInLine(String _line, String _station, HashMap<String, LinkedList<Station>> _metroData) {
//        for (String eachLine : _metroData.keySet()) {
//            if (eachLine.equals(_line)) {
//                LinkedList<Station> eachStationList = _metroData.get(eachLine);
//                for (Station eachStation : eachStationList) {
//                    if (eachStation.name.equals(_station)) {
//                        return eachStation.nodeNumber;
//                    }
//                }
//            }
//        }
//        System.out.println("ERROR finding name station!");
//        return -1;
//    }
//
//}
