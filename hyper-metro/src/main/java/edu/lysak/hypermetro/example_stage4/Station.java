package edu.lysak.hypermetro.example_stage4;

import java.util.ArrayList;
import java.util.HashMap;

public class Station {
    String name;
    int nodeNumber;
    int time;
    HashMap<String, ArrayList<String>> transfer = new HashMap<>();

    public Station() {
    }

    public Station(String _name) {
        this.name = _name;
    }

}
