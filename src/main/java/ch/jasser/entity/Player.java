package ch.jasser.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                ", name='" + name + '\'' +
                '}';
    }
}
