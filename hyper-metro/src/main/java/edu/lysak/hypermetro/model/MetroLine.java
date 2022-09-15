package edu.lysak.hypermetro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;

@Getter
@AllArgsConstructor
public class MetroLine {
    private String lineName;
    private LinkedList<Station> stations;
}
