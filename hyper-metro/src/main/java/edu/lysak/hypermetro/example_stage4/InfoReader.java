package edu.lysak.hypermetro.example_stage4;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class InfoReader {

    public HashMap<String, LinkedList<Station>> readFile(String _fileName, HashMap<String, LinkedList<Station>> _metroData) {

        File file = new File(_fileName);
        StringBuilder result = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNext()) {
                String readLine = scanner.nextLine();
                result.append(readLine).append("\n");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: No file found: " + _fileName);
            System.exit(0);
        }

        jsonConverter(result.toString(), _metroData);
        return _metroData;
    }

    public void jsonConverter(String _json_string, HashMap<String, LinkedList<Station>> _metroData) {
        JsonObject elem = JsonParser.parseString(_json_string).getAsJsonObject();
        String[] lineName = elem.keySet().toString().replaceAll("[\\[\\]]", "").split(", ");
        for (String eachLine : lineName) {

            LinkedList<Station> lineWay = new LinkedList<>();

            JsonObject lineObj = elem.getAsJsonObject(eachLine);
            int index = 1;
            while (true) {
                JsonObject stationObj = lineObj.getAsJsonObject(String.valueOf(index));
                if (stationObj == null) {
                    break;
                }
                String stationName = stationObj.get("name").toString().replaceAll("\"", "");
                String transferLine = null;
                String transferStat = null;

                String transfer = stationObj
                        .get("transfer")
                        .toString()
                        .replaceAll("\\[", "")
                        .replaceAll("]", "");

                if (!transfer.equals("")) {
                    JsonObject transferObj = JsonParser.parseString(transfer).getAsJsonObject();
                    transferLine = transferObj.get("line").toString().replaceAll("\"", "");
                    transferStat = transferObj.get("station").toString().replaceAll("\"", "");

                }

                Station station = new Station();
                station.name = stationName;
                if (transferLine != null) {
                    ArrayList<String> currentStation = new ArrayList<>();
                    currentStation.add(transferStat);
                    station.transfer.put(transferLine, currentStation);
                }
                lineWay.add(station);
                index++;
            }

            lineWay.addFirst(new Station("depot"));
            lineWay.addLast(new Station("depot"));
            _metroData.put(eachLine, lineWay);
        }
    }
}
