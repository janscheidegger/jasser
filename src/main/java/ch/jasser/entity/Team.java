package ch.jasser.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Team {

    private String name;
    private List<String> players = new ArrayList<>();
    private List<Integer> pointsPerRound = new ArrayList<>();

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

    public String getName() {
        return name;
    }

    public List<String> getPlayers() {
        return players;
    }

    public List<Integer> getPointsPerRound() {
        return Collections.unmodifiableList(pointsPerRound);
    }

    public int getPoints() {
        return pointsPerRound.stream()
                             .mapToInt(Integer::valueOf)
                             .sum();
    }

    public Team addPoints(int points, int currentTurn) {
        Team team = Team.of(name, players);

        if (currentTurn == pointsPerRound.size()) {
            team.pointsPerRound = Stream.concat(pointsPerRound.stream(),
                    Stream.of(points))
                                        .collect(Collectors.toUnmodifiableList());
        } else if (currentTurn < pointsPerRound.size()) {
            List<Integer> tmp = new ArrayList<>(pointsPerRound);
            tmp.set(currentTurn, tmp.get(currentTurn) + points);
            team.pointsPerRound = Collections.unmodifiableList(tmp);
        }
        return team;
    }

    public Team addPlayer(String player) {
        return Team.of(this.name, Stream.concat(this.players.stream(), Stream.of(player))
                                        .collect(Collectors.toList()));
    }
}

