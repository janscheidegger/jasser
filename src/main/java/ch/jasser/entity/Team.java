package ch.jasser.entity;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private String name;
    private List<String> players = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void addPlayer(String playername) {
        this.players.add(playername);
    }
}

