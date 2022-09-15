package edu.lysak.hypermetro.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import edu.lysak.hypermetro.exception.MetroException;
import edu.lysak.hypermetro.model.MetroLine;
import edu.lysak.hypermetro.model.Station;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetroJsonParser {

    public List<MetroLine> parseJsonFile(String filePath) {
        Path path = Path.of(filePath);

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonElement tree = JsonParser.parseReader(reader);
            JsonObject jsonObject = tree.getAsJsonObject();
            return jsonObject.entrySet().stream()
                    .map(MetroJsonParser::parseMetroLine)
                    .toList();
        } catch (JsonParseException e) {
            throw new MetroException("Incorrect file");
        } catch (IOException ex) {
            throw new MetroException("Error! Such a file doesn't exist!");
        }
    }

    private static MetroLine parseMetroLine(Map.Entry<String, JsonElement> line) {
        String lineName = line.getKey();
        JsonObject stationJson = line.getValue().getAsJsonObject();
        LinkedList<Station> stations = stationJson.entrySet().stream()
                .map(MetroJsonParser::parseStation)
                .collect(Collectors.toCollection(LinkedList::new));
        return new MetroLine(lineName, stations);
    }

    private static Station parseStation(Map.Entry<String, JsonElement> station) {
        return new Station(
                Integer.parseInt(station.getKey()),
                station.getValue().getAsString()
        );
    }
}
