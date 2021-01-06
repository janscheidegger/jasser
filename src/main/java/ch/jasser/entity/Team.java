package ch.jasser.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Team {

    private String name;
    private List<String> players = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<String> getPlayers() {
        return players;
    }

    public static Team of(String name, String... names) {
        Team team = new Team();
        team.name = name;
        team.players = List.of(names);
        return team;
    }

    public static Team of(String name, List<String> names) {
        Team team = new Team();
        team.name = name;
        team.players = Collections.unmodifiableList(names);
        return team;
    }

    public Team addPlayer(String player) {
        return Team.of(this.name, Stream.concat(this.players.stream(), Stream.of(player)).collect(Collectors.toList()));
    }
}

