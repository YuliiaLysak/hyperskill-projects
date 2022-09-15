package edu.lysak.hypermetro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Station {
    private final int id;
    private final String name;
    private final List<Transfer> transfer;
}
